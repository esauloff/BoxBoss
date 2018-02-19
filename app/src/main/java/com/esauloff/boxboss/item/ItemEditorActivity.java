package com.esauloff.boxboss.item;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.storage.ItemDatabase;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ItemEditorActivity extends Activity {
    private static final String ITEM_EXTRA = "item";
    private static final String ITEM_ID_EXTRA = "itemId";

    private EditText nameEdit;
    private EditText commentEdit;
    private Button pickColorButton;
    private Button saveButton;

    private int itemColor;
    private AlertDialog colorPickerDialog;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Item item;
    private ItemDatabase itemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        nameEdit = findViewById(R.id.edit_name);
        commentEdit = findViewById(R.id.edit_comment);
        pickColorButton = findViewById(R.id.btn_pickColor);
        saveButton = findViewById(R.id.btn_save);

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                saveButton.setEnabled(charSequence != null && charSequence.length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        saveButton.setEnabled(false);

        Serializable extra = getIntent().getSerializableExtra(ITEM_EXTRA);
        if(extra instanceof Item) {
            item = (Item)extra;

            nameEdit.setText(item.getName());
            commentEdit.setText(item.getComment());
            itemColor = item.getColor();
            pickColorButton.setBackgroundColor(itemColor);
        }
        else {
            item = new Item();
        }

        colorPickerDialog = ColorPickerDialogBuilder.with(this).setTitle("Choose color").density(5)
                .initialColor(itemColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        itemColor = selectedColor;
                        pickColorButton.setBackgroundColor(itemColor);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) { }
                }).build();

        itemDatabase = ItemDatabase.getInstance(this);
    }

    public void pickColor(View view) {
        colorPickerDialog.show();
    }

    public void save(View view) {
        item.setName(nameEdit.getText().toString());
        item.setComment(commentEdit.getText().toString());
        item.setColor(itemColor);

        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int id = 0;

                if(item.getId() != 0) {
                    if(itemDatabase.itemDao().update(item) == 1) {
                        id = item.getId();
                    }
                    else {
                        throw new Exception("An error occurred while updating item in database");
                    }
                }
                else {
                    id = (int)itemDatabase.itemDao().insert(item);
                    if(id == -1) {
                        throw new Exception("An error occurred while inserting item into database");
                    }
                }

                return id;
            }
        });

        int id = 0;
        try {
            id = future.get(5, TimeUnit.SECONDS);
        }
        catch(TimeoutException | ExecutionException | InterruptedException exc) {
            future.cancel(true);

            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT);
            exc.printStackTrace();
        }

        Intent intent = new Intent();
        intent.putExtra(ITEM_ID_EXTRA, id);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}


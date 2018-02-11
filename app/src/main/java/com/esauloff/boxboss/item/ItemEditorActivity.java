package com.esauloff.boxboss.item;

import android.app.Activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.storage.ItemDatabase;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class ItemEditorActivity extends Activity {
    private EditText nameEdit;
    private EditText commentEdit;
    private Button pickColorButton;
    private Button saveButton;

    private TextWatcher nameEditWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            saveButton.setEnabled(charSequence != null && charSequence.length() != 0);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    private int itemColor;
    private AlertDialog colorPickerDialog;
    private ColorPickerClickListener colorPickerListener = new ColorPickerClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
            itemColor = selectedColor;
            pickColorButton.setBackgroundColor(itemColor);
        }
    };

    private ItemDatabase itemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        nameEdit = findViewById(R.id.edit_name);
        commentEdit = findViewById(R.id.edit_comment);
        pickColorButton = findViewById(R.id.btn_pickColor);
        saveButton = findViewById(R.id.btn_save);

        nameEdit.addTextChangedListener(nameEditWatcher);
        saveButton.setEnabled(false);

        colorPickerDialog = ColorPickerDialogBuilder.with(this).setTitle("Choose color").initialColor(0).density(5)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .setPositiveButton("OK", colorPickerListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) { }
                })
                .build();

        itemDatabase = ItemDatabase.getInstance(this);
    }

    public void pickColor(View view) {
        colorPickerDialog.show();
    }

    public void save(View view) {
        final Item item = new Item(nameEdit.getText().toString(), commentEdit.getText().toString(), itemColor);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                itemDatabase.itemDao().insertAll(item);
            }
        });

        setResult(RESULT_OK);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}


package com.esauloff.boxboss.item;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;

public class ItemEditorActivity extends Activity {
    private EditText nameEdit = null;
    private EditText commentEdit = null;
    private Button saveButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        nameEdit = findViewById(R.id.edit_name);
        commentEdit = findViewById(R.id.edit_comment);
        saveButton = findViewById(R.id.btn_save);

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                saveButton.setEnabled(charSequence != null && charSequence.length() != 0);
            }
        });
        saveButton.setEnabled(false);
    }

    public void save(View view) {
        Item item = new Item(nameEdit.getText().toString(), commentEdit.getText().toString());
    }

    public void cancel(View view) {
        finish();
    }
}


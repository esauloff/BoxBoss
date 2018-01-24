package com.esauloff.boxboss.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.esauloff.boxboss.R;

public class ItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }

    public void openItemEditor(View view) {
        Intent intent = new Intent(this, ItemEditorActivity.class);
        startActivity(intent);
    }
}


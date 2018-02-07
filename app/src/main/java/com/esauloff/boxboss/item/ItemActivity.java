package com.esauloff.boxboss.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.esauloff.boxboss.R;

public class ItemActivity extends Activity {
    private static final int ITEM_EDITOR_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ITEM_EDITOR_ACTIVITY && resultCode == RESULT_OK) {

        }
    }

    public void openItemEditor(View view) {
        Intent intent = new Intent(this, ItemEditorActivity.class);
        startActivityForResult(intent, ITEM_EDITOR_ACTIVITY);
    }
}


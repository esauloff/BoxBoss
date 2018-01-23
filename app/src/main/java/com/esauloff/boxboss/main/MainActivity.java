package com.esauloff.boxboss.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.item.ItemActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openItems(View view) {
        Intent intent = new Intent(this, ItemActivity.class);
        startActivity(intent);
    }
}


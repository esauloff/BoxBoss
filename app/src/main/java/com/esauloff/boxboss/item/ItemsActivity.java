package com.esauloff.boxboss.item;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.storage.ItemDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends Activity {
    private static final int ITEM_EDITOR_ACTIVITY = 1;

    private RecyclerView itemsView;
    private RecyclerView.Adapter itemsViewAdapter;
    private RecyclerView.LayoutManager itemsViewLayoutManager;

    private ItemDatabase itemDatabase;

    private List<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemsView = findViewById(R.id.rview_items);
        itemsView.setHasFixedSize(true);

        itemsViewLayoutManager = new LinearLayoutManager(this);
        itemsView.setLayoutManager(itemsViewLayoutManager);

        itemsViewAdapter = new ItemsViewAdapter(items);
        itemsView.setAdapter(itemsViewAdapter);

        itemDatabase = ItemDatabase.getInstance(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                items.addAll(itemDatabase.itemDao().getAll());
            }
        });

        itemsViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ITEM_EDITOR_ACTIVITY && resultCode == RESULT_OK) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    items.addAll(itemDatabase.itemDao().getAll());
                }
            });
        }

        itemsViewAdapter.notifyDataSetChanged();
    }

    public void openItemEditor(View view) {
        Intent intent = new Intent(this, ItemEditorActivity.class);
        startActivityForResult(intent, ITEM_EDITOR_ACTIVITY);
    }
}


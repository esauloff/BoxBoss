package com.esauloff.boxboss.item;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.View;
import android.widget.Toast;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.item.ItemsViewAdapter.OnItemClickListener;
import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.storage.ItemDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ItemsActivity extends Activity {
    private static final int ITEM_EDITOR_ACTIVITY = 1;
    private static final String ITEM_EXTRA = "item";
    private static final String ITEM_ID_EXTRA = "itemId";

    private RecyclerView itemsView;
    private RecyclerView.Adapter itemsViewAdapter;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private List<Item> items = new ArrayList<Item>();
    private ItemDatabase itemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemsView = findViewById(R.id.rview_items);
        itemsView.setHasFixedSize(true);
        itemsView.setLayoutManager(new LinearLayoutManager(this));

        itemsViewAdapter = new ItemsViewAdapter(items, new OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(ItemsActivity.this, ItemEditorActivity.class);
                intent.putExtra(ITEM_EXTRA, item);
                startActivityForResult(intent, ITEM_EDITOR_ACTIVITY);
            }
        });
        itemsView.setAdapter(itemsViewAdapter);

        new ItemTouchHelper(new SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(getApplicationContext(), "Left swipe", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        }).attachToRecyclerView(itemsView);

        itemDatabase = ItemDatabase.getInstance(this);

        updateView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ITEM_EDITOR_ACTIVITY && resultCode == RESULT_OK) {
            int id = (int)data.getSerializableExtra(ITEM_ID_EXTRA);

            updateView();
        }
    }

    public void openItemEditor(View view) {
        Intent intent = new Intent(this, ItemEditorActivity.class);
        startActivityForResult(intent, ITEM_EDITOR_ACTIVITY);
    }

    private void updateView() {
        Future<List<Item> > future = executor.submit(new Callable<List<Item> >() {
            @Override
            public List<Item> call() throws Exception {
                return itemDatabase.itemDao().getItems();
            }
        });

        try {
            items.removeAll(items);
            items.addAll(future.get(5, TimeUnit.SECONDS));
        }
        catch(TimeoutException | ExecutionException | InterruptedException exc) {
            future.cancel(true);

            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT);
            exc.printStackTrace();
        }

        itemsViewAdapter.notifyDataSetChanged();
    }
}


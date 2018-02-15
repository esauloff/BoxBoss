package com.esauloff.boxboss.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.model.ItemDao;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    private static volatile ItemDatabase instance;

    public abstract ItemDao itemDao();

    public static ItemDatabase getInstance(Context context) {
        if(instance == null) {
            synchronized(ItemDatabase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder( context.getApplicationContext(),
                            ItemDatabase.class, "boxboss.db" ).build();
                }
            }
        }

        return instance;
    }
}


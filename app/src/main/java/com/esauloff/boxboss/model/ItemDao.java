package com.esauloff.boxboss.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    List<Item> getAll();

//    @Query("SELECT * FROM item WHERE")
//    Item getItemById();

    @Insert
    void insertAll(Item... items);
}


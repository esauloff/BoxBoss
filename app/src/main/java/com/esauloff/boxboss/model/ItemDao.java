package com.esauloff.boxboss.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM Items")
    List<Item> getItems();

    @Insert
    long insert(Item item);

    @Update
    int update(Item item);

    @Delete
    void delete(Item item);
}


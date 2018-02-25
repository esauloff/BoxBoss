package com.esauloff.boxboss.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;

import com.esauloff.boxboss.model.converters.BitmapConverter;
import com.esauloff.boxboss.model.converters.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "Items")
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "color")
    private int color;

    @TypeConverters(BitmapConverter.class)
//    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    private BitmapSerializable picture;
//    private Bitmap picture;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "createdDate")
    private Date createdDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "lastModifiedDate")
    private Date lastModifiedDate;

    public Item() {
        createdDate = new Date();
        lastModifiedDate = createdDate;
    }

    public Item(String name, String comment, int color) {
        this.name = name;
        this.comment = comment;
        this.color = color;

        createdDate = new Date();
        lastModifiedDate = createdDate;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* name */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* comment */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /* color */
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /* picture */
    public BitmapSerializable getPicture() {
        return picture;
    }

    public void setPicture(BitmapSerializable picture) {
        this.picture = picture;
    }

    public Bitmap getPictureBitmap() {
        return picture.toBitmap();
    }

    public void setPictureBitmap(Bitmap picture) {
        this.picture = new BitmapSerializable(picture);
    }

    /* createdDate */
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /* lastModifiedDate */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}


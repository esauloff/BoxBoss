package com.esauloff.boxboss.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

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

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @ColumnInfo(name = "color")
    private int color;

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

    /* imagePath */
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /* color */
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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


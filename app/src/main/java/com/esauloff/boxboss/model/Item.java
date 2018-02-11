package com.esauloff.boxboss.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "comment")
    private String comment;
    @ColumnInfo(name = "color")
    private int color;
//    private Date creationDate = null;
//    private Date lastModifiedDate = null;

    public Item(String name, String comment, int color) {
        this.name = name;
        this.comment = comment;
        this.color = color;

//        creationDate = new Date();
//        lastModifiedDate = creationDate;
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

//    /* creationDate */
//    public Date getCreationDate() {
//        return creationDate;
//    }
//
//    private void setCreationDate(Date creationDate) {
//        this.creationDate = creationDate;
//    }

//    /* lastModifiedDate */
//    public Date getLastModifiedDate() {
//        return lastModifiedDate;
//    }
//
//    public void setLastModifiedDate(Date lastModifiedDate) {
//        this.lastModifiedDate = lastModifiedDate;
//    }
}


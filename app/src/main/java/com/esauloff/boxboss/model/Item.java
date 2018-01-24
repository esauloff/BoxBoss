package com.esauloff.boxboss.model;

import java.util.Date;

public class Item {
    private String name = null;
    private String comment = null;
    private Date creationDate = null;
    private Date lastModifiedDate = null;

    public Item(String name, String comment) {
        this.name = name;
        this.comment = comment;

        creationDate = new Date();
        lastModifiedDate = creationDate;
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

    /* creationDate */
    public Date getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /* lastModifiedDate */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}


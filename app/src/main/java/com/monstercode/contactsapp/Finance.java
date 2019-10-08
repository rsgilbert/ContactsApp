package com.monstercode.contactsapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Room table for the contact details saved on the app
 */


@Entity(tableName = "finances")
public class Finance implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "duty")
    private String duty;

    @ColumnInfo(name = "room")
    private String room;

    @ColumnInfo(name = "contact")
    private String contact;

    @ColumnInfo(name = "lastChecked")
    private Long lastChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Long lastChecked) {
        this.lastChecked = lastChecked;
    }
}


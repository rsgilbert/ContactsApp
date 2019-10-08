package com.monstercode.contactsapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Room table for the contact details saved on the app
 */


@Entity(tableName = "details")
public class Detail implements Serializable {
    @PrimaryKey(autoGenerate=true)
    private int id;

    @ColumnInfo(name="firstname")
    private String firstname;

    @ColumnInfo(name="lastname")
    private String lastname;

    @ColumnInfo(name="tel1")
    private String tel1;

    @ColumnInfo(name="tel2")
    private String tel2;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="job")
    private String job;

    @ColumnInfo(name = "sitename")
    private String sitename;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "lastChecked")
    private Long lastChecked;

    /**
     * Getters and Setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Long lastChecked) {
        this.lastChecked = lastChecked;
    }


}

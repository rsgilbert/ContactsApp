package com.monstercode.contactsapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DetailDao {
    @Query("SELECT * FROM details")
    List<Detail> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Detail detail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Detail> details);

}

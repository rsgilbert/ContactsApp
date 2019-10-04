package com.monstercode.contactsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FinanceDao {
    @Query("SELECT * FROM finances")
    List<Detail> getAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Finance finance);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Finance> finances);

    @Delete()
    void deleteOne(Finance finance);

}

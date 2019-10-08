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
    List<Finance> getAll();

    @Query("SELECT * FROM finances WHERE id = :financeId")
    Finance getOne(int financeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Finance finance);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Finance> finances);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOne(Finance finance);

    @Delete()
    void deleteOne(Finance finance);


}

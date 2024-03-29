package com.monstercode.contactsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DetailDao {
    @Query("SELECT * FROM details ORDER BY lastChecked DESC")
    List<Detail> getAll();

    @Query("SELECT * FROM details WHERE id = :detailId")
    Detail getOne(int detailId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Detail detail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Detail> details);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOne(Detail detail);

    @Delete()
    void deleteOne(Detail detail);

}

package com.monstercode.contactsapp;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database used by the app, defines the tables to include
 * as well as Data Entry Objects (dao) to use
 */

@Database(entities = {Detail.class, Finance.class, }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DetailDao detailDao();
    public abstract FinanceDao financeDao();
}

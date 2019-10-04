package com.monstercode.contactsapp;


import android.content.Context;
import androidx.room.Room;

/**
 * Provides connection to the AppDatabase
 */
public class DatabaseClient {
    private Context context;
    private static DatabaseClient mInstance;

    // db object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "db")
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }
    public AppDatabase getAppDatabase () {
        return appDatabase;
    }
}

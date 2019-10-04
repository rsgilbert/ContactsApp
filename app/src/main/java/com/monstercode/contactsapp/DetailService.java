package com.monstercode.contactsapp;

import android.content.SharedPreferences;

import com.monstercode.contactsapp.data.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface DetailService {
    @GET("/contacts/")
    public Call<List<Detail>> getDetails(
            @Query("query") String query
    );
}

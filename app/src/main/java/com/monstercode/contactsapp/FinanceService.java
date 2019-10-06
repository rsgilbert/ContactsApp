package com.monstercode.contactsapp;

import android.content.SharedPreferences;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface FinanceService {
    @GET("/finance/")
    public Call<List<Finance>> getFinance(
            @Query("query") String query
    );
}

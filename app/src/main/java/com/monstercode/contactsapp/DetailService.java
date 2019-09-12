package com.monstercode.contactsapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface DetailService {
    @GET("/contacts/")
    public Call<List<Detail>> getDetails();

    @GET("/contacts/{detailId}")
    public Call<Detail> getDetail(
            @Path("detailId") int detailId
    );
    @GET("/contacts?{queryString}")
    public Call<Detail> queryDetails(
            @Path("queryString") String query
    );
}


package com.example.travelplanner.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static RetrofitInstance instance;
    public ApiInterface apiInterface;
    public static String IPAddress = "http://192.168.1.12:5000";
    RetrofitInstance() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(IPAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       apiInterface =  retrofit.create(ApiInterface.class);
    }
    public static RetrofitInstance getInstance() {
        if (instance == null) {
            instance = new RetrofitInstance();
        }
        return instance;
    }
}

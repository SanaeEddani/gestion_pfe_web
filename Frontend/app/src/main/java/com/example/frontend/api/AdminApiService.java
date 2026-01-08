package com.example.frontend.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminApiService {

    private static AdminApi instance;

    // Remplace l'URL par celle de ton backend
    private static final String BASE_URL = "http://10.41.223.37:9090/";

    public static AdminApi getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(AdminApi.class);
        }
        return instance;
    }


}

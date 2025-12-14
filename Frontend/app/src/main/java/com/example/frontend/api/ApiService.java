package com.example.frontend.api;

import com.example.frontend.model.UserRequest;
import com.example.frontend.model.UserResponse;
import com.example.frontend.model.Appogee;
import com.example.frontend.model.CodeProf;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // Inscription utilisateur
    @POST("/api/register")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    // Liste des codes Apogée (si jamais tu veux récupérer depuis backend)
    @GET("/api/appogees")
    Call<List<Appogee>> getAppogees();

    // Liste des codes Prof
    @GET("/api/codeprofs")
    Call<List<CodeProf>> getCodeProfs();
}

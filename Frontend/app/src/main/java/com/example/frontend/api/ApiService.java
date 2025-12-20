package com.example.frontend.api;

import com.example.frontend.model.EtudiantProfile;
import com.example.frontend.model.UserRequest;
import com.example.frontend.model.UserResponse;
import com.example.frontend.model.Appogee;
import com.example.frontend.model.CodeProf;
import com.example.frontend.model.Projet;
import com.example.frontend.model.Remarque;
import com.example.frontend.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Inscription utilisateur
    @POST("/api/register")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    // Liste des codes Apogée
    @GET("/api/appogees")
    Call<List<Appogee>> getAppogees();

    // Liste des codes Prof
    @GET("/api/codeprofs")
    Call<List<CodeProf>> getCodeProfs();
    @GET("/utilisateurs/profile")
    Call<EtudiantProfile> getEtudiantProfile(
            @Header("Authorization") String token
    );


}
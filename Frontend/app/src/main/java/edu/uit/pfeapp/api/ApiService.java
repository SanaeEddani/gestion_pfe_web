package edu.uit.pfeapp.api;

import edu.uit.pfeapp.model.UserRequest;
import edu.uit.pfeapp.model.UserResponse;
import edu.uit.pfeapp.model.Appogee;
import edu.uit.pfeapp.model.CodeProf;

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

package com.example.frontend.api;

import com.example.frontend.model.EtudiantProjetDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EncadrantApi {

    @GET("api/encadrant/etudiants")
    Call<List<EtudiantProjetDTO>> getEtudiants(
            @Query("filiere") String filiere
    );



    @POST("api/encadrant/encadrer/{projetId}")
    Call<String> encadrer(
            @Path("projetId") int projetId,
            @Query("encadrantId") int encadrantId
    );
}

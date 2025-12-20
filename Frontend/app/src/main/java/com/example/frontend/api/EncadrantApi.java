package com.example.frontend.api;

import com.example.frontend.model.EtudiantProjetDTO;
import com.example.frontend.model.EncadrerRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EncadrantApi {

    /* ==============================
       LISTE DES ÉTUDIANTS DISPONIBLES
       ============================== */
    @GET("api/encadrant/etudiants")
    Call<List<EtudiantProjetDTO>> getEtudiantsDisponibles(
            @Query("filiere") String filiere
    );

    /* ==============================
       MES ÉTUDIANTS
       ============================== */
    @GET("api/encadrant/mes-etudiants")
    Call<List<EtudiantProjetDTO>> getMesEtudiants(
            @Query("encadrantId") Integer encadrantId
    );

    /* ==============================
       ACTION ENCADRER (BOUTON)
       ============================== */
    @POST("api/encadrant/encadrer")
    Call<Void> encadrer(
            @Body EncadrerRequest body
    );
}

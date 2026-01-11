package com.example.frontend.api;

import com.example.frontend.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface AdminApi {



    @GET("admin/students")
    Call<List<StudentAdmin>> getStudents();

    @GET("admin/encadrants")
    Call<List<EncadrantAdmin>> getEncadrants();

    @GET("admin/professeurs")
    Call<List<Professeur>> getProfesseurs();

    @POST("admin/affecter")
    Call<Void> affecter(@Body AffectationRequest request);

    @PUT("admin/reaffecter")
    Call<Void> reaffecter(@Body AffectationRequest request);

    @DELETE("admin/desaffecter/{id}")
    Call<Void> desaffecter(@Path("id") Long etudiantId);

    @POST("admin/encadrants/{encadrantId}/addStudents")
    Call<Void> addStudentsToEncadrant(@Path("encadrantId") Long encadrantId, @Body List<String> numAppogeeList);

    @POST("admin/encadrants/{encadrantId}/removeStudents")
    Call<Void> removeStudentsFromEncadrant(@Path("encadrantId") Long encadrantId, @Body List<String> numAppogeeList);

    @GET("/api/admin/soutenances/projets-eligibles")
    Call<List<EtudiantProjetDTO>> getProjetsEligibles();

    @GET("/api/admin/soutenances/salles-disponibles")
    Call<List<Salle>> getSallesDisponibles(
            @Query("debut") String debut,
            @Query("fin") String fin);

    @POST("/api/admin/soutenances")
    Call<Void> programmerSoutenance(@Body SoutenanceDTO dto);

    // Récupérer la liste des salles
    @GET("api/admin/salles")
    Call<List<Salle>> getSalles();

    // Programmer une soutenance


    // -------------------- SINGLETON RETROFIT --------------------
    class Factory {
        private static AdminApi instance;

        public static AdminApi getInstance() {
            if (instance == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.16:9090/") // <-- adapte à ton backend
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                instance = retrofit.create(AdminApi.class);
            }
            return instance;
        }
    }
}

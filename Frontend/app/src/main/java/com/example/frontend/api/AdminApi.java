package com.example.frontend.api;

import com.example.frontend.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
import com.example.frontend.model.AffectationRequest;

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

}

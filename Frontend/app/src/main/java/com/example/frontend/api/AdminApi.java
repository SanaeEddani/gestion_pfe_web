package com.example.frontend.api;

import com.example.frontend.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface AdminApi {

    @GET("admin/students")
    Call<List<StudentAdmin>> getStudents();

    @GET("admin/encadrants")
    Call<List<EncadrantAdmin>> getEncadrants();

    @POST("admin/affecter")
    Call<Void> affecter(@Body AffectationRequest request);

    @PUT("admin/reaffecter")
    Call<Void> reaffecter(@Body AffectationRequest request);

    @DELETE("admin/desaffecter/{id}")
    Call<Void> desaffecter(@Path("id") Long etudiantId);
}

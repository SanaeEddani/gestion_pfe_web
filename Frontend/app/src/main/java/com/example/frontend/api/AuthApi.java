package com.example.frontend.api;

import com.example.frontend.model.Appogee;
import com.example.frontend.model.CodeProf;
import com.example.frontend.model.EtudiantProfile;
import com.example.frontend.model.JwtResponse;
import com.example.frontend.model.LoginRequest;
import com.example.frontend.model.ProjetDTO;
import com.example.frontend.model.UserRequest;
import com.example.frontend.model.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
    @POST("api/auth/login")
    Call<JwtResponse> login(@Body LoginRequest request);

    @POST("auth/password/send-otp")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(@Field("email") String email);

    @POST("auth/password/verify-otp")
    @FormUrlEncoded
    Call<Boolean> verifyOtp(@Field("email") String email, @Field("otp") String otp);

    @POST("auth/password/reset-password")
    Call<ResponseBody> resetPassword(
            @Query("email") String email,
            @Query("otp") String otp,
            @Query("newPassword") String newPassword
    );

    @POST("/api/auth/register")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    @GET("/api/appogees")
    Call<List<Appogee>> getAppogees();

    @GET("/api/codeprofs")
    Call<List<CodeProf>> getCodeProfs();

    @GET("api/etudiant/profile")
    Call<EtudiantProfile> getEtudiantProfile(@Header("Authorization") String authHeader);

    @POST("api/projets")
    @FormUrlEncoded
    Call<ResponseBody> ajouterProjet(
            @Field("sujet") String sujet,
            @Field("description") String description,
            @Field("entreprise") String entreprise
    );

    @POST("api/projets")
    Call<ResponseBody> ajouterProjet(@Body ProjetDTO projet);

    @GET("/api/projets/me")
    Call<List<ProjetDTO>> getMesProjets(@Header("Authorization") String authHeader);

    // 🔹 Nouveau endpoint pour l'upload des documents
    @Multipart
    @POST("api/documents/upload")
    Call<ResponseBody> uploadDocument(
            @Part MultipartBody.Part file,
            @Part("etudiantId") RequestBody etudiantId
    );


}

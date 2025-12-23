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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("api/auth/login")  // endpoint Spring Boot
    Call<JwtResponse> login(@Body LoginRequest request);

    @POST("auth/password/send-otp")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(@Field("email") String email);


    // Vérification OTP


    @POST("auth/password/verify-otp")
    @FormUrlEncoded
    Call<Boolean> verifyOtp(@Field("email") String email, @Field("otp") String otp);

    // Réinitialisation mot de passe
    @POST("auth/password/reset-password")
    Call<ResponseBody> resetPassword(
            @Query("email") String email,
            @Query("otp") String otp,
            @Query("newPassword") String newPassword
    );


    @POST("/api/auth/register")
     Call<UserResponse> registerUser(@Body UserRequest userRequest) ;



    // Liste des codes Apogée (si jamais tu veux récupérer depuis backend)
    @GET("/api/appogees")
    Call<List<Appogee>> getAppogees();

    // Liste des codes Prof
    @GET("/api/codeprofs")
    Call<List<CodeProf>> getCodeProfs();

    @GET("api/etudiant/profile")
    Call<EtudiantProfile> getEtudiantProfile();

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

}


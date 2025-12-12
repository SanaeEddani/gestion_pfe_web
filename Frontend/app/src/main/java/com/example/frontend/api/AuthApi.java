package com.example.frontend.api;

import com.example.frontend.model.JwtResponse;
import com.example.frontend.model.LoginRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("api/auth/login")  // endpoint Spring Boot
    Call<JwtResponse> login(@Body LoginRequest request);

    // Demande d'OTP
    @POST("auth/password/forgot")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(@Field("email") String email);

    // Vérification OTP
    @POST("auth/password/verify-otp")
    @FormUrlEncoded
    Call<Boolean> verifyOtp(@Field("email") String email, @Field("otp") String otp);

    // Réinitialisation mot de passe
    @POST("auth/password/reset")
    @FormUrlEncoded
    Call<Void> resetPassword(@Field("email") String email, @Field("newPassword") String newPassword);

}


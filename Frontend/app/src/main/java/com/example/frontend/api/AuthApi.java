package com.example.frontend.api;

import com.example.frontend.model.JwtResponse;
import com.example.frontend.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("api/auth/login")  // endpoint Spring Boot
    Call<JwtResponse> login(@Body LoginRequest request);
}


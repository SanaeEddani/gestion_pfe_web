package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    private AuthApi authApi;

    // Configuration du timeout (en secondes)
    private static final long CONNECT_TIMEOUT = 30;
    private static final long READ_TIMEOUT = 30;
    private static final long WRITE_TIMEOUT = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailForgot = findViewById(R.id.emailForgot);
        Button btnSendReset = findViewById(R.id.btnSendReset);

        // 1. Créer l'OkHttpClient avec les timeouts configurés
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // 2. Créer l'instance Retrofit avec l'OkHttpClient configuré
        // Remplacez BASE_URL par votre URL de base réelle
        String BASE_URL = "http://172.17.206.246:9090/"; // À modifier selon votre configuration

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // Utilisation du client OkHttp avec timeouts
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 3. Créer l'instance de l'API
        authApi = retrofit.create(AuthApi.class);

        btnSendReset.setOnClickListener(v -> {
            String email = emailForgot.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Appel backend avec les timeouts configurés
            authApi.forgotPassword(email).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "OTP envoyé à votre email avec Succès", Toast.LENGTH_SHORT).show();

                        // Aller à la page d'entrée OTP
                        Intent i = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                        i.putExtra("email", email);
                        startActivity(i);

                    } else {
                        // Diagnostic détaillé
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            errorBody = "Impossible de lire le corps de l'erreur: " + e.getMessage();
                        }

                        showPopup(
                                "Erreur côté serveur",
                                "\nMessage: " + response.message()
                        );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String message = t.getMessage();
                    if (t.getCause() != null) {
                        message += "\nCause: " + t.getCause().getMessage();
                    }
                    showPopup("Erreur réseau", "Impossible de contacter le backend : " + message);
                }
            });
        });
    }

    private void showPopup(String title, String message) {
        new AlertDialog.Builder(ForgotPasswordActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
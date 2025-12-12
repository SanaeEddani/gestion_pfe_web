package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailForgot = findViewById(R.id.emailForgot);
        Button btnSendReset = findViewById(R.id.btnSendReset);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.16:9090/") // URL de ton backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        btnSendReset.setOnClickListener(v -> {
            String email = emailForgot.getText().toString().trim();
            if (email.isEmpty()) {
                showPopup("Erreur", "Veuillez entrer votre email");
                return;
            }

            // Appel backend
            authApi.forgotPassword(email).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showPopup("Succès", "OTP envoyé à votre email");

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
                                "Code HTTP: " + response.code() +
                                        "\nMessage: " + response.message() +
                                        "\nDétails: " + errorBody
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

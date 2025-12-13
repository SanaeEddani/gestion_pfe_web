package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailForgot = findViewById(R.id.emailForgot);
        Button btnSendReset = findViewById(R.id.btnSendReset);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        AuthApi authApi = RetrofitClient
                .getRetrofitInstance()
                .create(AuthApi.class);

        btnSendReset.setOnClickListener(v -> {
            String email = emailForgot.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Veuillez entrer votre email",Toast.LENGTH_SHORT).show();

                return;
            }

            // Appel backend
            authApi.forgotPassword(email).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(ForgotPasswordActivity.this, "OTP envoyé à votre email avec Succès ",Toast.LENGTH_SHORT).show();

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

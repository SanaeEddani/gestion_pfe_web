// ResetPasswordActivity.java
package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordActivity extends AppCompatActivity {

    private AuthApi authApi;
    private String email;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Récupérer email et OTP depuis l'activité précédente
        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");

        if (email == null || otp == null) {
            Toast.makeText(this, "Données manquantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EditText newPasswordInput = findViewById(R.id.newPassword);
        EditText confirmPasswordInput = findViewById(R.id.confirmPassword); // Ajoutez ce champ dans votre layout
        Button btnReset = findViewById(R.id.btnResetPassword);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.16:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        btnReset.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un nouveau mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Appel au bon endpoint avec les bons paramètres
            authApi.resetPassword(email, otp, newPassword).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String message = response.body() != null ?
                                    response.body().string() : "Mot de passe réinitialisé";
                            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                            // Retour à l'écran de login
                            Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(ResetPasswordActivity.this, "Réinitialisation réussie", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Erreur: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
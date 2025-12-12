package com.example.frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordActivity extends AppCompatActivity {

    private AuthApi authApi;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText newPasswordInput = findViewById(R.id.newPassword);
        Button btnReset = findViewById(R.id.btnResetPassword);

        // Récupération du token depuis le deep link
        if (getIntent().getData() != null) {
            token = getIntent().getData().getQueryParameter("token");
            Toast.makeText(this, "Token reçu: " + token, Toast.LENGTH_LONG).show();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.16:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        btnReset.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            if (newPassword.isEmpty() || token == null) {
                Toast.makeText(this, "Veuillez entrer un mot de passe et vérifier le token", Toast.LENGTH_SHORT).show();
                return;
            }

            authApi.resetPassword(token, newPassword).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Mot de passe réinitialisé avec succès", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Erreur côté serveur", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ResetPasswordActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}

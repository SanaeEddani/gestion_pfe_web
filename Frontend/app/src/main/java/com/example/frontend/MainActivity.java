package com.example.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.JwtResponse;
import com.example.frontend.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class))
        );

        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class))
        );

        // 🔹 API Auth
        AuthApi authApi = RetrofitClient
                .getRetrofitInstance()
                .create(AuthApi.class);

        btnLogin.setOnClickListener(v -> {

            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                showPopup("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            LoginRequest request = new LoginRequest(userEmail, userPassword);

            authApi.login(request).enqueue(new Callback<JwtResponse>() {

                @Override
                public void onResponse(
                        Call<JwtResponse> call,
                        Response<JwtResponse> response
                ) {

                    if (response.isSuccessful() && response.body() != null) {

                        JwtResponse jwt = response.body();

                        int userId = jwt.getId();          // ✅ ID utilisateur
                        int role = jwt.getRoleInt();       // ✅ rôle
                        String token = jwt.getToken();     // ✅ JWT

                        // ✅ SAUVEGARDE DANS SharedPreferences
                        SharedPreferences sp =
                                getSharedPreferences("auth", MODE_PRIVATE);

                        sp.edit()
                                .putInt("user_id", userId)
                                .putInt("role", role)
                                .putString("token", token)
                                .apply();

                        // 🔁 Redirection selon rôle
                        switch (role) {
                            case 1:
                                startActivity(new Intent(
                                        MainActivity.this,
                                        AdminActivity.class
                                ));
                                break;

                            case 2:
                                startActivity(new Intent(
                                        MainActivity.this,
                                        StudentActivity.class
                                ));
                                break;

                            case 3:
                                startActivity(new Intent(
                                        MainActivity.this,
                                        EncadrantActivity.class
                                ));
                                break;

                            default:
                                Toast.makeText(
                                        MainActivity.this,
                                        "Rôle inconnu",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                        }

                        finish(); // fermer écran login

                    } else {
                        Toast.makeText(
                                MainActivity.this,
                                "Email ou mot de passe incorrect",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<JwtResponse> call,
                        Throwable t
                ) {
                    showPopup("Erreur réseau", t.getMessage());
                }
            });
        });
    }

    private void showPopup(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}

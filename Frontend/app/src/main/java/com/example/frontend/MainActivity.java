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

    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        /* =========================
           Navigation
           ========================= */
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class))
        );

        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class))
        );

        /* =========================
           Initialisation Retrofit
           ========================= */
        authApi = RetrofitClient
                .getRetrofitInstance(MainActivity.this)
                .create(AuthApi.class);

        /* =========================
           Login
           ========================= */
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

                        String token = jwt.getToken();
                        String roleString = jwt.getRole();
                        int role = jwt.getRoleInt();

                        // ✅ ID utilisateur (compatible id / userId)
                        int userId = jwt.getId();
                        if (userId <= 0) {
                            userId = jwt.getUserId();
                        }
                        if (userId <= 0) {
                            userId = getUserIdFromEmail(userEmail);
                        }

                        /* =========================
                           Sauvegarde session
                           ========================= */
                        SharedPreferences.Editor editor =
                                getSharedPreferences("user_session", MODE_PRIVATE).edit();

                        editor.putString("token", token);
                        editor.putString("email", userEmail);
                        editor.putString("role", roleString);
                        editor.putInt("role_int", role);
                        editor.putInt("user_id", userId);
                        editor.apply();

                        Toast.makeText(
                                MainActivity.this,
                                "Connexion réussie !",
                                Toast.LENGTH_SHORT
                        ).show();

                        /* =========================
                           Redirection selon rôle
                           ========================= */
                        switch (role) {
                            case 1: // Admin
                                startActivity(new Intent(
                                        MainActivity.this,
                                        AdminActivity.class
                                ));
                                break;

                            case 2: // Étudiant
                                Intent studentIntent = new Intent(
                                        MainActivity.this,
                                        EtudiantProfileActivity.class
                                );
                                studentIntent.putExtra("STUDENT_ID", userId);
                                startActivity(studentIntent);
                                break;

                            case 3: // Encadrant / Cadre
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

    /* =========================
       Méthodes utilitaires
       ========================= */

    // ⚠️ TEMPORAIRE – à remplacer par une vraie API
    private int getUserIdFromEmail(String email) {
        SharedPreferences prefs =
                getSharedPreferences("user_session", MODE_PRIVATE);
        return prefs.getInt("user_id", 1); // valeur par défaut test
    }

    private void showPopup(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /* =========================
       OPTIONNEL : auto-login
       ========================= */
    /*
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getSharedPreferences("user_session", MODE_PRIVATE);

        if (prefs.contains("token") && prefs.contains("user_id")) {

            int userId = prefs.getInt("user_id", -1);
            int role = prefs.getInt("role_int", -1);

            if (userId > 0 && role > 0) {
                if (role == 1) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else if (role == 2) {
                    Intent i = new Intent(this, EtudiantProfileActivity.class);
                    i.putExtra("STUDENT_ID", userId);
                    startActivity(i);
                } else if (role == 3) {
                    startActivity(new Intent(this, EncadrantActivity.class));
                }
                finish();
            }
        }
    }
    */
}

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

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Initialiser Retrofit
        AuthApi authApi = RetrofitClient
                .getRetrofitInstance(MainActivity.this)
                .create(AuthApi.class);

        btnLogin.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                showPopup("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            // Créer l'objet LoginRequest
            LoginRequest request = new LoginRequest(userEmail, userPassword);

            authApi.login(request).enqueue(new Callback<JwtResponse>() {
                @Override
                public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JwtResponse jwtResponse = response.body();
                        String token = jwtResponse.getToken();
                        String roleString = jwtResponse.getRole();
                        int role = jwtResponse.getRoleInt();

                        // IMPORTANT : Récupérer l'ID utilisateur
                        // Si votre API ne retourne pas l'ID dans JwtResponse,
                        // vous devez faire un appel API supplémentaire pour obtenir l'ID
                        int userId = jwtResponse.getUserId();

                        if (userId <= 0) {
                            // Si l'ID n'est pas dans la réponse, essayez de le récupérer
                            // Vous devrez peut-être créer une API pour obtenir l'ID par email
                            // Pour l'instant, on utilise une valeur par défaut
                            userId = getUserIdFromEmail(userEmail); // Méthode à implémenter
                        }

                        // Stocker les informations utilisateur
                        SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
                        editor.putString("token", token);
                        editor.putString("email", userEmail);
                        editor.putString("role", roleString);
                        editor.putInt("role_int", role);
                        editor.putInt("user_id", userId); // STOCKER L'ID
                        editor.apply();

                        Toast.makeText(MainActivity.this, "Connexion réussie!", Toast.LENGTH_SHORT).show();

                        // Redirection selon rôle
                        switch (role) {
                            case 1: // Admin
                                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                break;
                            case 2: // Étudiant
                                Intent studentIntent = new Intent(MainActivity.this, EtudiantProfileActivity.class);
                                studentIntent.putExtra("STUDENT_ID", userId);
                                startActivity(studentIntent);
                                break;
                            case 3: // Encadrant/Cadre
                                startActivity(new Intent(MainActivity.this, CadreActivity.class));
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Rôle inconnu", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        finish(); // fermer MainActivity
                    } else {
                        Toast.makeText(MainActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JwtResponse> call, Throwable t) {
                    showPopup("Erreur réseau", t.getMessage());
                }
            });
        });
    }

    // Méthode temporaire pour obtenir l'ID - À REMPLACER par un appel API réel
    private int getUserIdFromEmail(String email) {
        // Ceci est une solution temporaire
        // Dans un cas réel, vous devriez avoir une API qui retourne l'ID utilisateur par email
        // Par exemple : apiService.getUserIdByEmail(email)

        // Pour le moment, retournez une valeur par défaut
        // ou utilisez SharedPreferences si vous avez déjà enregistré l'ID
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        return prefs.getInt("user_id", 1); // Valeur par défaut 1 pour les tests
    }

    private void showPopup(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // OPTIONNEL : Si vous voulez garder la redirection automatique
    // Commentez cette méthode si vous voulez toujours voir l'écran de login
    /*
    @Override
    protected void onStart() {
        super.onStart();
        checkIfAlreadyLoggedIn();
    }

    private void checkIfAlreadyLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        if (prefs.contains("token") && prefs.contains("user_id")) {
            int userId = prefs.getInt("user_id", -1);
            int role = prefs.getInt("role_int", -1);
            String userEmail = prefs.getString("email", "");

            if (userId > 0 && role != -1 && !userEmail.isEmpty()) {
                if (role == 2) { // Étudiant
                    Intent intent = new Intent(MainActivity.this, StudentDashboardActivity.class);
                    intent.putExtra("STUDENT_ID", userId);
                    startActivity(intent);
                    finish();
                } else if (role == 1) { // Admin
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    finish();
                } else if (role == 3) { // Cadre
                    startActivity(new Intent(MainActivity.this, CadreActivity.class));
                    finish();
                }
            }
        }
    }
    */
}
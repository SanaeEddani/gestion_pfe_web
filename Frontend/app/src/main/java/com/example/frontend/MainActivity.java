package com.example.frontend;




import android.content.Intent;
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
import retrofit2.Retrofit;




public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;


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
                .getRetrofitInstance()
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

            authApi.login(request).enqueue(new Callback<JwtResponse>()  {
                @Override
                public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int role = response.body().getRoleInt();
                        String token = response.body().getToken();


                        // Stocker token si besoin
                        // SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
                        // prefs.edit().putString("token", token).apply();

                        // Redirection selon rôle
                        switch (role) {
                            case 1:
                                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, StudentActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this, CadreActivity.class));
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Rôle inconnu",Toast.LENGTH_SHORT).show();
                                break;
                        }

                        finish(); // fermer LoginActivity
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

    private void showPopup(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }







}

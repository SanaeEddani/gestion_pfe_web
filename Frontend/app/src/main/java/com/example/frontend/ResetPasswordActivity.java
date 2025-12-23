// ResetPasswordActivity.java
package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClientAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private AuthApi authApi;
    private String email;
    private String otp;
    private TextView ruleLength, ruleLowercase, ruleUppercase, ruleDigit, ruleSpecial;
    private EditText newPasswordInput, confirmPasswordInput;
    private Button btnReset;

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

        // Initialiser les vues
        newPasswordInput = findViewById(R.id.newPassword);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        btnReset = findViewById(R.id.btnResetPassword);

        ruleLength = findViewById(R.id.ruleLength);
        ruleLowercase = findViewById(R.id.ruleLowercase);
        ruleUppercase = findViewById(R.id.ruleUppercase);
        ruleDigit = findViewById(R.id.ruleDigit);
        ruleSpecial = findViewById(R.id.ruleSpecial);

        // Initialiser Retrofit
        authApi = RetrofitClientAuth.getInstance().create(AuthApi.class);

        // Ajouter le TextWatcher pour valider les règles en temps réel
        newPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordRules(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnReset.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // Vérifier si tous les critères sont satisfaits
            if (!isPasswordValid(newPassword)) {
                Toast.makeText(this, "Le mot de passe ne respecte pas toutes les règles", Toast.LENGTH_SHORT).show();
                return;
            }

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

    private void updatePasswordRules(String password) {
        toggleRule(ruleLength, password.length() >= 8);
        toggleRule(ruleLowercase, password.matches(".*[a-z].*"));
        toggleRule(ruleUppercase, password.matches(".*[A-Z].*"));
        toggleRule(ruleDigit, password.matches(".*\\d.*"));
        toggleRule(ruleSpecial, password.matches(".*[@#$%!].*"));
    }

    private void toggleRule(TextView ruleView, boolean isValid) {
        if (isValid) {
            ruleView.setTextColor(ContextCompat.getColor(this, R.color.successGreen));
            // Si vous n'avez pas de couleur successGreen, utilisez:
            // ruleView.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            ruleView.setTextColor(ContextCompat.getColor(this, R.color.errorRed));
            // Si vous n'avez pas de couleur errorRed, utilisez:
            // ruleView.setTextColor(Color.parseColor("#F44336"));
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%!].*");
    }
}
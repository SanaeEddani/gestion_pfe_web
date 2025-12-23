package com.example.frontend;



import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.UserRequest;
import com.example.frontend.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    RadioGroup roleGroup;
    RadioButton studentBtn, teacherBtn;
    LinearLayout studentSection, teacherSection;

    Spinner filiereSpinner, departementSpinner;
    EditText emailEditText, passwordEditText, apogeeEditText,
            codeProfEditText, nomEditText, prenomEditText;

    Button registerButton;
    TextView loginLink;

    // Règles mot de passe
    TextView ruleLength, ruleLowercase, ruleUppercase, ruleDigit, ruleSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Bind views
        roleGroup = findViewById(R.id.roleRadioGroup);
        studentBtn = findViewById(R.id.studentRadioButton);
        teacherBtn = findViewById(R.id.teacherRadioButton);

        studentSection = findViewById(R.id.studentSection);
        teacherSection = findViewById(R.id.teacherSection);

        filiereSpinner = findViewById(R.id.filiereSpinner);
        departementSpinner = findViewById(R.id.departementSpinner);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        apogeeEditText = findViewById(R.id.apogeeEditText);
        codeProfEditText = findViewById(R.id.codeProfEditText);
        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);

        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Password rules
        ruleLength = findViewById(R.id.ruleLength);
        ruleLowercase = findViewById(R.id.ruleLowercase);
        ruleUppercase = findViewById(R.id.ruleUppercase);
        ruleDigit = findViewById(R.id.ruleDigit);
        ruleSpecial = findViewById(R.id.ruleSpecial);

        // Spinners
        filiereSpinner.setAdapter(new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Choisir --", "GI", "GE", "TM", "GC", "RT"}));

        departementSpinner.setAdapter(new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Choisir --", "Informatique", "Maths", "Physique", "Management"}));

        // Changement de rôle
        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.studentRadioButton) {
                studentSection.setVisibility(View.VISIBLE);
                teacherSection.setVisibility(View.GONE);
            } else if (checkedId == R.id.teacherRadioButton) {
                studentSection.setVisibility(View.GONE);
                teacherSection.setVisibility(View.VISIBLE);
            }
        });

        // Validation mot de passe en temps réel
        passwordEditText.addTextChangedListener(passwordWatcher);

        // Inscription
        registerButton.setOnClickListener(v -> {
            if (validateFields()) {
                registerUser();
            }
        });

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }


    /* =======================
       PASSWORD VALIDATION
       ======================= */
    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            updatePasswordRules(s.toString());
        }
        @Override public void afterTextChanged(Editable s) {}
    };

    private void updatePasswordRules(String password) {
        toggleRule(ruleLength, password.length() >= 8);
        toggleRule(ruleLowercase, password.matches(".*[a-z].*"));
        toggleRule(ruleUppercase, password.matches(".*[A-Z].*"));
        toggleRule(ruleDigit, password.matches(".*\\d.*"));
        toggleRule(ruleSpecial, password.matches(".*[@#$%!].*"));
    }

    private void toggleRule(TextView rule, boolean valid) {
        rule.setVisibility(valid ? View.GONE : View.VISIBLE);
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%!].*");
    }

    /* =======================
       FORM VALIDATION
       ======================= */
    private boolean validateFields() {

        if (TextUtils.isEmpty(nomEditText.getText())) {
            nomEditText.setError("Nom obligatoire");
            return false;
        }

        if (TextUtils.isEmpty(prenomEditText.getText())) {
            prenomEditText.setError("Prénom obligatoire");
            return false;
        }

        String email = emailEditText.getText().toString().trim().toLowerCase();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email invalide");
            return false;
        }

        String expectedEmail = prenomEditText.getText().toString().trim().toLowerCase()
                + "." +
                nomEditText.getText().toString().trim().toLowerCase()
                + "@uit.ac.ma";

        if (!email.equals(expectedEmail)) {
            emailEditText.setError("Email doit être prenom.nom@uit.ac.ma");
            return false;
        }

        String password = passwordEditText.getText().toString();
        if (!isPasswordValid(password)) {
            Toast.makeText(this, "Mot de passe non conforme", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (studentBtn.isChecked()) {
            if (TextUtils.isEmpty(apogeeEditText.getText())) {
                apogeeEditText.setError("Code Apogée obligatoire");
                return false;
            }
            if (filiereSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Choisir une filière", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (teacherBtn.isChecked()) {
            if (departementSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Choisir un département", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(codeProfEditText.getText())) {
                codeProfEditText.setError("Code Prof obligatoire");
                return false;
            }
        }

        return true;
    }

    // Initialiser Retrofit
    AuthApi authApi = RetrofitClient
            .getRetrofitInstance(SignupActivity.this)
            .create(AuthApi.class);


    /* =======================
       API CALL
       ======================= */
    private void registerUser() {

        UserRequest request = new UserRequest();
        request.setNom(nomEditText.getText().toString().trim());
        request.setPrenom(prenomEditText.getText().toString().trim());
        request.setEmail(emailEditText.getText().toString().trim());
        request.setPassword(passwordEditText.getText().toString());

        if (studentBtn.isChecked()) {
            request.setRole("etudiant");
            request.setAppogee(apogeeEditText.getText().toString().trim());
            request.setFiliere(filiereSpinner.getSelectedItem().toString());
        } else {
            request.setRole("encadrant");
            request.setCodeProf(codeProfEditText.getText().toString().trim());
            request.setDepartement(departementSpinner.getSelectedItem().toString());
        }

        registerButton.setEnabled(false);
        registerButton.setText("Inscription...");



        authApi.registerUser(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                registerButton.setEnabled(true);
                registerButton.setText("S’inscrire");

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignupActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_LONG).show();
                    if (response.body().isSuccess()) finish();
                } else {
                    Toast.makeText(SignupActivity.this,
                            "Erreur serveur",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                registerButton.setEnabled(true);
                registerButton.setText("S’inscrire");
                Toast.makeText(SignupActivity.this,
                        "Erreur réseau",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

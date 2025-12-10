package edu.uit.pfeapp.auth;
import com.example.frontend.R;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {

    RadioGroup roleGroup;
    RadioButton studentBtn, teacherBtn;
    LinearLayout studentSection, teacherSection;
    Spinner filiereSpinner, departementSpinner;
    EditText emailEditText, passwordEditText, apogeeEditText, codeProfEditText;
    Button registerButton;

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
        registerButton = findViewById(R.id.registerButton);

        // Fake data pour les spinners (backend à connecter ensuite)
        ArrayAdapter<String> filieres = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Choisir --", "GI", "GE", "TM", "GC", "RT"});
        filiereSpinner.setAdapter(filieres);

        ArrayAdapter<String> departements = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Choisir --", "Informatique", "Maths", "Physique", "Management"});
        departementSpinner.setAdapter(departements);

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

        // Validation et inscription
        registerButton.setOnClickListener(v -> {
            if (validateFields()) {
                Toast.makeText(this, "Formulaire valide, envoyer au backend", Toast.LENGTH_SHORT).show();
                // TODO: appeler backend via Retrofit
            }
        });
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(emailEditText.getText()) || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
            emailEditText.setError("Email invalide");
            return false;
        }

        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setError("Mot de passe obligatoire");
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
}

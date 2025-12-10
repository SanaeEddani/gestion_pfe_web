package edu.uit.pfeapp.auth;

import android.os.Bundle;
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

import com.example.frontend.R;

public class SignupActivity extends AppCompatActivity {

    RadioGroup roleGroup;
    RadioButton studentBtn, teacherBtn;
    LinearLayout studentSection, teacherSection;
    Spinner filiereSpinner, departementSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        roleGroup = findViewById(R.id.roleRadioGroup);
        studentBtn = findViewById(R.id.studentRadioButton);
        teacherBtn = findViewById(R.id.teacherRadioButton);

        studentSection = findViewById(R.id.studentSection);
        teacherSection = findViewById(R.id.teacherSection);

        filiereSpinner = findViewById(R.id.filiereSpinner);
        departementSpinner = findViewById(R.id.departementSpinner);

        // Fake data (backend later)
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
    }
}

package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Rediriger directement vers StudentDashboardActivity
        Intent intent = new Intent(this, EtudiantProfileActivity.class);

        // Récupérer l'ID de l'étudiant depuis SharedPreferences ou l'intent
        int studentId = getSharedPreferences("user_session", MODE_PRIVATE)
                .getInt("user_id", 1);

        intent.putExtra("STUDENT_ID", studentId);
        startActivity(intent);
        finish(); // Fermer StudentActivity
    }
}
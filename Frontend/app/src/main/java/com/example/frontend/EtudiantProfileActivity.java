package com.example.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EtudiantProfile;
import com.example.frontend.model.ProjetDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtudiantProfileActivity extends AppCompatActivity {

    private static final String TAG = "EtudiantProfileActivity";

    private TextView tvNom, tvEmail, tvFiliere, tvAppogee;
    private TextView tvProjetSujet, tvProjetDescription, tvProjetEntreprise;
    private Button btnAjouterProjet;
    private LinearLayout projetContainer;
    private FloatingActionButton clickButton;

    private int studentId;
    private String token;

    private ActivityResultLauncher<Intent> ajouterProjetLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_profile);

        /* =======================
           1️⃣ STUDENT_ID depuis Intent
           ======================= */
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);
        if (studentId == -1) {
            Toast.makeText(this, "ID étudiant introuvable", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        /* =======================
           2️⃣ TOKEN
           ======================= */
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Session expirée", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        /* =======================
           3️⃣ Initialisation vues
           ======================= */
        tvNom = findViewById(R.id.tvNom);
        tvEmail = findViewById(R.id.tvEmail);
        tvFiliere = findViewById(R.id.tvFiliere);
        tvAppogee = findViewById(R.id.tvAppogee);

        tvProjetSujet = findViewById(R.id.tvProjetSujet);
        tvProjetDescription = findViewById(R.id.tvProjetDescription);
        tvProjetEntreprise = findViewById(R.id.tvProjetEntreprise);

        btnAjouterProjet = findViewById(R.id.btnAjouterProjet);
        projetContainer = findViewById(R.id.projetContainer);
        clickButton = findViewById(R.id.fab); // 🔴 FAB

        projetContainer.setVisibility(View.GONE);

        /* =======================
           4️⃣ Launcher ajout projet
           ======================= */
        ajouterProjetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        tvProjetSujet.setText("Sujet : " + data.getStringExtra("sujet"));
                        tvProjetDescription.setText("Description : " + data.getStringExtra("description"));
                        tvProjetEntreprise.setText("Entreprise : " + data.getStringExtra("entreprise"));

                        projetContainer.setVisibility(View.VISIBLE);
                    }
                }
        );

        /* =======================
           5️⃣ Charger données
           ======================= */
        loadEtudiantProfile();
        loadProjets();

        /* =======================
           6️⃣ Ajouter projet
           ======================= */
        btnAjouterProjet.setOnClickListener(v -> {
            Intent intent = new Intent(this, AjouterProjetActivity.class);
            intent.putExtra("STUDENT_ID", studentId);
            ajouterProjetLauncher.launch(intent);
        });

        /* =======================
           7️⃣ 🔥 CLICK BUTTON → AUTRE ACTIVITY
           ======================= */
        clickButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    EtudiantProfileActivity.this,
                    DocumentsActivity.class
            );
            intent.putExtra("STUDENT_ID", (long) studentId);

            startActivity(intent);
        });
    }

    /* =======================
       API Profil
       ======================= */
    private void loadEtudiantProfile() {
        AuthApi api = RetrofitClient.getRetrofitInstance(this).create(AuthApi.class);

        api.getEtudiantProfile("Bearer " + token).enqueue(new Callback<EtudiantProfile>() {
            @Override
            public void onResponse(Call<EtudiantProfile> call, Response<EtudiantProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EtudiantProfile e = response.body();

                    tvNom.setText("Nom complet : " + e.getPrenom() + " " + e.getNom());
                    tvEmail.setText("Email : " + e.getEmail());
                    tvFiliere.setText("Filière : " + e.getFiliere());
                    tvAppogee.setText("Numéro Appogée : " + e.getNumAppogee());
                }
            }

            @Override
            public void onFailure(Call<EtudiantProfile> call, Throwable t) {
                Log.e(TAG, "Erreur profil", t);
            }
        });
    }

    /* =======================
       API Projets
       ======================= */
    private void loadProjets() {
        AuthApi api = RetrofitClient.getRetrofitInstance(this).create(AuthApi.class);

        api.getMesProjets("Bearer " + token).enqueue(new Callback<List<ProjetDTO>>() {
            @Override
            public void onResponse(Call<List<ProjetDTO>> call, Response<List<ProjetDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ProjetDTO p = response.body().get(0);

                    tvProjetSujet.setText("Sujet : " + p.getSujet());
                    tvProjetDescription.setText("Description : " + p.getDescription());
                    tvProjetEntreprise.setText("Entreprise : " + p.getEntreprise());

                    projetContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProjetDTO>> call, Throwable t) {
                Log.e(TAG, "Erreur projets", t);
            }
        });
    }
}

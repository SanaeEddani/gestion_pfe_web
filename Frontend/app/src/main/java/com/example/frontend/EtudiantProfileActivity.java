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

    private ActivityResultLauncher<Intent> ajouterProjetLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate appelé");
        setContentView(R.layout.activity_etudiant_profile);

        try {
            tvNom = findViewById(R.id.tvNom);
            tvEmail = findViewById(R.id.tvEmail);
            tvFiliere = findViewById(R.id.tvFiliere);
            tvAppogee = findViewById(R.id.tvAppogee);

            tvProjetSujet = findViewById(R.id.tvProjetSujet);
            tvProjetDescription = findViewById(R.id.tvProjetDescription);
            tvProjetEntreprise = findViewById(R.id.tvProjetEntreprise);

            btnAjouterProjet = findViewById(R.id.btnAjouterProjet);
            projetContainer = findViewById(R.id.projetContainer);
            projetContainer.setVisibility(View.GONE);

            Log.d(TAG, "Vues initialisées correctement");

        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'initialisation des vues", e);
        }

        ajouterProjetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "ActivityResult reçu, code: " + result.getResultCode());
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String sujet = data.getStringExtra("sujet");
                        String description = data.getStringExtra("description");
                        String entreprise = data.getStringExtra("entreprise");

                        projetContainer.setVisibility(View.VISIBLE);
                        tvProjetSujet.setText("Sujet : " + (sujet != null ? sujet : ""));
                        tvProjetDescription.setText("Description : " + (description != null ? description : ""));
                        tvProjetEntreprise.setText("Entreprise : " + (entreprise != null ? entreprise : ""));

                        Log.d(TAG, "Projet ajouté : " + sujet + " / " + description + " / " + entreprise);
                    } else {
                        Log.d(TAG, "Pas de données reçues ou résultat non OK");
                    }
                }
        );

        // Récupérer le profil étudiant
        try {
            AuthApi authApi = RetrofitClient.getRetrofitInstance(this).create(AuthApi.class);
            authApi.getEtudiantProfile().enqueue(new Callback<EtudiantProfile>() {
                @Override
                public void onResponse(Call<EtudiantProfile> call, Response<EtudiantProfile> response) {
                    Log.d(TAG, "onResponse appelé");
                    if (response.isSuccessful() && response.body() != null) {
                        EtudiantProfile e = response.body();
                        Log.d(TAG, "Profil reçu : " + new Gson().toJson(e));

                        try {
                            tvNom.setText("Nom complet : " +
                                    (e.getPrenom() != null ? e.getPrenom() : "") + " " +
                                    (e.getNom() != null ? e.getNom() : ""));
                            tvEmail.setText("Email : " + (e.getEmail() != null ? e.getEmail() : ""));
                            tvFiliere.setText("Filière : " + (e.getFiliere() != null ? e.getFiliere() : ""));
                            tvAppogee.setText("Numéro Appogée : " + (e.getNumAppogee() != null ? e.getNumAppogee() : ""));

                            // Charger les projets après récupération du profil
                            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String token = prefs.getString("token", "");
                            if (token != null && !token.isEmpty()) {
                                loadProjets(token);
                            }


                        } catch (Exception ex) {
                            Log.e(TAG, "Erreur lors de l'affichage du profil", ex);
                        }
                    } else {
                        Log.e(TAG, "Profil étudiant non trouvé, code: " + response.code());
                        Toast.makeText(EtudiantProfileActivity.this,
                                "Profil étudiant non trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EtudiantProfile> call, Throwable t) {
                    Log.e(TAG, "Erreur réseau", t);
                    Toast.makeText(EtudiantProfileActivity.this,
                            "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'appel API", e);
        }

        btnAjouterProjet.setOnClickListener(v -> {
            try {
                Log.d(TAG, "Clique sur Ajouter Projet");
                Intent intent = new Intent(EtudiantProfileActivity.this, AjouterProjetActivity.class);
                ajouterProjetLauncher.launch(intent);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du lancement de AjouterProjetActivity", e);
            }
        });
    }

    // Méthode pour charger les projets existants
    private void loadProjets(String token) {
        AuthApi authApi = RetrofitClient.getRetrofitInstance(this).create(AuthApi.class);
        Log.d(TAG, "Chargement des projets avec token : " + token);
        authApi.getMesProjets("Bearer " + token).enqueue(new Callback<List<ProjetDTO>>() {
            @Override
            public void onResponse(Call<List<ProjetDTO>> call, Response<List<ProjetDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    projetContainer.setVisibility(View.VISIBLE);
                    ProjetDTO p = response.body().get(0);
                    tvProjetSujet.setText("Sujet : " + p.getSujet());
                    tvProjetDescription.setText("Description : " + p.getDescription());
                    tvProjetEntreprise.setText("Entreprise : " + p.getEntreprise());
                    Log.d(TAG, "Projet affiché après login : " + p.getSujet());
                } else {
                    projetContainer.setVisibility(View.GONE);
                    Log.d(TAG, "Aucun projet trouvé après login");
                }
            }

            @Override
            public void onFailure(Call<List<ProjetDTO>> call, Throwable t) {
                Log.e(TAG, "Erreur réseau pour projets après login", t);
            }
        });


}


}

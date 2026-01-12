package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.ProjetDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjouterProjetActivity extends AppCompatActivity {

    EditText etSujet, etDescription, etEntreprise;
    Button btnSoumettre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_projet);

        etSujet = findViewById(R.id.etSujet);
        etDescription = findViewById(R.id.etDescription);
        etEntreprise = findViewById(R.id.etEntreprise);
        btnSoumettre = findViewById(R.id.btnSoumettre);

        AuthApi api = RetrofitClient.getRetrofitInstance(this).create(AuthApi.class);

        btnSoumettre.setOnClickListener(v -> {
            String sujet = etSujet.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String entreprise = etEntreprise.getText().toString().trim();

            if (sujet.isEmpty()) {
                etSujet.setError("Le sujet est obligatoire");
                etSujet.requestFocus();
                return;
            }

            ProjetDTO projet = new ProjetDTO(sujet, description, entreprise);

            api.ajouterProjet(projet).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AjouterProjetActivity.this,
                                "Projet ajouté avec succès !", Toast.LENGTH_SHORT).show();

                        // 🔹 Envoyer le projet à EtudiantProfileActivity
                        Intent intent = new Intent();
                        intent.putExtra("sujet", sujet);
                        intent.putExtra("description", description);
                        intent.putExtra("entreprise", entreprise);
                        setResult(RESULT_OK, intent);
                        finish(); // fermer l'activité
                    } else {
                        Toast.makeText(AjouterProjetActivity.this,
                                "Erreur lors de l'ajout du projet", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AjouterProjetActivity.this,
                            "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

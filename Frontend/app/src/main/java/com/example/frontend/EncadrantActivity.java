package com.example.frontend;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapter.EtudiantAdapter;
import com.example.frontend.api.EncadrantApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncadrantActivity extends AppCompatActivity {

    private EncadrantApi api;
    private final int ENCADRANT_ID = 1; // ⚠️ à remplacer par l'id réel connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encadrant);

        RecyclerView recyclerView = findViewById(R.id.recyclerEtudiants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        api = RetrofitClient.getRetrofitInstance().create(EncadrantApi.class);

        chargerEtudiants(recyclerView);
    }

    private void chargerEtudiants(RecyclerView recyclerView) {
        api.getEtudiants(null).enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(Call<List<EtudiantProjetDTO>> call,
                                   Response<List<EtudiantProjetDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<EtudiantProjetDTO> list = new ArrayList<>(response.body());

                    EtudiantAdapter adapter = new EtudiantAdapter(list, etudiant -> {
                        encadrerProjet(etudiant.getProjetId(), list, etudiant, recyclerView);
                    });

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                Toast.makeText(EncadrantActivity.this,
                        "Erreur chargement étudiants", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void encadrerProjet(int projetId,
                                List<EtudiantProjetDTO> list,
                                EtudiantProjetDTO etudiant,
                                RecyclerView recyclerView) {

        api.encadrer(projetId, ENCADRANT_ID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EncadrantActivity.this,
                            "Étudiant encadré", Toast.LENGTH_SHORT).show();

                    list.remove(etudiant);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(EncadrantActivity.this,
                        "Erreur encadrement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

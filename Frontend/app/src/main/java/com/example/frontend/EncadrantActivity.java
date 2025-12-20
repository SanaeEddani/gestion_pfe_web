package com.example.frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapter.EtudiantAdapter;
import com.example.frontend.api.EncadrantApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncadrantActivity extends AppCompatActivity {

    private static final int ENCADRANT_ID = 6;

    private EncadrantApi api;
    private EtudiantAdapter adapter;

    private final List<EtudiantProjetDTO> allEtudiants = new ArrayList<>();
    private final List<EtudiantProjetDTO> filteredEtudiants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encadrant);

        RecyclerView recyclerView = findViewById(R.id.recyclerEtudiants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner spinnerFiliere = findViewById(R.id.spinnerFiliere);

        adapter = new EtudiantAdapter(filteredEtudiants, etudiant -> {
            api.encadrer(etudiant.getProjetId(), ENCADRANT_ID)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            allEtudiants.remove(etudiant);
                            filteredEtudiants.remove(etudiant);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(EncadrantActivity.this,
                                    "Étudiant encadré", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(EncadrantActivity.this,
                                    "Erreur encadrement", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        recyclerView.setAdapter(adapter);

        api = RetrofitClient.getRetrofitInstance().create(EncadrantApi.class);
        chargerEtudiants(spinnerFiliere);
    }

    private void chargerEtudiants(Spinner spinnerFiliere) {
        api.getEtudiants(null).enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(Call<List<EtudiantProjetDTO>> call,
                                   Response<List<EtudiantProjetDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    allEtudiants.clear();
                    allEtudiants.addAll(response.body());

                    setupSpinner(spinnerFiliere);
                    appliquerFiltre("Toutes");
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                Toast.makeText(EncadrantActivity.this,
                        "Erreur chargement étudiants", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =============================
    // Spinner filière
    // =============================
    private void setupSpinner(Spinner spinner) {

        Set<String> filieresSet = new HashSet<>();
        for (EtudiantProjetDTO e : allEtudiants) {
            filieresSet.add(e.getFiliere());
        }

        List<String> filieres = new ArrayList<>();
        filieres.add("Toutes");
        filieres.addAll(filieresSet);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                filieres
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                appliquerFiltre(filieres.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // =============================
    // Filtrage par filière
    // =============================
    private void appliquerFiltre(String filiere) {
        filteredEtudiants.clear();

        for (EtudiantProjetDTO e : allEtudiants) {
            if (filiere.equals("Toutes") || e.getFiliere().equalsIgnoreCase(filiere)) {
                filteredEtudiants.add(e);
            }
        }

        adapter.notifyDataSetChanged();
    }
}

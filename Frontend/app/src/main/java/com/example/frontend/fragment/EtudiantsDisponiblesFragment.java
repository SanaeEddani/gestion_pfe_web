package com.example.frontend.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.EtudiantAdapter;
import com.example.frontend.api.EncadrantApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EncadrerRequest;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtudiantsDisponiblesFragment extends Fragment {

    private EncadrantApi api;
    private EtudiantAdapter adapter;
    private final List<EtudiantProjetDTO> etudiants = new ArrayList<>();
    private int encadrantId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_etudiants_disponibles,
                container,
                false
        );

        Spinner spinner = view.findViewById(R.id.spinnerFiliere);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerEtudiants);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🔹 Récupération ID encadrant
        encadrantId = getEncadrantId();

        api = RetrofitClient.getRetrofitInstance()
                .create(EncadrantApi.class);

        // ✅ ADAPTER EN MODE "ETUDIANTS DISPONIBLES"
        adapter = new EtudiantAdapter(
                etudiants,
                EtudiantAdapter.MODE_ETUDIANTS_DISPO,
                etudiant -> {

                    if (encadrantId == -1) {
                        Toast.makeText(
                                getContext(),
                                "Encadrant non identifié",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    EncadrerRequest body = new EncadrerRequest(
                            etudiant.getProjetId(),
                            encadrantId
                    );

                    api.encadrer(body).enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(
                                Call<Void> call,
                                Response<Void> response
                        ) {
                            if (response.isSuccessful()) {

                                etudiants.remove(etudiant);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(
                                        getContext(),
                                        "Étudiant encadré ✅",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else if (response.code() == 409) {

                                Toast.makeText(
                                        getContext(),
                                        "Projet déjà encadré",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        getContext(),
                                        "Erreur serveur : " + response.code(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<Void> call,
                                Throwable t
                        ) {
                            Toast.makeText(
                                    getContext(),
                                    "Erreur réseau",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
        );

        recyclerView.setAdapter(adapter);

        // 🔹 Spinner filières
        String[] filieres = {"Toutes", "GI", "GE"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                filieres
        );
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                String selected = filieres[position];
                chargerEtudiants(
                        selected.equals("Toutes") ? null : selected
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 🔥 Chargement initial
        chargerEtudiants(null);

        return view;
    }

    private void chargerEtudiants(String filiere) {

        api.getEtudiantsDisponibles(filiere)
                .enqueue(new Callback<List<EtudiantProjetDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<EtudiantProjetDTO>> call,
                            Response<List<EtudiantProjetDTO>> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {

                            etudiants.clear();
                            etudiants.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<EtudiantProjetDTO>> call,
                            Throwable t
                    ) {
                        Toast.makeText(
                                getContext(),
                                "Erreur chargement étudiants",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private int getEncadrantId() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE);
        return sp.getInt("user_id", -1);
    }
}

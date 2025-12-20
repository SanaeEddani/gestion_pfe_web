package com.example.frontend.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.DetailEtudiantActivity;
import com.example.frontend.adapter.EtudiantAdapter;
import com.example.frontend.api.EncadrantApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesEtudiantsFragment extends Fragment {

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
                R.layout.fragment_mes_etudiants,
                container,
                false
        );

        RecyclerView recyclerView = view.findViewById(R.id.recyclerMesEtudiants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🔹 ID encadrant connecté
        encadrantId = getEncadrantId();

        api = RetrofitClient.getRetrofitInstance()
                .create(EncadrantApi.class);

        adapter = new EtudiantAdapter(
                etudiants,
                EtudiantAdapter.MODE_MES_ETUDIANTS,
                etudiant -> {

                    // ✅ NAVIGATION VERS DETAIL (avec les infos)
                    Intent intent = new Intent(
                            requireContext(),
                            DetailEtudiantActivity.class
                    );

                    intent.putExtra("projetId", etudiant.getProjetId());
                    intent.putExtra("nom", etudiant.getNom());
                    intent.putExtra("prenom", etudiant.getPrenom());
                    intent.putExtra("filiere", etudiant.getFiliere());
                    intent.putExtra("sujet", etudiant.getSujet());
                    intent.putExtra("entreprise", etudiant.getEntreprise());
                    intent.putExtra(
                            "dateDebut",
                            String.valueOf(etudiant.getDateDebut())
                    );
                    intent.putExtra(
                            "dateFin",
                            String.valueOf(etudiant.getDateFin())
                    );

                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(adapter);

        // 🔥 Chargement initial
        chargerMesEtudiants();

        return view;
    }

    // ✅ Refresh automatique
    @Override
    public void onResume() {
        super.onResume();
        chargerMesEtudiants();
    }

    private void chargerMesEtudiants() {

        api.getMesEtudiants(encadrantId)
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
                                "Erreur chargement mes étudiants",
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

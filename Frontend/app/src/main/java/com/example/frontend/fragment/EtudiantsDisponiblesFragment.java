package com.example.frontend.fragment;

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
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtudiantsDisponiblesFragment extends Fragment {

    private static final int ENCADRANT_ID = 6; // Karim Haddad

    private EncadrantApi api;
    private EtudiantAdapter adapter;
    private final List<EtudiantProjetDTO> etudiants = new ArrayList<>();

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

        adapter = new EtudiantAdapter(etudiants, etudiant -> {
            api.encadrer(etudiant.getProjetId(), ENCADRANT_ID)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            etudiants.remove(etudiant);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(),
                                    "Étudiant encadré", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(),
                                    "Erreur encadrement", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        recyclerView.setAdapter(adapter);

        api = RetrofitClient.getRetrofitInstance().create(EncadrantApi.class);

        // Spinner filières
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

        // 🔥 chargement initial
        chargerEtudiants(null);

        return view;
    }

    private void chargerEtudiants(String filiere) {
        api.getEtudiants(filiere).enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(
                    Call<List<EtudiantProjetDTO>> call,
                    Response<List<EtudiantProjetDTO>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    etudiants.clear();
                    etudiants.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Erreur chargement étudiants",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}

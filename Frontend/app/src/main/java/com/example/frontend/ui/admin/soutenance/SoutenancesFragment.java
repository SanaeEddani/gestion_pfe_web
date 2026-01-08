package com.example.frontend.ui.admin;

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
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.EtudiantProjetDTO;
import com.example.frontend.model.Salle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoutenancesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminApi api;
    private List<Salle> salles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soutenances, container, false);

        recyclerView = view.findViewById(R.id.recyclerSoutenances);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = RetrofitClientAdmin
                .getInstance(requireContext())
                .create(AdminApi.class);

        loadSalles();
        loadProjetsEligibles();

        return view;
    }

    private void loadSalles() {
        api.getSalles().enqueue(new Callback<List<Salle>>() {
            @Override
            public void onResponse(Call<List<Salle>> call, Response<List<Salle>> response) {
                if (response.isSuccessful()) {
                    salles = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Salle>> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur chargement salles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProjetsEligibles() {
        api.getProjetsEligibles().enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(Call<List<EtudiantProjetDTO>> call,
                                   Response<List<EtudiantProjetDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    SoutenanceAdapter adapter =
                            new SoutenanceAdapter(response.body(), salles, api, getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

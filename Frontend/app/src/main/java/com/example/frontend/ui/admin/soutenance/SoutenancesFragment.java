package com.example.frontend.ui.admin.soutenance;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.frontend.R;
import com.example.frontend.api.AdminApiService;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoutenancesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProjetSoutenanceAdapter adapter;
    private com.example.frontend.api.AdminApi api; // API directement

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soutenances, container, false);

        recyclerView = view.findViewById(R.id.recyclerSoutenances);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = AdminApiService.getInstance(); // injection correcte

        adapter = new ProjetSoutenanceAdapter(new ArrayList<>(), getContext(), api);
        recyclerView.setAdapter(adapter);

        loadProjets();

        return view;
    }

    private void loadProjets() {
        api.getProjetsEligibles().enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(Call<List<EtudiantProjetDTO>> call, Response<List<EtudiantProjetDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body()); // met à jour la RecyclerView
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

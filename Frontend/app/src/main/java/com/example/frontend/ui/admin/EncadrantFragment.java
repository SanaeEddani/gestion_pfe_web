package com.example.frontend.ui.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.EncadrantAdmin;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncadrantFragment extends Fragment implements EncadrantAdapter.OnEncadrantActionListener {

    private EncadrantAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_encadrants, container, false);

        RecyclerView rv = view.findViewById(R.id.recyclerEncadrants);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText editName = view.findViewById(R.id.editFilterName);
        EditText editDepartement = view.findViewById(R.id.editFilterDepartement);
        EditText editNbr = view.findViewById(R.id.editFilterNbrEtudiants);

        AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);

        api.getEncadrants().enqueue(new Callback<List<EncadrantAdmin>>() {
            @Override
            public void onResponse(Call<List<EncadrantAdmin>> call, Response<List<EncadrantAdmin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new EncadrantAdapter(response.body(), EncadrantFragment.this);
                    rv.setAdapter(adapter);

                    TextWatcher watcher = new TextWatcher() {
                        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override public void afterTextChanged(Editable s) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.filter(
                                    editName.getText().toString(),
                                    editDepartement.getText().toString(),
                                    editNbr.getText().toString()
                            );
                        }
                    };

                    editName.addTextChangedListener(watcher);
                    editDepartement.addTextChangedListener(watcher);
                    editNbr.addTextChangedListener(watcher);
                }
            }

            @Override
            public void onFailure(Call<List<EncadrantAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    // ================== CALLBACKS POUR ADAPTER ==================
    @Override
    public void onAddStudentsClicked(EncadrantAdmin encadrant) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_students);

        EditText editApogee = dialog.findViewById(R.id.editNumApogee);
        LinearLayout container = dialog.findViewById(R.id.containerApogeeList);
        Button btnAdd = dialog.findViewById(R.id.btnAddApogee);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmAdd);

        List<String> apogees = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            String num = editApogee.getText().toString().trim();
            if (!num.isEmpty() && !apogees.contains(num)) {
                apogees.add(num);
                TextView tv = new TextView(getContext());
                tv.setText(num);
                container.addView(tv);
                editApogee.setText("");
            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (!apogees.isEmpty()) {
                AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);
                api.addStudentsToEncadrant(encadrant.id, apogees).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Ajout réussi", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        refreshEncadrants();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public void onRemoveStudentsClicked(EncadrantAdmin encadrant) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_remove_students);

        EditText editApogee = dialog.findViewById(R.id.editNumApogeeRemove);
        LinearLayout container = dialog.findViewById(R.id.containerApogeeListRemove);
        Button btnAdd = dialog.findViewById(R.id.btnAddApogeeRemove);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmRemove);

        List<String> apogees = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            String num = editApogee.getText().toString().trim();
            if (!num.isEmpty() && !apogees.contains(num)) {
                apogees.add(num);
                TextView tv = new TextView(getContext());
                tv.setText(num);
                container.addView(tv);
                editApogee.setText("");
            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (!apogees.isEmpty()) {
                AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);
                api.removeStudentsFromEncadrant(encadrant.id, apogees).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Suppression réussie", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        refreshEncadrants();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.show();
    }

    private void refreshEncadrants() {
        AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);
        api.getEncadrants().enqueue(new Callback<List<EncadrantAdmin>>() {
            @Override
            public void onResponse(Call<List<EncadrantAdmin>> call, Response<List<EncadrantAdmin>> response) {
                if (response.isSuccessful() && response.body() != null && adapter != null) {
                    adapter.updateData(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<EncadrantAdmin>> call, Throwable t) {}
        });
    }
}

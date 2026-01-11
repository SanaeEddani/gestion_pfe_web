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
import com.example.frontend.model.StudentAdmin;

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

        EditText editNomPrenom = dialog.findViewById(R.id.editNomPrenomStudent);
        LinearLayout container = dialog.findViewById(R.id.containerApogeeList);
        Button btnAdd = dialog.findViewById(R.id.btnAddApogee);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmAdd);

        List<String> studentsList = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            String nomPrenom = editNomPrenom.getText().toString().trim();
            if (!nomPrenom.isEmpty() && !studentsList.contains(nomPrenom)) {
                studentsList.add(nomPrenom);
                TextView tv = new TextView(getContext());
                tv.setText(nomPrenom);
                container.addView(tv);
                editNomPrenom.setText("");
            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (!studentsList.isEmpty()) {
                AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);

                api.getStudents().enqueue(new Callback<List<StudentAdmin>>() {
                    @Override
                    public void onResponse(Call<List<StudentAdmin>> call, Response<List<StudentAdmin>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<String> apogees = new ArrayList<>();
                            for (String nomPrenom : studentsList) {
                                String[] parts = nomPrenom.split(" ", 2);
                                String nom = parts[0].trim();
                                String prenom = parts.length > 1 ? parts[1].trim() : "";
                                for (StudentAdmin s : response.body()) {
                                    if (s.nom.equalsIgnoreCase(nom) && s.prenom.equalsIgnoreCase(prenom)) {
                                        apogees.add(s.getApogee());
                                    }
                                }
                            }

                            if (!apogees.isEmpty()) {
                                int currentCount = encadrant.etudiants != null ? encadrant.etudiants.size() : 0;
                                if (currentCount + apogees.size() > 10) {
                                    Toast.makeText(getContext(),
                                            "Impossible : cet encadrant ne peut pas avoir plus de 10 étudiants",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }

                                api.addStudentsToEncadrant(encadrant.getId(), apogees).enqueue(new Callback<Void>() {
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
                            } else {
                                Toast.makeText(getContext(), "Aucun étudiant trouvé", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StudentAdmin>> call, Throwable t) {
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

        EditText editNomPrenomRemove = dialog.findViewById(R.id.editNomPrenomStudentRemove);
        LinearLayout container = dialog.findViewById(R.id.containerApogeeListRemove);
        Button btnAdd = dialog.findViewById(R.id.btnAddApogeeRemove);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmRemove);

        List<String> studentsListRemove = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            String nomPrenom = editNomPrenomRemove.getText().toString().trim();
            if (!nomPrenom.isEmpty() && !studentsListRemove.contains(nomPrenom)) {
                studentsListRemove.add(nomPrenom);
                TextView tv = new TextView(getContext());
                tv.setText(nomPrenom);
                container.addView(tv);
                editNomPrenomRemove.setText("");
            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (!studentsListRemove.isEmpty()) {
                AdminApi api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);

                api.getStudents().enqueue(new Callback<List<StudentAdmin>>() {
                    @Override
                    public void onResponse(Call<List<StudentAdmin>> call, Response<List<StudentAdmin>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<String> apogees = new ArrayList<>();
                            for (String nomPrenom : studentsListRemove) {
                                String[] parts = nomPrenom.split(" ", 2);
                                String nom = parts[0].trim();
                                String prenom = parts.length > 1 ? parts[1].trim() : "";
                                for (StudentAdmin s : response.body()) {
                                    if (s.getNom().equalsIgnoreCase(nom) && s.getPrenom().equalsIgnoreCase(prenom)) {
                                        apogees.add(s.getApogee());
                                    }
                                }
                            }

                            if (!apogees.isEmpty()) {
                                api.removeStudentsFromEncadrant(encadrant.getId(), apogees)
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Suppression réussie", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    refreshEncadrants(); // ✅ recharge depuis le serveur
                                                } else {
                                                    Toast.makeText(getContext(), "Erreur suppression", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "Aucun étudiant trouvé", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StudentAdmin>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshEncadrants();
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

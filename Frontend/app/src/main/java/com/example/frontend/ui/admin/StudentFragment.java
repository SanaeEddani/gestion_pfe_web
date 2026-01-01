package com.example.frontend.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.AffectationRequest;
import com.example.frontend.model.EncadrantAdmin;
import com.example.frontend.model.StudentAdmin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFragment extends Fragment implements StudentAdapter.OnStudentActionListener {

    private StudentAdapter adapter;
    private AdminApi api;

    private EditText editName, editApogee;
    private Spinner spinnerStatus;

    private List<EncadrantAdmin> encadrants = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        api = RetrofitClientAdmin.getInstance(requireContext()).create(AdminApi.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editName = view.findViewById(R.id.editFilterName);
        editApogee = view.findViewById(R.id.editFilterApogee);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);

        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(requireContext(),
                        R.array.status_student,
                        android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);
        spinnerStatus.setSelection(0);

        setupFilters();
        loadEncadrants();           // Charger les encadrants réels
        loadStudents(recyclerView);

        return view;
    }

    private void setupFilters() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
        };

        editName.addTextChangedListener(watcher);
        editApogee.addTextChangedListener(watcher);

        spinnerStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void applyFilter() {
        if (adapter == null) return;
        adapter.filter(
                editName.getText().toString(),
                editApogee.getText().toString(),
                spinnerStatus.getSelectedItem().toString()
        );
    }

    private void loadEncadrants() {
        api.getEncadrants().enqueue(new Callback<List<EncadrantAdmin>>() {
            @Override
            public void onResponse(Call<List<EncadrantAdmin>> call, Response<List<EncadrantAdmin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    encadrants = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<EncadrantAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur chargement encadrants", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStudents(getView().findViewById(R.id.recyclerStudents));
    }


    private void loadStudents(RecyclerView recyclerView) {
        api.getStudents().enqueue(new Callback<List<StudentAdmin>>() {
            @Override
            public void onResponse(Call<List<StudentAdmin>> call, Response<List<StudentAdmin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new StudentAdapter(response.body(), StudentFragment.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudentAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAffect(StudentAdmin student) {
        showAffectDialog(student, false);
    }

    @Override
    public void onReaffect(StudentAdmin student) {
        showAffectDialog(student, true);
    }

    private void showAffectDialog(StudentAdmin student, boolean isReaffect) {

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_affect_student, null);

        EditText editEncadrantNomPrenom = dialogView.findViewById(R.id.editEncadrantNomPrenom);
        Button btnConfirm = dialogView.findViewById(R.id.btnAffect);

        // Pré-remplissage en cas de réaffectation
        if (isReaffect && student.encadrantNom != null) {
            editEncadrantNomPrenom.setText(student.encadrantNom);
        }

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(isReaffect ? "Réaffecter l'étudiant" : "Affecter l'étudiant")
                .setView(dialogView)
                .setCancelable(true)
                .create();

        btnConfirm.setOnClickListener(v -> {

            String nomPrenom = editEncadrantNomPrenom.getText().toString().trim();
            if (nomPrenom.isEmpty()) {
                editEncadrantNomPrenom.setError("Nom et prénom obligatoires");
                return;
            }

            Long encadrantId = null;
            for (EncadrantAdmin e : encadrants) {
                String fullName = e.nom + " " + e.prenom;
                if (fullName.equalsIgnoreCase(nomPrenom)) {
                    encadrantId = e.id;
                    break;
                }
            }

            if (encadrantId == null) {
                editEncadrantNomPrenom.setError("Encadrant introuvable");
                return;
            }

            // Nouvelle structure avec ID encadrant
            AffectationRequest dto = new AffectationRequest(student.id, encadrantId);

            Call<Void> call = isReaffect
                    ? api.reaffecter(dto)
                    : api.affecter(dto);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(),
                                isReaffect ? "Réaffectation réussie" : "Affectation réussie",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadStudents(getView().findViewById(R.id.recyclerStudents));
                    } else {
                        Toast.makeText(getContext(),
                                "Erreur serveur",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(),
                            t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }
}

package com.example.frontend.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.StudentAdmin;

import java.util.List;

import retrofit2.*;

public class StudentFragment extends Fragment {

    private StudentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_students, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText editName = view.findViewById(R.id.editFilterName);
        EditText editApogee = view.findViewById(R.id.editFilterApogee);
        Spinner spinnerStatus = view.findViewById(R.id.spinnerStatus);

        // ✅ INITIALISATION SPINNER (OBLIGATOIRE)
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.status_student,
                        android.R.layout.simple_spinner_item
                );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);
        spinnerStatus.setSelection(0); // "Tous"

        AdminApi api = RetrofitClientAdmin
                .getInstance(requireContext()).create(AdminApi.class);

        api.getStudents().enqueue(new Callback<List<StudentAdmin>>() {
            @Override
            public void onResponse(Call<List<StudentAdmin>> call,
                                   Response<List<StudentAdmin>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    adapter = new StudentAdapter(response.body());
                    recyclerView.setAdapter(adapter);

                    TextWatcher watcher = new TextWatcher() {
                        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override public void afterTextChanged(Editable s) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            applyFilter(editName, editApogee, spinnerStatus);
                        }
                    };

                    editName.addTextChangedListener(watcher);
                    editApogee.addTextChangedListener(watcher);

                    spinnerStatus.setOnItemSelectedListener(
                            new android.widget.AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(android.widget.AdapterView<?> parent,
                                                           View view, int position, long id) {
                                    applyFilter(editName, editApogee, spinnerStatus);
                                }

                                @Override
                                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                            }
                    );
                }
            }

            @Override
            public void onFailure(Call<List<StudentAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    // ✅ MÉTHODE SÉCURISÉE DE FILTRAGE
    private void applyFilter(EditText editName, EditText editApogee, Spinner spinnerStatus) {

        if (adapter == null) return;

        String name = editName.getText().toString();
        String apogee = editApogee.getText().toString();

        String status = "Tous";
        if (spinnerStatus.getSelectedItem() != null) {
            status = spinnerStatus.getSelectedItem().toString();
        }

        adapter.filter(name, apogee, status);
    }
}

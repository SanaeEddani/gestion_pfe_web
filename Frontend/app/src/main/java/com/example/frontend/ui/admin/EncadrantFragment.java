package com.example.frontend.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.EncadrantAdmin;
import com.example.frontend.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncadrantFragment extends Fragment {

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
                    adapter = new EncadrantAdapter(response.body());
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
}

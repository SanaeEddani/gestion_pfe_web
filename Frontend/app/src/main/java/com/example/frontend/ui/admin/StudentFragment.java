package com.example.frontend.ui.admin;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.frontend.api.*;
import com.example.frontend.model.StudentAdmin;
import java.util.List;
import retrofit2.*;

public class StudentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = new RecyclerView(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        AdminApi api = RetrofitClientAdmin
                .getInstance(requireContext()).create(AdminApi.class);

        api.getStudents().enqueue(new Callback<List<StudentAdmin>>() {
            @Override
            public void onResponse(Call<List<StudentAdmin>> call, Response<List<StudentAdmin>> response) {
                if (response.isSuccessful()) {
                    rv.setAdapter(new StudentAdapter(response.body()));
                } else {
                    Toast.makeText(getContext(), "Erreur serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StudentAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rv;
    }
}

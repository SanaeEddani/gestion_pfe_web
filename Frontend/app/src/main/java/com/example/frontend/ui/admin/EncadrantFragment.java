package com.example.frontend.ui.admin;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.frontend.api.*;
import com.example.frontend.model.EncadrantAdmin;
import java.util.List;
import retrofit2.*;

public class EncadrantFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = new RecyclerView(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        AdminApi api = RetrofitClientAdmin
                .getInstance(requireContext()).create(AdminApi.class);

        api.getEncadrants().enqueue(new Callback<List<EncadrantAdmin>>() {
            @Override
            public void onResponse(Call<List<EncadrantAdmin>> call, Response<List<EncadrantAdmin>> response) {
                if (response.isSuccessful()) {
                    rv.setAdapter(new EncadrantAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<EncadrantAdmin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rv;
    }
}

package com.example.frontend;




import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;
import com.example.frontend.api.ApiClient;
import com.example.frontend.api.ApiService;
import com.example.frontend.model.EtudiantProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtudiantProfileActivity extends AppCompatActivity {

    TextView tvNom, tvPrenom, tvEmail, tvFiliere, tvDepartement, tvAppogee, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_profile);

        tvNom = findViewById(R.id.tvNom);
        tvPrenom = findViewById(R.id.tvPrenom);
        tvEmail = findViewById(R.id.tvEmail);
        tvFiliere = findViewById(R.id.tvFiliere);
        tvDepartement = findViewById(R.id.tvDepartement);
        tvAppogee = findViewById(R.id.tvAppogee);
        tvRole = findViewById(R.id.tvRole);

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getEtudiantProfile("Bearer " + token)
                .enqueue(new Callback<EtudiantProfile>() {
                    @Override
                    public void onResponse(Call<EtudiantProfile> call, Response<EtudiantProfile> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            EtudiantProfile e = response.body();
                            tvNom.setText(e.getNom());
                            tvPrenom.setText(e.getPrenom());
                            tvEmail.setText(e.getEmail());
                        } else {
                            Toast.makeText(EtudiantProfileActivity.this,
                                    "Profil non trouvé", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EtudiantProfile> call, Throwable t) {
                        Toast.makeText(EtudiantProfileActivity.this,
                                "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}

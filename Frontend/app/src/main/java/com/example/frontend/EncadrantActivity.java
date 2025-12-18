package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.EncadrantApi;
import com.example.frontend.api.RetrofitClient;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncadrantActivity extends AppCompatActivity {

    private static final String TAG = "ENCADRANT_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ UTILISE TON XML
        setContentView(R.layout.activity_encadrant);

        TextView txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setText("EncadrantActivity chargée ✅");

        EncadrantApi api = RetrofitClient
                .getRetrofitInstance()
                .create(EncadrantApi.class);

        // ============================
        // TEST GET étudiants
        // ============================
        api.getEtudiants(null).enqueue(new Callback<List<EtudiantProjetDTO>>() {
            @Override
            public void onResponse(Call<List<EtudiantProjetDTO>> call,
                                   Response<List<EtudiantProjetDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    txtStatus.setText("Étudiants trouvés : " + response.body().size());
                    Log.d(TAG, "Nombre d'étudiants : " + response.body().size());
                } else {
                    txtStatus.setText("Erreur API : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<EtudiantProjetDTO>> call, Throwable t) {
                txtStatus.setText("Erreur réseau ❌");
                Log.e(TAG, "Erreur réseau", t);
            }
        });
    }
}

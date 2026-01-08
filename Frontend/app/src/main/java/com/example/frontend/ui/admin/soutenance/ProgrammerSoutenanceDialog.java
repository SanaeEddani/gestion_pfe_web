package com.example.frontend.ui.admin.soutenance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.*;
import android.widget.*;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.model.SoutenanceDTO;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammerSoutenanceDialog {

    private final Context context;
    private final AdminApi api;
    private final Long projetId;

    public ProgrammerSoutenanceDialog(Context context,
                                      AdminApi api,
                                      Long projetId) {
        this.context = context;
        this.api = api;
        this.projetId = projetId;
    }

    public void show() {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.dialog_programmer_soutenance, null);

        Button btnConfirm = v.findViewById(R.id.btnConfirm);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(v)
                .setTitle("Programmer soutenance")
                .create();

        btnConfirm.setOnClickListener(b -> {
            // Exemple simple avec dates fixes
            SoutenanceDTO dto = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dto = new SoutenanceDTO(
                        projetId,
                        1L, // ID de la salle (à remplacer par la sélection réelle)
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(2)
                );
            }

            api.programmerSoutenance(dto).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Soutenance programmée", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Erreur lors de la programmation", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}

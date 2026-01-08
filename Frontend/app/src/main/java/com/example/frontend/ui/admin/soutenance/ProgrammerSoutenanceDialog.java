package com.example.frontend.ui.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.model.Salle;
import com.example.frontend.model.SoutenanceDTO;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammerSoutenanceDialog {

    private final Context context;
    private final AdminApi api;
    private final List<Salle> salles;
    private final Long projetId;

    private LocalDateTime debut, fin;

    public ProgrammerSoutenanceDialog(Context context,
                                      AdminApi api,
                                      List<Salle> salles,
                                      Long projetId) {
        this.context = context;
        this.api = api;
        this.salles = salles;
        this.projetId = projetId;
    }

    public void show() {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_programmer_soutenance, null);

        Spinner spinner = view.findViewById(R.id.spinnerSalles);
        Button btnDebut = view.findViewById(R.id.btnDebutPicker);
        Button btnFin = view.findViewById(R.id.btnFinPicker);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                salles.stream().map(Salle::getNom).toList()
        );
        spinner.setAdapter(adapter);

        btnDebut.setOnClickListener(v ->
                debut = LocalDateTime.now().plusDays(1)
        );

        btnFin.setOnClickListener(v ->
                fin = debut.plusHours(2)
        );

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Programmer soutenance")
                .create();

        btnConfirm.setOnClickListener(v -> {
            if (debut == null || fin == null) {
                Toast.makeText(context, "Choisir les dates", Toast.LENGTH_SHORT).show();
                return;
            }

            Long salleId = salles.get(spinner.getSelectedItemPosition()).getId();

            api.programmerSoutenance(
                    new SoutenanceDTO(projetId, salleId, debut, fin)
            ).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Soutenance programmée", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }
}

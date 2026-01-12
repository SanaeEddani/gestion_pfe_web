package com.example.frontend.ui.admin.soutenance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.model.Salle;
import com.example.frontend.model.SoutenanceDTO;

import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammerSoutenanceDialog {

    private final Context context;
    private final AdminApi api;
    private final long projetId;

    private Spinner spinnerSalles;
    private Button btnDebutPicker, btnFinPicker;

    private Calendar dateDebutCalendar;
    private Calendar dateFinCalendar;

    private static final SimpleDateFormat displayFormatter =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);

    private static final SimpleDateFormat isoFormatter =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    public ProgrammerSoutenanceDialog(Context context, AdminApi api) {
        this.context = context;
        this.api = api;

        // ✅ Récupération ID projet depuis SharedPreferences
        this.projetId = context.getSharedPreferences("admin_prefs", Context.MODE_PRIVATE)
                .getLong("projet_id", -1);

        if (projetId == -1) {
            Toast.makeText(context,
                    "Erreur : ID du projet introuvable",
                    Toast.LENGTH_LONG).show();
            Log.e("SoutenanceDebug", "projet_id manquant !");
        } else {
            Log.d("SoutenanceDebug", "Projet ID = " + projetId);
        }
    }

    public void show() {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.dialog_programmer_soutenance, null);

        spinnerSalles = v.findViewById(R.id.spinnerSalles);
        btnDebutPicker = v.findViewById(R.id.btnDebutPicker);
        btnFinPicker = v.findViewById(R.id.btnFinPicker);
        Button btnConfirm = v.findViewById(R.id.btnConfirm);

        chargerSalles();

        btnDebutPicker.setOnClickListener(view -> showDateTimePicker(true));
        btnFinPicker.setOnClickListener(view -> showDateTimePicker(false));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(v)
                .setTitle("Programmer une soutenance")
                .create();

        btnConfirm.setOnClickListener(b -> {
            if (validerFormulaire()) {
                programmerSoutenance();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void chargerSalles() {
        api.getSalles().enqueue(new Callback<List<Salle>>() {
            @Override
            public void onResponse(Call<List<Salle>> call,
                                   Response<List<Salle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayAdapter<Salle> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            response.body()
                    );
                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item);
                    spinnerSalles.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Salle>> call, Throwable t) {
                Toast.makeText(context,
                        "Erreur chargement salles",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker(boolean debut) {
        Calendar now = Calendar.getInstance();

        new DatePickerDialog(context, (view, y, m, d) -> {
            new TimePickerDialog(context, (tp, h, min) -> {
                Calendar c = Calendar.getInstance();
                c.set(y, m, d, h, min, 0);

                if (debut) {
                    dateDebutCalendar = c;
                    btnDebutPicker.setText("Début : "
                            + displayFormatter.format(c.getTime()));
                } else {
                    dateFinCalendar = c;
                    btnFinPicker.setText("Fin : "
                            + displayFormatter.format(c.getTime()));
                }
            }, now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), true).show();

        }, now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validerFormulaire() {
        if (projetId == -1) return false;
        if (spinnerSalles.getSelectedItem() == null) return false;
        if (dateDebutCalendar == null || dateFinCalendar == null) return false;
        if (dateFinCalendar.before(dateDebutCalendar)) return false;
        return true;
    }

    private void programmerSoutenance() {
        Salle salle = (Salle) spinnerSalles.getSelectedItem();

        SoutenanceDTO dto = new SoutenanceDTO(
                projetId,
                salle.getId(),
                isoFormatter.format(dateDebutCalendar.getTime()),
                isoFormatter.format(dateFinCalendar.getTime())
        );

        api.programmerSoutenance(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                Toast.makeText(context,
                        "Soutenance programmée",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context,
                        "Erreur réseau",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammerSoutenanceDialog {

    private final Context context;
    private final AdminApi api;
    private final Long projetId;

    private Spinner spinnerSalles;
    private Button btnDebutPicker, btnFinPicker;

    private Calendar dateDebutCalendar = null;
    private Calendar dateFinCalendar = null;

    private List<Salle> listeSalles = new ArrayList<>();

    // Formatter affichage
    private static final SimpleDateFormat displayFormatter =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);

    // Formatter ISO pour backend
    private static final SimpleDateFormat isoFormatter =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    public ProgrammerSoutenanceDialog(Context context, AdminApi api, Long projetId) {
        this.context = context;
        this.api = api;
        this.projetId = projetId;
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
            public void onResponse(Call<List<Salle>> call, Response<List<Salle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listeSalles = response.body();

                    ArrayAdapter<Salle> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            listeSalles
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSalles.setAdapter(adapter);

                    if (listeSalles.isEmpty()) {
                        Toast.makeText(context,
                                "Aucune salle disponible",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,
                            "Erreur lors du chargement des salles",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Salle>> call, Throwable t) {
                Toast.makeText(context,
                        "Erreur réseau",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker(boolean estDateDebut) {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {

                    TimePickerDialog timePicker = new TimePickerDialog(
                            context,
                            (timeView, hourOfDay, minute) -> {

                                Calendar selected = Calendar.getInstance();
                                selected.set(year, month, dayOfMonth, hourOfDay, minute, 0);

                                if (estDateDebut) {
                                    dateDebutCalendar = selected;
                                    btnDebutPicker.setText(
                                            "Début: " + displayFormatter.format(selected.getTime())
                                    );
                                } else {
                                    dateFinCalendar = selected;
                                    btnFinPicker.setText(
                                            "Fin: " + displayFormatter.format(selected.getTime())
                                    );
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );

                    timePicker.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }

    private boolean validerFormulaire() {

        if (spinnerSalles.getSelectedItem() == null) {
            Toast.makeText(context,
                    "Veuillez sélectionner une salle",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateDebutCalendar == null || dateFinCalendar == null) {
            Toast.makeText(context,
                    "Veuillez sélectionner les dates",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateFinCalendar.before(dateDebutCalendar)) {
            Toast.makeText(context,
                    "La date de fin doit être après la date de début",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateDebutCalendar.before(Calendar.getInstance())) {
            Toast.makeText(context,
                    "La date de début ne peut pas être dans le passé",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void programmerSoutenance() {

        Salle salleSelectionnee = (Salle) spinnerSalles.getSelectedItem();

        String dateDebutIso = isoFormatter.format(dateDebutCalendar.getTime());
        String dateFinIso = isoFormatter.format(dateFinCalendar.getTime());

        // 🔹 AJOUT DES LOGS POUR DEBUG
        Log.d("SoutenanceDebug", "Projet ID envoyé : " + projetId);
        Log.d("SoutenanceDebug", "Salle ID envoyée : " + salleSelectionnee.getId());
        Log.d("SoutenanceDebug", "Date début : " + dateDebutIso);
        Log.d("SoutenanceDebug", "Date fin : " + dateFinIso);

        SoutenanceDTO dto = new SoutenanceDTO(
                projetId,
                salleSelectionnee.getId(),
                dateDebutIso,
                dateFinIso
        );

        api.programmerSoutenance(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context,
                            "Soutenance programmée avec succès",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,
                            "Erreur lors de la programmation",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context,
                        "Erreur réseau : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

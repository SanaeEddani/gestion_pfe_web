package com.example.frontend.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;

import com.example.frontend.AdminActivity;
import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.api.RetrofitClientAdmin;
import com.example.frontend.model.DashboardStats;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    private PieChart pieChartEtudiants;
    private PieChart pieChartEncadrants;
    private Button btnGestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        pieChartEtudiants = findViewById(R.id.pieChartEtudiants);
        pieChartEncadrants = findViewById(R.id.pieChartEncadrants);
        btnGestion = findViewById(R.id.btnGestion);

        AdminApi api = RetrofitClientAdmin
                .getInstance(this)
                .create(AdminApi.class);

        api.getDashboardStats().enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStats stats = response.body();
                    showPieCharts(stats);
                }
            }

            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                // Gestion de l'erreur
            }
        });

        btnGestion.setOnClickListener(v ->
                startActivity(new Intent(this, AdminActivity.class))
        );
    }

    private void showPieCharts(DashboardStats stats) {

        // --- Premier graphique : Étudiants ---
        ArrayList<PieEntry> entriesEtudiants = new ArrayList<>();
        int totalEtudiants = stats.etudiantsAffectes + stats.etudiantsNonAffectes;
        float pourcAffectes = totalEtudiants == 0 ? 0 : (stats.etudiantsAffectes * 100f / totalEtudiants);
        float pourcNonAffectes = totalEtudiants == 0 ? 0 : (stats.etudiantsNonAffectes * 100f / totalEtudiants);

        entriesEtudiants.add(new PieEntry(pourcAffectes, "")); // pas de texte
        entriesEtudiants.add(new PieEntry(pourcNonAffectes, ""));

        PieDataSet dataSetEtudiants = new PieDataSet(entriesEtudiants, "Étudiants affectés / non affectés");
        dataSetEtudiants.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSetEtudiants.setValueTextColor(Color.BLACK);
        dataSetEtudiants.setValueFormatter(new PercentFormatter()); // % automatique
        dataSetEtudiants.setValueTextSize(14f);

        PieData dataEtudiants = new PieData(dataSetEtudiants);
        pieChartEtudiants.setData(dataEtudiants);
        pieChartEtudiants.setUsePercentValues(true);
        pieChartEtudiants.getDescription().setEnabled(false);
        pieChartEtudiants.setCenterText("Étudiants");
        pieChartEtudiants.setCenterTextSize(16f);
        pieChartEtudiants.animateY(1000);
        pieChartEtudiants.invalidate();

        // --- Deuxième graphique : Encadrants ---
        ArrayList<PieEntry> entriesEncadrants = new ArrayList<>();
        int totalEncadrants = stats.encadrantsAvecEtudiants + stats.encadrantsSansEtudiants;
        float pourcAvec = totalEncadrants == 0 ? 0 : (stats.encadrantsAvecEtudiants * 100f / totalEncadrants);
        float pourcSans = totalEncadrants == 0 ? 0 : (stats.encadrantsSansEtudiants * 100f / totalEncadrants);

        entriesEncadrants.add(new PieEntry(pourcAvec, ""));
        entriesEncadrants.add(new PieEntry(pourcSans, ""));

        PieDataSet dataSetEncadrants = new PieDataSet(entriesEncadrants, "Encadrants avec / sans étudiants");
        dataSetEncadrants.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSetEncadrants.setValueTextColor(Color.BLACK);
        dataSetEncadrants.setValueFormatter(new PercentFormatter());
        dataSetEncadrants.setValueTextSize(14f);

        PieData dataEncadrants = new PieData(dataSetEncadrants);
        pieChartEncadrants.setData(dataEncadrants);
        pieChartEncadrants.setUsePercentValues(true);
        pieChartEncadrants.getDescription().setEnabled(false);
        pieChartEncadrants.setCenterText("Encadrants");
        pieChartEncadrants.setCenterTextSize(16f);
        pieChartEncadrants.animateY(1000);
        pieChartEncadrants.invalidate();
    }
}

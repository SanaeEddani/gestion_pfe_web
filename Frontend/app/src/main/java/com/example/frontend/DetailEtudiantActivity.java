package com.example.frontend;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;

public class DetailEtudiantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_etudiant);

        // 🔹 Récupération des données
        String nom = getIntent().getStringExtra("nom");
        String prenom = getIntent().getStringExtra("prenom");
        String filiere = getIntent().getStringExtra("filiere");
        String sujet = getIntent().getStringExtra("sujet");
        String entreprise = getIntent().getStringExtra("entreprise");
        String dateDebut = getIntent().getStringExtra("dateDebut");
        String dateFin = getIntent().getStringExtra("dateFin");

        // 🔹 Views
        TextView tvNom = findViewById(R.id.tvNom);
        TextView tvSujet = findViewById(R.id.tvSujet);
        TextView tvEntreprise = findViewById(R.id.tvEntreprise);
        TextView tvPeriode = findViewById(R.id.tvPeriode);
        TextView tvSoutenance = findViewById(R.id.tvSoutenance);

        // ✅ Affichage
        tvNom.setText(nom + " " + prenom + " (" + filiere + ")");
        tvSujet.setText("Sujet : " + sujet);
        tvEntreprise.setText("Entreprise : " + entreprise);
        tvPeriode.setText("Période : " + dateDebut + " → " + dateFin);

        // 🔜 Placeholder pour plus tard
        tvSoutenance.setText("Soutenance non planifiée");
    }
}

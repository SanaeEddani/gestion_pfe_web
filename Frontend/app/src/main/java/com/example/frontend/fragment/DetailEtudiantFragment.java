package com.example.frontend.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontend.R;

public class DetailEtudiantFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_detail_etudiant,
                container,
                false
        );

        TextView tvNom = view.findViewById(R.id.tvNom);
        TextView tvSujet = view.findViewById(R.id.tvSujet);
        TextView tvEntreprise = view.findViewById(R.id.tvEntreprise);
        TextView tvPeriode = view.findViewById(R.id.tvPeriode);
        TextView tvSoutenance = view.findViewById(R.id.tvSoutenance);

        // ✅ Récupération SAFE du projetId
        Bundle args = getArguments();
        if (args == null || !args.containsKey("projetId")) {
            Toast.makeText(
                    getContext(),
                    "Erreur : projet introuvable",
                    Toast.LENGTH_SHORT
            ).show();
            return view; // ⛔ évite le crash
        }

        int projetId = args.getInt("projetId");

        // 🔥 TEMPORAIRE (preuve que la navigation marche)
        tvNom.setText("Projet ID : " + projetId);
        tvSujet.setText("Chargement du sujet...");
        tvEntreprise.setText("");
        tvPeriode.setText("");
        tvSoutenance.setText("Soutenance non planifiée");

        return view;
    }
}

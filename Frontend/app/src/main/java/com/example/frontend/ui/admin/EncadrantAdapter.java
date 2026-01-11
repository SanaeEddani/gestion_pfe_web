package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.model.EncadrantAdmin;

import java.util.ArrayList;
import java.util.List;

public class EncadrantAdapter extends RecyclerView.Adapter<EncadrantAdapter.ViewHolder> {

    private final List<EncadrantAdmin> allEncadrants;
    private final List<EncadrantAdmin> displayedEncadrants;
    private final OnEncadrantActionListener listener;

    public EncadrantAdapter(List<EncadrantAdmin> encadrants, OnEncadrantActionListener listener) {
        this.allEncadrants = new ArrayList<>(encadrants);
        this.displayedEncadrants = new ArrayList<>(encadrants);
        this.listener = listener;
    }

    public interface OnEncadrantActionListener {
        void onAddStudentsClicked(EncadrantAdmin encadrant);
        void onRemoveStudentsClicked(EncadrantAdmin encadrant);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, email, departement;
        ImageButton btnAdd, btnRemove, btnViewStudents; // bouton œil

        ViewHolder(View v) {
            super(v);
            nomPrenom = v.findViewById(R.id.textNomPrenom);
            email = v.findViewById(R.id.textEmail);
            departement = v.findViewById(R.id.textDepartement);
            btnAdd = v.findViewById(R.id.btnAdd);
            btnRemove = v.findViewById(R.id.btnRemove);
            btnViewStudents = v.findViewById(R.id.btnViewStudents); // initialisation bouton œil
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encadrant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EncadrantAdmin e = displayedEncadrants.get(position);
        holder.nomPrenom.setText(e.nom + " " + e.prenom);
        holder.email.setText(e.email);
        holder.departement.setText(e.departement);

        // ======= RAJOUT DES ICONES =======
        holder.btnAdd.setImageResource(R.drawable.ic_add);       // ic_add pour ajouter
        holder.btnRemove.setImageResource(R.drawable.ic_remove); // ic_remove pour supprimer

        // ======= CLICK LISTENERS =======
        holder.btnAdd.setOnClickListener(v -> listener.onAddStudentsClicked(e));
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveStudentsClicked(e));

        // ======= BOUTON ŒIL POUR AFFICHER LES ÉTUDIANTS =======
        holder.btnViewStudents.setOnClickListener(v -> {
            if (e.etudiants == null || e.etudiants.isEmpty()) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Étudiants")
                        .setMessage("Aucun étudiant")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                StringBuilder sb = new StringBuilder();
                for (String nomEtudiant : e.etudiants) {
                    sb.append("• ").append(nomEtudiant).append("\n");
                }
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Étudiants de " + e.nom)
                        .setMessage(sb.toString())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedEncadrants.size();
    }

    // ======= FILTRAGE =======
    public void filter(String name, String departement, String nbrEtudiantsStr) {
        displayedEncadrants.clear();
        int nbrEtudiants = -1;
        if (!nbrEtudiantsStr.isEmpty()) {
            try { nbrEtudiants = Integer.parseInt(nbrEtudiantsStr); } catch (NumberFormatException ignored) {}
        }
        for (EncadrantAdmin e : allEncadrants) {
            boolean matchName = name.isEmpty() || (e.nom + " " + e.prenom).toLowerCase().contains(name.toLowerCase());
            boolean matchDepartement = departement.isEmpty() || (e.departement != null && e.departement.toLowerCase().contains(departement.toLowerCase()));
            boolean matchNbr = nbrEtudiants == -1 || (e.etudiants != null && e.etudiants.size() == nbrEtudiants);
            if (matchName && matchDepartement && matchNbr) displayedEncadrants.add(e);
        }
        notifyDataSetChanged();
    }

    // ======= MISE À JOUR DES DONNÉES =======
    public void updateData(List<EncadrantAdmin> newData) {
        allEncadrants.clear();
        allEncadrants.addAll(newData);
        displayedEncadrants.clear();
        displayedEncadrants.addAll(newData);
        notifyDataSetChanged();
    }
}

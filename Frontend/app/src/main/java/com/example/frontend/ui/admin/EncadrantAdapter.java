package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.model.EncadrantAdmin;
import com.example.frontend.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncadrantAdapter extends RecyclerView.Adapter<EncadrantAdapter.ViewHolder> {

    private final List<EncadrantAdmin> allEncadrants;       // Liste complète
    private final List<EncadrantAdmin> displayedEncadrants; // Liste filtrée

    public EncadrantAdapter(List<EncadrantAdmin> encadrants) {
        this.allEncadrants = new ArrayList<>(encadrants);
        this.displayedEncadrants = new ArrayList<>(encadrants);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, email, departement, etudiants;
        ImageButton btnAdd, btnRemove;

        ViewHolder(View v) {
            super(v);
            nomPrenom = v.findViewById(R.id.textNomPrenom);
            email = v.findViewById(R.id.textEmail);
            departement = v.findViewById(R.id.textDepartement);
            etudiants = v.findViewById(R.id.textEtudiants);
            btnAdd = v.findViewById(R.id.btnAdd);
            btnRemove = v.findViewById(R.id.btnRemove);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_encadrant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EncadrantAdmin e = displayedEncadrants.get(position);

        holder.nomPrenom.setText(e.nom + " " + e.prenom);
        holder.email.setText("Email: " + e.email);
        holder.departement.setText("Département: " + e.departement);

        // Affichage des noms et prénoms des étudiants
        String etudiantsStr = "Étudiants: ";
        if (e.etudiants != null && !e.etudiants.isEmpty()) {
            etudiantsStr += String.join(", ", e.etudiants);
        } else {
            etudiantsStr += "0";
        }
        holder.etudiants.setText(etudiantsStr);

        holder.btnAdd.setImageResource(R.drawable.ic_add);
        holder.btnRemove.setImageResource(R.drawable.ic_remove);

        holder.btnAdd.setOnClickListener(v -> {
            // TODO: ouvrir dialog pour ajouter étudiant
        });

        holder.btnRemove.setOnClickListener(v -> {
            // TODO: ouvrir dialog pour supprimer étudiant
        });
    }

    @Override
    public int getItemCount() {
        return displayedEncadrants.size();
    }

    // ================= FILTRAGE =================
    public void filter(String name, String departement, String nbrEtudiantsStr) {
        displayedEncadrants.clear();

        int nbrEtudiants = -1;
        if (!nbrEtudiantsStr.isEmpty()) {
            try {
                nbrEtudiants = Integer.parseInt(nbrEtudiantsStr);
            } catch (NumberFormatException ignored) {}
        }

        for (EncadrantAdmin e : allEncadrants) {
            boolean matchName = name.isEmpty() || (e.nom + " " + e.prenom).toLowerCase().contains(name.toLowerCase());
            boolean matchDepartement = departement.isEmpty() || (e.departement != null && e.departement.toLowerCase().contains(departement.toLowerCase()));
            boolean matchNbr = nbrEtudiants == -1 || (e.etudiants != null && e.etudiants.size() == nbrEtudiants);

            if (matchName && matchDepartement && matchNbr) {
                displayedEncadrants.add(e);
            }
        }
        notifyDataSetChanged();
    }
}

package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.R;
import com.example.frontend.model.EncadrantAdmin;
import com.example.frontend.model.StudentAdmin;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encadrant, parent, false);
        return new ViewHolder(view);
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        EncadrantAdmin e = displayedEncadrants.get(position);
        holder.nomPrenom.setText(e.nom + " " + e.prenom);
        holder.email.setText("Email: " + e.email);
        holder.departement.setText("Département: " + e.departement);

        String etudiantsStr = "Étudiants: ";
        if (e.etudiants != null && !e.etudiants.isEmpty()) {
            List<String> nomsComplets = new ArrayList<>();
            for (String nomEtudiant : e.etudiants) {
                nomsComplets.add(nomEtudiant);
            }

            etudiantsStr += String.join(", ", nomsComplets);
        } else {
            etudiantsStr += "0";
        }
        holder.etudiants.setText(etudiantsStr);


        // ======= RAJOUT DES ICONES =======
        holder.btnAdd.setImageResource(R.drawable.ic_add);       // ic_add pour ajouter
        holder.btnRemove.setImageResource(R.drawable.ic_remove); // ic_remove pour supprimer

        // ======= CLICK LISTENERS =======
        holder.btnAdd.setOnClickListener(v -> listener.onAddStudentsClicked(e));
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveStudentsClicked(e));
    }


    @Override
    public int getItemCount() {
        return displayedEncadrants.size();
    }

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

    public void updateData(List<EncadrantAdmin> newData) {
        allEncadrants.clear();
        allEncadrants.addAll(newData);
        displayedEncadrants.clear();
        displayedEncadrants.addAll(newData);
        notifyDataSetChanged();
    }
}

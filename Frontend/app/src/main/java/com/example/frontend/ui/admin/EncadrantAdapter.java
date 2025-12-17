package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;  // <-- IMPORTANT
import com.example.frontend.model.EncadrantAdmin;
import java.util.List;
import com.example.frontend.R;



public class EncadrantAdapter extends RecyclerView.Adapter<EncadrantAdapter.ViewHolder> {

    private final List<EncadrantAdmin> encadrants;

    public EncadrantAdapter(List<EncadrantAdmin> encadrants) {
        this.encadrants = encadrants;
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
        EncadrantAdmin e = encadrants.get(position);

        holder.nomPrenom.setText(e.nom + " " + e.prenom);
        holder.email.setText("Email: " + e.email);
        holder.departement.setText("Département: " + e.departement);
        holder.etudiants.setText("Étudiants: " + e.etudiants);

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
        return encadrants.size();
    }
}

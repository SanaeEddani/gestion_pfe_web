package com.example.frontend.ui.admin.soutenance;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.model.SoutenanceDTO;

import java.util.List;

public class SoutenanceAdapter extends RecyclerView.Adapter<SoutenanceAdapter.ViewHolder> {

    private final List<SoutenanceDTO> soutenances;

    public SoutenanceAdapter(List<SoutenanceDTO> soutenances) {
        this.soutenances = soutenances;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soutenance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        SoutenanceDTO s = soutenances.get(pos);

        h.tvDate.setText("Début : " + s.getDateDebut());
        h.tvSalle.setText("Salle : " + s.getSalle());
        h.tvEncadrant.setText("Encadrant : " + s.getNomEncadrant());
        h.tvDept.setText("Département : " + s.getDepartement());
        h.tvNb.setText("Étudiants : " + s.getNombreEtudiants());
    }

    @Override
    public int getItemCount() {
        return soutenances.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSalle, tvEncadrant, tvDept, tvNb;

        ViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvSalle = v.findViewById(R.id.tvSalle);
            tvEncadrant = v.findViewById(R.id.tvEncadrant);
            tvDept = v.findViewById(R.id.tvDepartement);
            tvNb = v.findViewById(R.id.tvNbEtudiants);
        }
    }
}

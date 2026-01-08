package com.example.frontend.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.model.EtudiantProjetDTO;
import com.example.frontend.model.Salle;

import java.util.List;

public class SoutenanceAdapter
        extends RecyclerView.Adapter<SoutenanceAdapter.ViewHolder> {

    private final List<EtudiantProjetDTO> projets;
    private final List<Salle> salles;
    private final AdminApi api;
    private final Context context;

    public SoutenanceAdapter(List<EtudiantProjetDTO> projets,
                             List<Salle> salles,
                             AdminApi api,
                             Context context) {
        this.projets = projets;
        this.salles = salles;
        this.api = api;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soutenance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        EtudiantProjetDTO p = projets.get(position);

        h.txtNom.setText(p.getNom() + " " + p.getPrenom());
        h.txtFiliere.setText(p.getFiliere());
        h.txtSujet.setText(p.getSujet());

        h.btnProgrammer.setOnClickListener(v ->
                new ProgrammerSoutenanceDialog(
                        context,
                        api,
                        salles,
                        p.getProjetId()
                ).show()
        );
    }

    @Override
    public int getItemCount() {
        return projets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtFiliere, txtSujet;
        Button btnProgrammer;

        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNom);
            txtFiliere = v.findViewById(R.id.txtFiliere);
            txtSujet = v.findViewById(R.id.txtSujet);
            btnProgrammer = v.findViewById(R.id.btnProgrammer);
        }
    }
}

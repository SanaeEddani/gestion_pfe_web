package com.example.frontend.ui.admin.soutenance;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.api.AdminApi;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.ArrayList;
import java.util.List;

public class ProjetSoutenanceAdapter extends RecyclerView.Adapter<ProjetSoutenanceAdapter.ViewHolder> {

    private final List<EtudiantProjetDTO> projets;
    private final Context context;
    private final AdminApi api;

    public ProjetSoutenanceAdapter(List<EtudiantProjetDTO> projets, Context context, AdminApi api) {
        this.projets = projets;
        this.context = context;
        this.api = api;
    }

    public ProjetSoutenanceAdapter(List<EtudiantProjetDTO> projets, AdminApi api, Context context) {
        this.projets = projets;
        this.api = api;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etudiant_soutenance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        EtudiantProjetDTO e = projets.get(pos);
        h.txtNom.setText(e.getNom() + " " + e.getPrenom());
        h.txtFiliere.setText(e.getFiliere());
        h.txtSujet.setText(e.getSujet());
        h.txtEntreprise.setText(e.getEntreprise());
        h.txtDates.setText(e.getDateDebut() + " → " + e.getDateFin());
        h.txtNom.setText(e.getNomEtudiant() + " " + e.getPrenomEtudiant());
        h.txtFiliere.setText(e.getDepartement());

        h.btnProgrammer.setOnClickListener(v -> {
            new ProgrammerSoutenanceDialog(context, api, (long) e.getId()).show();
        });

    }

    @Override
    public int getItemCount() {
        return projets.size();
    }

    public void updateList(List<EtudiantProjetDTO> newList) {
        projets.clear();
        projets.addAll(newList);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        Button btnProgrammer;
        TextView txtNom, txtFiliere, txtSujet, txtEntreprise, txtDates;

        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNomEtudiant);
            txtFiliere = v.findViewById(R.id.txtFiliereEtudiant);
            txtSujet = v.findViewById(R.id.txtSujetProjet);
            txtEntreprise = v.findViewById(R.id.txtEntrepriseProjet);
            txtDates = v.findViewById(R.id.txtDatesProjet);
            btnProgrammer = v.findViewById(R.id.btnProgrammer); // ✅ IMPORTANT
        }
    }

}

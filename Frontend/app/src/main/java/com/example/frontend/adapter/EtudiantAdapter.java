package com.example.frontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.model.EtudiantProjetDTO;

import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.ViewHolder> {

    // 🔹 MODES
    public static final int MODE_ETUDIANTS_DISPO = 0;
    public static final int MODE_MES_ETUDIANTS = 1;

    public interface OnActionClick {
        void onClick(EtudiantProjetDTO etudiant);
    }

    private final List<EtudiantProjetDTO> list;
    private final OnActionClick listener;
    private final int mode;

    // 🔹 CONSTRUCTEUR
    public EtudiantAdapter(
            List<EtudiantProjetDTO> list,
            int mode,
            OnActionClick listener
    ) {
        this.list = list;
        this.mode = mode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etudiant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EtudiantProjetDTO e = list.get(position);

        holder.txtNom.setText(
                e.getNom() + " " + e.getPrenom() + " (" + e.getFiliere() + ")"
        );
        holder.txtSujet.setText("Sujet : " + e.getSujet());
        holder.txtEntreprise.setText("Entreprise : " + e.getEntreprise());
        holder.txtDates.setText(
                "Période : " + e.getDateDebut() + " → " + e.getDateFin()
        );

        // 🔹 TEXTE DU BOUTON SELON L'ÉCRAN
        if (mode == MODE_MES_ETUDIANTS) {
            holder.btnAction.setText("Détail");
        } else {
            holder.btnAction.setText("Encadrer");
        }

        holder.btnAction.setOnClickListener(v -> listener.onClick(e));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtSujet, txtEntreprise, txtDates;
        Button btnAction;

        ViewHolder(View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtSujet = itemView.findViewById(R.id.txtSujet);
            txtEntreprise = itemView.findViewById(R.id.txtEntreprise);
            txtDates = itemView.findViewById(R.id.txtDates);
            btnAction = itemView.findViewById(R.id.btnEncadrer); // même bouton
        }
    }
}

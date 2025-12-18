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

    public interface OnEncadrerClick {
        void onEncadrer(EtudiantProjetDTO etudiant);
    }

    private final List<EtudiantProjetDTO> list;
    private final OnEncadrerClick listener;

    public EtudiantAdapter(List<EtudiantProjetDTO> list, OnEncadrerClick listener) {
        this.list = list;
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
        holder.txtNom.setText(e.getNom() + " " + e.getPrenom() + " (" + e.getFiliere() + ")");
        holder.txtSujet.setText("Sujet : " + e.getSujet());

        holder.btnEncadrer.setOnClickListener(v -> listener.onEncadrer(e));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtSujet;
        Button btnEncadrer;

        ViewHolder(View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtSujet = itemView.findViewById(R.id.txtSujet);
            btnEncadrer = itemView.findViewById(R.id.btnEncadrer);
        }
    }
}

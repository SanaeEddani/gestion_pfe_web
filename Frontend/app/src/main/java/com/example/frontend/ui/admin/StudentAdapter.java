package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.model.StudentAdmin;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private final List<StudentAdmin> allStudents;      // liste complète (API)
    private final List<StudentAdmin> displayedStudents; // liste filtrée

    public StudentAdapter(List<StudentAdmin> students) {
        this.allStudents = new ArrayList<>(students);
        this.displayedStudents = new ArrayList<>(students);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, email, filiere, encadrant; // ✅ AJOUT
        ImageView statusIcon;
        ImageButton btnAction;

        ViewHolder(View v) {
            super(v);
            nomPrenom = v.findViewById(R.id.textNomPrenom);
            email = v.findViewById(R.id.textEmail);
            filiere = v.findViewById(R.id.textFiliere);
            encadrant = v.findViewById(R.id.textEncadrant); // ✅ AJOUT
            statusIcon = v.findViewById(R.id.statusIcon);
            btnAction = v.findViewById(R.id.btnAction);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        StudentAdmin s = displayedStudents.get(position);

        holder.nomPrenom.setText(s.nom + " " + s.prenom);
        holder.email.setText("Email: " + s.email);
        holder.filiere.setText("Filière: " + s.filiere);

        if (s.affecte) {
            holder.statusIcon.setImageResource(R.drawable.ic_check_green);
            holder.btnAction.setImageResource(R.drawable.ic_reassign);

            // ✅ ENCADRANT
            if (s.encadrantNom != null && !s.encadrantNom.isEmpty()) {
                holder.encadrant.setText("Encadrant : " + s.encadrantNom);
                holder.encadrant.setVisibility(View.VISIBLE);
            } else {
                holder.encadrant.setVisibility(View.GONE);
            }

        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_cross_red);
            holder.btnAction.setImageResource(R.drawable.ic_assign);
            holder.encadrant.setVisibility(View.GONE); // ✅ IMPORTANT
        }
    }


    @Override
    public int getItemCount() {
        return displayedStudents.size();
    }

    // ===== FILTRAGE =====
    public void filter(String name, String apogee, String status) {
        displayedStudents.clear();

        for (StudentAdmin s : allStudents) {

            boolean matchName =
                    name.isEmpty() ||
                            (s.nom + " " + s.prenom)
                                    .toLowerCase()
                                    .contains(name.toLowerCase());

            boolean matchApogee =
                    apogee.isEmpty() ||
                            (s.apogee != null && s.apogee.contains(apogee));


            boolean matchStatus =
                    status.equals("Tous") ||
                            (status.equals("Affecté") && s.affecte) ||
                            (status.equals("Non affecté") && !s.affecte);

            if (matchName && matchApogee && matchStatus) {
                displayedStudents.add(s);
            }
        }

        notifyDataSetChanged();
    }
}

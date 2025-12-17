package com.example.frontend.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.model.StudentAdmin;
import com.example.frontend.R;
import java.util.List;



public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private final List<StudentAdmin> students;

    public StudentAdapter(List<StudentAdmin> students) {
        this.students = students;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, email, filiere;
        ImageView statusIcon;
        ImageButton btnAction;

        ViewHolder(View v) {
            super(v);
            nomPrenom = v.findViewById(R.id.textNomPrenom);
            email = v.findViewById(R.id.textEmail);
            filiere = v.findViewById(R.id.textFiliere);
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
        StudentAdmin s = students.get(position);

        holder.nomPrenom.setText(s.nom + " " + s.prenom);
        holder.email.setText("Email: " + s.email);
        holder.filiere.setText("Filière: " + s.filiere);

        if (s.affecte) {
            holder.statusIcon.setImageResource(R.drawable.ic_check_green);
            holder.btnAction.setImageResource(R.drawable.ic_reassign);
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_cross_red);
            holder.btnAction.setImageResource(R.drawable.ic_assign);
        }

        holder.btnAction.setOnClickListener(v -> {
            // TODO: ouvrir dialog d’affectation / réaffectation
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}

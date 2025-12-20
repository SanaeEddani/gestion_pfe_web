package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend.R;
import com.example.frontend.model.Remarque;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RemarquesAdapter extends RecyclerView.Adapter<RemarquesAdapter.ViewHolder> {

    private List<Remarque> remarques;
    private SimpleDateFormat dateFormat;

    public RemarquesAdapter(List<Remarque> remarques) {
        this.remarques = remarques;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_remarque, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Remarque remarque = remarques.get(position);

        // Vérifier si les méthodes existent dans votre classe Remarque
        if (remarque != null) {
            holder.tvContenu.setText(remarque.getContenu() != null ? remarque.getContenu() : "");

            String encadrantNom = remarque.getEncadrantNom() != null ?
                    "Par: " + remarque.getEncadrantNom() : "Par: Non spécifié";
            holder.tvEncadrant.setText(encadrantNom);

            if (remarque.getCreatedAt() != null) {
                holder.tvDate.setText(dateFormat.format(remarque.getCreatedAt()));
            } else {
                holder.tvDate.setText("Date non disponible");
            }
        }
    }

    @Override
    public int getItemCount() {
        return remarques != null ? remarques.size() : 0;
    }

    // Classe ViewHolder interne
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContenu, tvEncadrant, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialiser les vues
            tvContenu = itemView.findViewById(R.id.tv_contenu);
            tvEncadrant = itemView.findViewById(R.id.tv_encadrant);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
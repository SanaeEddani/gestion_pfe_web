package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.DocumentsActivity;
import com.example.frontend.R;
import com.example.frontend.model.Document;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    Context context;
    List<Document> list;

    public DocumentAdapter(Context context, List<Document> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document doc = list.get(position);

        holder.txtNom.setText(doc.nomDocument);
        holder.txtDate.setText(doc.dateUpload);

        if ("pdf".equalsIgnoreCase(doc.format))
            holder.iconFormat.setImageResource(R.drawable.ic_pdf);
        else if ("ppt".equalsIgnoreCase(doc.format) || "pptx".equalsIgnoreCase(doc.format))
            holder.iconFormat.setImageResource(R.drawable.ic_ppt);
        else
            holder.iconFormat.setImageResource(R.drawable.ic_file);

        holder.remarquesContainer.removeAllViews();

        if (doc.remarques != null) {
            for (Document.Remarque r : doc.remarques) {
                TextView tv = new TextView(context);
                tv.setText("- " + r.contenu);
                tv.setTextSize(12);
                tv.setTextColor(Color.DKGRAY);
                holder.remarquesContainer.addView(tv);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "http://IP:8080/api/documents/download/" + doc.chemin
            ));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNom, txtDate;
        ImageView iconFormat;
        LinearLayout remarquesContainer;

        ViewHolder(View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtDate = itemView.findViewById(R.id.txtDate);
            iconFormat = itemView.findViewById(R.id.iconFormat);
            remarquesContainer = itemView.findViewById(R.id.remarquesContainer);
        }
    }
}

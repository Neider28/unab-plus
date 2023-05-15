package com.example.unabplus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorPersonalizadoComentario extends RecyclerView.Adapter<AdaptadorPersonalizadoComentario.ViewHolder> {

    private ArrayList<Comentario> comentarioList;
    private OnItemClickListener onItemClickListener;

    public AdaptadorPersonalizadoComentario(ArrayList<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @NonNull
    @Override
    public AdaptadorPersonalizadoComentario.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(miView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizadoComentario.ViewHolder holder, int position) {
        Comentario comentario = comentarioList.get(position);
        holder.enlazar(comentario);
    }

    @Override
    public int getItemCount() {
        return comentarioList.size();
    }

    public void setListaComentarios(ArrayList<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsuario, tvComentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsuario = itemView.findViewById(R.id.tv_usuario_comentario);
            tvComentario = itemView.findViewById(R.id.tv_comentario);
        }

        public void enlazar(Comentario comentario) {
            tvUsuario.setText(comentario.getUsuario().toString());
            tvComentario.setText(comentario.getComentario().toString());

            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.OnItemClick(comentario, getAdapterPosition());
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Comentario comentario, int position);
    }
}

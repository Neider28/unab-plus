package com.example.unabplus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorPersonalizadoCategorias extends RecyclerView.Adapter<AdaptadorPersonalizadoCategorias.ViewHolder> {

    private ArrayList<Categoria> categoryList;
    private OnItemClickListener onItemClickListener;

    public AdaptadorPersonalizadoCategorias(ArrayList<Categoria> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public AdaptadorPersonalizadoCategorias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(miView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizadoCategorias.ViewHolder holder, int position) {
        Categoria categoria = categoryList.get(position);
        holder.enlazar(categoria);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setListaCategorias(ArrayList<Categoria> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoria = itemView.findViewById(R.id.tv_categoria);
        }

        public void enlazar(Categoria categoria) {
            tvCategoria.setText(categoria.getNombre().toString());

            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.OnItemClick(categoria, getAdapterPosition());
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Categoria categoria, int position);
    }
}

package com.example.unabplus;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class AdaptadorPersonalizado extends RecyclerView.Adapter<AdaptadorPersonalizado.ViewHolder> {

    private ArrayList<Movie> movieList;
    private OnItemClickListener onItemClickListener;

    public AdaptadorPersonalizado(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdaptadorPersonalizado.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(miView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizado.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.enlazar(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setListaMovie(ArrayList<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.iv_movie);
        }

        public void enlazar(Movie movie) {
            Transformation roundedTransformationBuilder = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth = source.getWidth();
                    int targetHeight = source.getHeight();

                    Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, source.getConfig());
                    Canvas canvas = new Canvas(result);
                    Paint paint  = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    RectF rectF = new RectF(0f, 0f, targetWidth, targetHeight);
                    float radius = targetWidth / 8f;
                    canvas.drawRoundRect(rectF, radius, radius, paint);

                    if (source != result) {
                        source.recycle();
                    }

                    return result;
                }

                @Override
                public String key() {
                    return "rounded";
                }
            };

            Picasso.get()
                    .load(movie.getPoster())
                    .transform(roundedTransformationBuilder)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivMovie);

            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.OnItemClick(movie, getAdapterPosition());
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Movie movie, int position);
    }
}

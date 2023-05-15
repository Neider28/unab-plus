package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private TextView tvCategoria;
    private RecyclerView rvMoviesCategory;
    private AdaptadorPersonalizado miAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    Categoria categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tvCategoria = findViewById(R.id.tv_categoria_detalle);
        rvMoviesCategory = findViewById(R.id.rv_movies_category);

        categoria = (Categoria) getIntent().getSerializableExtra("categoria");
        tvCategoria.setText(categoria.getNombre());

        cargarDatos();

        miAdapter = new AdaptadorPersonalizado(movieList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(CategoryActivity.this, DetalleActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        rvMoviesCategory.setAdapter(miAdapter);
        rvMoviesCategory.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void cargarDatos() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("trending_movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Movie movieGet = document.toObject(Movie.class);

                        if(movieGet.getTipo().contains(categoria.getNombre())) {
                            movieList.add(movieGet);
                        }
                    }

                    miAdapter.setListaMovie(movieList);
                } else {
                    Toast.makeText(CategoryActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
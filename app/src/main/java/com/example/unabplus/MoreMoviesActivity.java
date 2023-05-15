package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoreMoviesActivity extends AppCompatActivity {
    private RecyclerView rvMoreMovies;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private AdaptadorPersonalizado miAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_movies);

        rvMoreMovies = findViewById(R.id.rv_more_movies);

        cargarDatos();

        miAdapter = new AdaptadorPersonalizado(movieList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(MoreMoviesActivity.this, DetalleActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        rvMoreMovies.setAdapter(miAdapter);
        rvMoreMovies.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void cargarDatos() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("trending_movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Movie movieGet = document.toObject(Movie.class);
                        movieList.add(movieGet);
                    }

                    miAdapter.setListaMovie(movieList);
                } else {
                    Toast.makeText(MoreMoviesActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickVerMas(View view) {
        Intent miIntent = new Intent(MoreMoviesActivity.this, MainActivity.class);
        startActivity(miIntent);
    }
}
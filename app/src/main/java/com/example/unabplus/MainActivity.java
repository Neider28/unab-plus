package com.example.unabplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;



import java.util.ArrayList;

public class MainActivity<MovieResponse> extends AppCompatActivity {

    private ArrayList<Movie> movieList = new ArrayList<>();
    private RecyclerView rvMovies;
    private AdaptadorPersonalizado miAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarDatos();

        rvMovies = findViewById(R.id.rv_movies);

        miAdapter = new AdaptadorPersonalizado(movieList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
                intent.putExtra("movie", (CharSequence) movie);
                startActivity(intent);
            }
        });
        rvMovies.setAdapter(miAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    public void cargarDatos() {
        Movie movie = new Movie();
        movie.setId(22);
        movie.setTitulo("jdijdj");
        movie.setSinopsis("hdijdjd");
        movie.setImagen("https://dmn-dallas-news-prod.cdn.arcpublishing.com/resizer/P5emkNBerxsao9beKBkn26jcI_8=/1660x934/smart/filters:no_upscale()/cloudfront-us-east-1.images.arcpublishing.com/dmn/NHR5NN4PW3G6X6DKQBZMUMVDDE.jpg");

        movieList.add(movie);
    }

    public void clickPerfil(View view) {
        Intent miIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(miIntent);
    }
}
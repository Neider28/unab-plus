package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private String busqueda;
    private RecyclerView rvBusqueda;
    private EditText etSearch;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private AdaptadorPersonalizado miAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        busqueda = getIntent().getStringExtra("busqueda").toLowerCase();
        rvBusqueda = findViewById(R.id.rv_busqueda);
        etSearch = findViewById(R.id.et_search);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!etSearch.getText().toString().equals("")) {
                        movieList.clear();
                        cargarBusqueda(etSearch.getText().toString().toLowerCase());

                        miAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }
        });

        cargarBusqueda(busqueda);

        miAdapter = new AdaptadorPersonalizado(movieList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(SearchActivity.this, DetalleActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        rvBusqueda.setAdapter(miAdapter);
        rvBusqueda.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void cargarBusqueda(String text) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("trending_movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Movie movieGet = document.toObject(Movie.class);
                        String nombre = movieGet.getNombre().toLowerCase();
                        if (nombre.contains(text)) {
                            movieList.add(movieGet);
                        }
                    }

                    if (movieList.isEmpty()) {
                        Toast.makeText(SearchActivity.this, R.string.txt_no_resultados, Toast.LENGTH_SHORT).show();
                    }

                    miAdapter.setListaMovie(movieList);
                } else {
                    Toast.makeText(SearchActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickBuscar(View view) {
        if (!etSearch.getText().toString().equals("")) {
            movieList.clear();
            cargarBusqueda(etSearch.getText().toString().toLowerCase());

            miAdapter.notifyDataSetChanged();
        }
    }

    public void clickAtras(View view) {
        Intent miIntent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(miIntent);
    }
}
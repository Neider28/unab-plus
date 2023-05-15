package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movieList = new ArrayList<>();
    private ArrayList<Categoria> categoryList = new ArrayList<>();
    private RecyclerView rvMovies, rvCategorias;
    private AdaptadorPersonalizado miAdapter;
    private AdaptadorPersonalizadoCategorias miAdapterCategorias;
    private EditText etBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarDatos();
        cargarCategorias();

        rvMovies = findViewById(R.id.rv_movies);
        rvCategorias = findViewById(R.id.rv_categorias);
        etBuscar = findViewById(R.id.et_buscar);
        etBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!etBuscar.getText().toString().equals("")) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("busqueda", etBuscar.getText().toString());
                        startActivity(intent);
                        etBuscar.setText("");

                        miAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }
        });

        miAdapter = new AdaptadorPersonalizado(movieList);
        miAdapterCategorias = new AdaptadorPersonalizadoCategorias(categoryList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        miAdapterCategorias.setOnItemClickListener(new AdaptadorPersonalizadoCategorias.OnItemClickListener() {
            @Override
            public void OnItemClick(Categoria categoria, int position) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });

        rvMovies.setAdapter(miAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvCategorias.setAdapter(miAdapterCategorias);
        rvCategorias.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void cargarDatos() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("categorias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Categoria categoria = document.toObject(Categoria.class);
                        categoryList.add(categoria);
                    }

                    miAdapterCategorias.setListaCategorias(categoryList);
                } else {
                    Toast.makeText(MainActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cargarCategorias() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("trending_movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Movie movieGet = document.toObject(Movie.class);
                        movieGet.setId(document.getId());
                        movieList.add(movieGet);
                    }

                    miAdapter.setListaMovie(movieList);
                } else {
                    Toast.makeText(MainActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickPerfil(View view) {
        Intent miIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(miIntent);
    }

    public void clickVerMas(View view) {
        Intent miIntent = new Intent(MainActivity.this, MoreMoviesActivity.class);
        startActivity(miIntent);
    }

    public void clickBuscar(View view) {
        if (!etBuscar.getText().toString().equals("")) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("busqueda", etBuscar.getText().toString());
            startActivity(intent);
            etBuscar.setText("");
        }
    }
}
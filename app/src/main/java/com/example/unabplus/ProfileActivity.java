package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUser;
    private RecyclerView rvFavoritos;
    private AdaptadorPersonalizado miAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    SharedPreferences misPreferencias;
    Perfil getPerfil;

    private List<String> favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUser = findViewById(R.id.tv_user);
        rvFavoritos = findViewById(R.id.rv_favoritos);

        misPreferencias = getSharedPreferences("unab_plus", MODE_PRIVATE);
        tvUser.setText(misPreferencias.getString("usuario","user not found"));

        cargarFavoritos();

        miAdapter = new AdaptadorPersonalizado(movieList);

        miAdapter.setOnItemClickListener(new AdaptadorPersonalizado.OnItemClickListener() {
            @Override
            public void OnItemClick(Movie movie, int position) {
                Intent intent = new Intent(ProfileActivity.this, DetalleActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        rvFavoritos.setAdapter(miAdapter);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        movieList.clear();
        cargarFavoritos();
    }

    public void cargarFavoritos() {
        Gson gson = new Gson();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("usuarios").whereEqualTo("user", misPreferencias.getString("usuario","user not found")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        getPerfil = document.toObject(Perfil.class);
                        getPerfil.setId(document.getId());

                        favoritos = (List<String>) document.get("favoritos");
                        if(favoritos == null) {
                            favoritos = new ArrayList<>();
                        } else {
                            for (String favorito : favoritos) {
                                Movie movie = gson.fromJson(favorito, Movie.class);
                                movieList.add(movie);
                            }
                        }
                    }

                    miAdapter.setListaMovie(movieList);
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickCerrarSesion(View view) {
        SharedPreferences misPreferencias = getSharedPreferences("unab_plus", MODE_PRIVATE);
        SharedPreferences.Editor miEditor = misPreferencias.edit();
        miEditor.clear();
        miEditor.apply();

        startActivity(new Intent(getBaseContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
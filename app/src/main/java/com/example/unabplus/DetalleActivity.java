package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {

    private TextView tvTitulo, tvPuntaje, tvDescripcion, tvComentario;
    private ImageView ivMovie;
    private ImageButton ibFavorito;
    Movie movie;
    Perfil getPerfil;

    private List<String> favoritos;
    private List<String> comentarios;
    private ArrayList<Categoria> categoryList = new ArrayList<>();

    private RecyclerView rvComentarios, rvCategorias;
    private AdaptadorPersonalizadoComentario miAdapter;
    private AdaptadorPersonalizadoCategorias miAdapterCategorias;
    private ArrayList<Comentario> comentariosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvTitulo = findViewById(R.id.tv_titulo_detalle);
        tvPuntaje = findViewById(R.id.tv_puntaje_detalle);
        tvDescripcion = findViewById(R.id.tv_descripcion_detalle);
        ivMovie = findViewById(R.id.iv_movie_detalle);
        rvComentarios = findViewById(R.id.rv_comentarios);
        tvComentario = findViewById(R.id.tv_comentario_enviar);
        tvComentario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clickEnviarComentario();
                }

                return false;
            }
        });

        rvCategorias = findViewById(R.id.rv_categorias_detalle);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        tvTitulo.setText(movie.getNombre());
        tvPuntaje.setText(Float.toString(movie.getPuntaje()));
        tvDescripcion.setText(movie.getDescripcion());
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
                .error(R.drawable.ic_launcher_background)
                .into(ivMovie);
        cargarComentarios();

        miAdapter = new AdaptadorPersonalizadoComentario(comentariosList);
        miAdapterCategorias = new AdaptadorPersonalizadoCategorias(categoryList);

        for (String categoria : movie.getTipo()) {
            Categoria newCategoria = new Categoria();
            newCategoria.setNombre(categoria);

            categoryList.add(newCategoria);
        }

        miAdapterCategorias.setOnItemClickListener(new AdaptadorPersonalizadoCategorias.OnItemClickListener() {
            @Override
            public void OnItemClick(Categoria categoria, int position) {
                Intent intent = new Intent(DetalleActivity.this, CategoryActivity.class);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });

        miAdapter.setListaComentarios(comentariosList);
        rvComentarios.setAdapter(miAdapter);
        rvComentarios.setLayoutManager(new LinearLayoutManager(this));

        miAdapterCategorias.setListaCategorias(categoryList);
        rvCategorias.setAdapter(miAdapterCategorias);
        rvCategorias.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void clickFavorito(View view) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Gson gson = new Gson();

        SharedPreferences misPreferencias = getSharedPreferences("unab_plus", MODE_PRIVATE);
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
                            if(favoritos.contains(gson.toJson(movie))) {
                                favoritos.remove(gson.toJson(movie));
                                Toast.makeText(DetalleActivity.this, R.string.txt_removido, Toast.LENGTH_SHORT).show();
                            } else {
                                favoritos.add(gson.toJson(movie));
                                Toast.makeText(DetalleActivity.this, R.string.txt_agregado_a_favoritos, Toast.LENGTH_SHORT).show();
                            }
                        }

                        firestore.collection("usuarios").document(getPerfil.getId()).update("favoritos", favoritos);
                    }
                } else {
                    Toast.makeText(DetalleActivity.this, R.string.txt_ups, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cargarComentarios() {
        Gson gson = new Gson();

        if(movie.getComentarios() == null) {
            movie.setComentarios(new ArrayList<>());
        } else {
            for (String comentario : movie.getComentarios()) {
                Comentario getComentario = gson.fromJson(comentario, Comentario.class);
                comentariosList.add(getComentario);
            }
        }
    }

    public void clickEnviar(View view) {
        clickEnviarComentario();
    }

    public void clickEnviarComentario() {
        SharedPreferences misPreferencias = getSharedPreferences("unab_plus", MODE_PRIVATE);
        if (!tvComentario.getText().toString().equals("")) {
            Comentario newComentario = new Comentario();
            newComentario.setUsuario(misPreferencias.getString("usuario","user not found"));
            newComentario.setComentario(tvComentario.getText().toString());

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            Gson gson = new Gson();
            comentarios = movie.getComentarios();
            comentarios.add(gson.toJson(newComentario));
            comentariosList.add(newComentario);

            firestore.collection("trending_movies").document(movie.getId()).update("comentarios", comentarios);
            Toast.makeText(DetalleActivity.this, R.string.txt_gracias_por_compartir_tu_opinion, Toast.LENGTH_SHORT).show();
            tvComentario.setText("");
            miAdapter.notifyDataSetChanged();
        }
    }
}
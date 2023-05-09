package com.example.unabplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.et_user);
        etPassword = findViewById(R.id.et_password);
    }

    public void clickIniciarSesion(View view) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (etUser.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show();
        } else {
            firestore.collection("usuarios").whereEqualTo("user", etUser.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Perfil getPerfil = document.toObject(Perfil.class);

                            if (getPerfil.getPassword().equals(etPassword.getText().toString())) {
                                Intent miIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(miIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void clickRegistrarse(View view) {
        Intent miIntent = new Intent(this, SignupActivity.class);
        startActivity(miIntent);
    }
}
package com.example.unabplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText etUser, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUser = findViewById(R.id.et_user_signup);
        etPassword = findViewById(R.id.et_password_signup);
    }

    public void clickRegistrarse(View view) {
        Perfil newPerfil = new Perfil();
        newPerfil.setUser(etUser.getText().toString());
        newPerfil.setPassword(etPassword.getText().toString());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (etUser.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show();
        } else {
            firestore.collection("usuarios").add(newPerfil);
            Toast.makeText(this, "Perfil creado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
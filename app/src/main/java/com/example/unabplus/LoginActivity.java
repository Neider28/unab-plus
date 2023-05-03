package com.example.unabplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickIniciarSesion(View view) {
        Intent miIntent = new Intent(this, MainActivity.class);
        startActivity(miIntent);
    }

    public void clickRegistrarse(View view) {
        Intent miIntent = new Intent(this, SignupActivity.class);
        startActivity(miIntent);
    }
}
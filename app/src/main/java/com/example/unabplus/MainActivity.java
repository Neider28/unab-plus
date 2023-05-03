package com.example.unabplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickPerfil(View view) {
        Intent inte = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(inte);
    }
/*
    public void clickVerMas(View view) {
        Intent miIntent = new Intent(this, MoreMoviesActivity.class);
        startActivity(miIntent);
    }*/
}
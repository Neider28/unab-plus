package com.example.unabplus;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    private String id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("poster")
    private String poster;
    @SerializedName("puntaje")
    private Float puntaje;
    @SerializedName("tipo")
    private ArrayList<String> tipo;
    @SerializedName("comentarios")
    private List<String> comentarios;

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPoster() {
        return poster;
    }

    public Float getPuntaje() {
        return puntaje;
    }

    public ArrayList<String> getTipo() {
        return tipo;
    }

    public List<String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

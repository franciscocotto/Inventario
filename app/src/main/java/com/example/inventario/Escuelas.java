package com.example.inventario;

public class Escuelas {
    int id_escuela;
    String escuela;

    public Escuelas(){}

    public Escuelas(int id_escuela, String escuela){
        this.id_escuela = id_escuela;
        this.escuela = escuela;
    }

    public int getId_escuela() {
        return id_escuela;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setId_escuela(int id_escuela) {
        this.id_escuela = id_escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }
}

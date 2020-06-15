package com.example.inventario.ui;

import com.example.inventario.Documentos;

public class Docentes {
    private int id_docente;
    private String primerNombre, segundoNombre, primerApellido, segundoApellido;

    public Docentes(){    }

    public Docentes(int id_docente, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido){
        this.id_docente = id_docente;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
    }

    public int getId_docente() {
        return id_docente;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
}

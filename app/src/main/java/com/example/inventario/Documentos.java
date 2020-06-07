package com.example.inventario;

import java.util.Date;

public class Documentos {
    private int id_documento, id_categoria, id_idioma;
    private String titulo, isbn, tema, subtitulo, autor, editorial,descripcion;
    private Date fecha_ingreso;

    public Documentos() {
    }

    public Documentos(int id_documento, int id_categoria, int id_idioma, String titulo, String isbn, String tema, String subtitulo, String autor, String editorial, String descripcion, Date fecha_ingreso) {
        this.id_documento = id_documento;
        this.id_categoria = id_categoria;
        this.id_idioma = id_idioma;
        this.titulo = titulo;
        this.isbn = isbn;
        this.tema = tema;
        this.subtitulo = subtitulo;
        this.autor = autor;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_idioma() {
        return id_idioma;
    }

    public void setId_idioma(int id_idioma) {
        this.id_idioma = id_idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}


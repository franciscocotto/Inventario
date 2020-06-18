package com.example.inventario;

public class InventarioDocumentos extends Documentos {

    private int id_bien, id_area, id_docente, id_motivo, todo_ciclo, es_definitivo, id_escuela;
    private String fecha;

    public InventarioDocumentos(){}

    public InventarioDocumentos(int id_documento, int id_bien, int id_estado, String titulo, String isbn){
        this.id_documento = id_documento;
        this.id_bien = id_bien;
        this.id_estado = id_estado;
        this.titulo = titulo;
        this.isbn = isbn;
    }

    //Setters


    public void setId_bien(int id_bien) {
        this.id_bien = id_bien;
    }

    public void setId_area(int id_area) {
        this.id_area = id_area;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public void setTodo_ciclo(int todo_ciclo) {
        this.todo_ciclo = todo_ciclo;
    }

    public void setEs_definitivo(int es_definitivo) {
        this.es_definitivo = es_definitivo;
    }

    //Getters
    public int getId_bien() {
        return id_bien;
    }

    public int getId_area() {
        return id_area;
    }

    public int getId_docente() {
        return id_docente;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public int getTodo_ciclo() {
        return todo_ciclo;
    }

    public int getEs_definitivo() {
        return es_definitivo;
    }
}

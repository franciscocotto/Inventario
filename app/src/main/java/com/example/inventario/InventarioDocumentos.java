package com.example.inventario;

public class InventarioDocumentos extends Documentos {

    int id_bien, id_area, id_docente, id_motivo, todo_ciclo, es_definitivo;

    public InventarioDocumentos(){}

    public InventarioDocumentos(int id_documento, int id_estado, String titulo, String isbn){
        this.id_documento = id_documento;
        this.id_estado = id_estado;
        this.titulo = titulo;
        this.isbn = isbn;
    }

}

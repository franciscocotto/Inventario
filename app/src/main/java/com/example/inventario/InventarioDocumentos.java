package com.example.inventario;

public class InventarioDocumentos extends Documentos {

    public InventarioDocumentos(){}

    public InventarioDocumentos(int id_documento, int id_categoria, int id_estado, int id_idioma, String titulo, String isbn, String tema, String autor){
        this.id_documento=id_documento;
        this.id_categoria = id_categoria;
        this.id_estado = id_estado;
        this.id_idioma = id_idioma;
        this.titulo = titulo;
        this.isbn = isbn;
        this.tema = tema;
        this.autor = autor;
    }

}

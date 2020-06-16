package com.example.inventario;

public class InventarioDocumentos extends Documentos {

    static String estado;

    public InventarioDocumentos(){}

    public InventarioDocumentos(int id_documento, int id_estado, String titulo, String isbn){
        this.id_documento=id_documento;
        this.id_estado = id_estado;
        this.titulo = titulo;
        this.isbn = isbn;
        InventarioDocumentos.estado = String.valueOf(this.id_estado);
    }

    public static String getEstado() {
        return estado;
    }

    public static void setEstado(String estado) {
        InventarioDocumentos.estado = estado;
    }
}

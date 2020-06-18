package com.example.inventario;

public class Documentos_Consulta {
    protected int  id_documento,  id_estado;
    protected String id_categoria, id_idioma, titulo, isbn, tema, subtitulo, autor, editorial, descripcion, fecha_ingreso, palabras;
    protected static String isbnS;
    protected static String categoria;
    protected static String idioma;
    protected static Integer fragmento;


    public Documentos_Consulta() {
    }

    public Documentos_Consulta(
            String id_categoria,
            String id_idioma,
            int id_estado,
            String titulo,
            String isbn,
            String tema,
            String subtitulo,
            String autor,
            String editorial,
            String descripcion,
            String fecha_ingreso,
            String palabras) {

        this.id_categoria = id_categoria;
        this.id_idioma = id_idioma;
        this.id_estado = id_estado;
        this.titulo = titulo;
        this.isbn = isbn;
        this.tema = tema;
        this.subtitulo = subtitulo;
        this.autor = autor;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.fecha_ingreso = fecha_ingreso;
        this.palabras = palabras;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(String id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getId_idioma() {
        return id_idioma;
    }

    public void setId_idioma(String id_idioma) {
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

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getPalabras() {
        return palabras;
    }

    public void setPalabras(String palabras) {
        this.palabras = palabras;
    }

    public static void setIsbnS(String isbn){
        isbnS = isbn;
    }
    public static String getIsbnS(){
        return isbnS;
    }

    public static void setCategoria(String categoria1){
        categoria = categoria1;
    }
    public static String getcategoria(){
        return categoria;
    }

    public static void setidioma(String idioma1){
        idioma = idioma1;
    }
    public static String getidioma(){
        return idioma;
    }

    public static void setFragmento(int frag){ fragmento = frag;}
    public static int getFragmento(){
        return fragmento;
    }
}

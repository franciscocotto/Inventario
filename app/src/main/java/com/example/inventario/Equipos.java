package com.example.inventario;

public class Equipos {
    static int fragmento;
    static String num_inv;
    private int  id_equipo, id_categoria, id_marca, id_estado, cantidad_inicial;
    private Double precio;
    private String num_inventario, modelo, serie, fecha_compra, descripcion, fecha_ingreso;
    private static String numInventario;

    public Equipos() {
    }

    public Equipos(int id_categoria, int id_marca, int id_estado, int cantidad_inicial, Double precio, String num_inventario, String modelo, String serie, String fecha_compra, String descripcion, String fecha_ingreso) {
        this.id_categoria = id_categoria;
        this.id_marca = id_marca;
        this.id_estado = id_estado;
        this.cantidad_inicial = cantidad_inicial;
        this.precio = precio;
        this.num_inventario = num_inventario;
        this.modelo = modelo;
        this.serie = serie;
        this.fecha_compra = fecha_compra;
        this.descripcion = descripcion;
        this.fecha_ingreso = fecha_ingreso;
    }

    public Equipos(int id_equipo, String num_inventario, String modelo, String serie, String descripcion){
        this.id_equipo = id_equipo;
        this.num_inventario = num_inventario;
        this.modelo = modelo;
        this.serie = serie;
        this.descripcion = descripcion;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getCantidad_inicial() {
        return cantidad_inicial;
    }

    public void setCantidad_inicial(int cantidad_inicial) {
        this.cantidad_inicial = cantidad_inicial;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNum_inventario() {
        return num_inventario;
    }

    public void setNum_inventario(String num_inventario) {
        this.num_inventario = num_inventario;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
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

    public static int getFragmento() {
        return fragmento;
    }

    public static void setFragmento(int fragmento) {
        Equipos.fragmento = fragmento;
    }

    public static String getNum_inv() {
        return num_inv;
    }

    public static void setNum_inv(String num_inv) {
        Equipos.num_inv = num_inv;
    }

        public static String getNumInventario() {
        return numInventario;
    }

    public static void setNumInventario(String numInventario) {
        Equipos.numInventario = numInventario;
    }
}

package com.example.inventario;

public class Areas {
    int id_area;
    String area;

    public Areas(){}

    public Areas(int id_area, String area){
        this.id_area = id_area;
        this.area = area;
    }

    public int getId_area() {
        return id_area;
    }

    public String getArea() {
        return area;
    }


    public void setId_area(int id_area) {
        this.id_area = id_area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

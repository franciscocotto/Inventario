package com.example.inventario;

public class Motivos {
    int id_motivos;
    String motivos;

    public Motivos(){}

    public Motivos(int id_motivos, String motivos) {
        this.id_motivos = id_motivos;
        this.motivos = motivos;
    }

    public int getId_motivos() {
        return id_motivos;
    }

    public String getMotivos() {
        return motivos;
    }

    public void setId_motivos(int id_motivos) {
        this.id_motivos = id_motivos;
    }

    public void setMotivos(String motivos) {
        this.motivos = motivos;
    }
}

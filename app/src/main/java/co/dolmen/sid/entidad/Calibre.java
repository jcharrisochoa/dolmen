package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Calibre implements Serializable {
    private int id_calibre;
    private  String descripcion;

    public Calibre(){}

    public Calibre(int id_calibre, String descripcion) {
        this.id_calibre = id_calibre;
        this.descripcion = descripcion;
    }

    public int getId_calibre() {
        return id_calibre;
    }

    public void setId_calibre(int id_calibre) {
        this.id_calibre = id_calibre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

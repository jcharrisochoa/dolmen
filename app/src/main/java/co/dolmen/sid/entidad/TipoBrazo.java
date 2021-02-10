package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoBrazo implements Serializable {
    int idTipoBrazo;
    String descripcion;

    public TipoBrazo(){}

    public TipoBrazo(int idTipoBrazo, String descripcion) {
        this.idTipoBrazo = idTipoBrazo;
        this.descripcion = descripcion;
    }

    public int getidTipoBrazo() {
        return idTipoBrazo;
    }

    public void setidTipoBrazo(int idTipoBrazo) {
        this.idTipoBrazo = idTipoBrazo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

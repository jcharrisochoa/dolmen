package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Mobiliario extends Tipologia implements Serializable {
    int idMobiliario;
    String descripcionMobiliario;
    Tipologia tipologia;

    public Mobiliario(){
        super();
    }
    public Mobiliario(int idMobiliario,String descripcionMobiliario){
        super();
        this.idMobiliario = idMobiliario;
        this.descripcionMobiliario = descripcionMobiliario;
    }

    public int getIdMobiliario() {
        return idMobiliario;
    }

    public void setIdMobiliario(int idMobiliario) {
        this.idMobiliario = idMobiliario;
    }

    public String getDescripcionMobiliario() {
        return descripcionMobiliario;
    }

    public void setDescripcionMobiliario(String descripcionMobiliario) {
        this.descripcionMobiliario = descripcionMobiliario;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }
}

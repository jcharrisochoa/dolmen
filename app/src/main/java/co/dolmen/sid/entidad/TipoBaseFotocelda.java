package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoBaseFotocelda implements Serializable {
    int idTipoBaseFotocelda;
    String descripcion;

    public TipoBaseFotocelda(){

    }

    public TipoBaseFotocelda(int idTipoBaseFotocelda, String descripcion) {
        this.idTipoBaseFotocelda = idTipoBaseFotocelda;
        this.descripcion = descripcion;
    }

    public int getidTipoBaseFotocelda() {
        return idTipoBaseFotocelda;
    }

    public void setidTipoBaseFotocelda(int idTipoBaseFotocelda) {
        this.idTipoBaseFotocelda = idTipoBaseFotocelda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

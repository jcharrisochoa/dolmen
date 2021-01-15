package co.dolmen.sid.entidad;

public class TipoBrazo {
    int id_tipo_brazo;
    String descripcion;

    public TipoBrazo(){}

    public TipoBrazo(int id_tipo_brazo, String descripcion) {
        this.id_tipo_brazo = id_tipo_brazo;
        this.descripcion = descripcion;
    }

    public int getId_tipo_brazo() {
        return id_tipo_brazo;
    }

    public void setId_tipo_brazo(int id_tipo_brazo) {
        this.id_tipo_brazo = id_tipo_brazo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

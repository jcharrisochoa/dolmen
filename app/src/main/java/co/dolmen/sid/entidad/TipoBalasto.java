package co.dolmen.sid.entidad;

public class TipoBalasto {
    int id_tipo_balasto;
    String descripcion;

    public TipoBalasto(){}

    public TipoBalasto(int id_tipo_balasto, String descripcion) {
        this.id_tipo_balasto = id_tipo_balasto;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id_tipo_balasto;
    }

    public void setId(int id_tipo_balasto) {
        this.id_tipo_balasto = id_tipo_balasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

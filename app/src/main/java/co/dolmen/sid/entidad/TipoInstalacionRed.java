package co.dolmen.sid.entidad;

public class TipoInstalacionRed {
    int id_tipo_instalacion_red;
    String descripcion;

    public TipoInstalacionRed(){}

    public TipoInstalacionRed(int id_tipo_instalacion_red, String descripcion) {
        this.id_tipo_instalacion_red = id_tipo_instalacion_red;
        this.descripcion = descripcion;
    }

    public int getId_tipo_instalacion_red() {
        return id_tipo_instalacion_red;
    }

    public void setId_tipo_instalacion_red(int id_tipo_instalacion_red) {
        this.id_tipo_instalacion_red = id_tipo_instalacion_red;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

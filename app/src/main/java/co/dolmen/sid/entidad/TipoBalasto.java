package co.dolmen.sid.entidad;

public class TipoBalasto {
    int idTipoBalasto;
    String descripcion;

    public TipoBalasto(){}

    public TipoBalasto(int idTipoBalasto, String descripcion) {
        this.idTipoBalasto = idTipoBalasto;
        this.descripcion = descripcion;
    }

    public int getIdTipoBalasto() {
        return idTipoBalasto;
    }

    public void setIdTipoBalasto(int idTipoBalasto) {
        this.idTipoBalasto = idTipoBalasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package co.dolmen.sid.entidad;

public class TipoInstalacionRed {
    int idTipoInstalacionRed;
    String descripcion;

    public TipoInstalacionRed(){}

    public TipoInstalacionRed(int idTipoInstalacionRed, String descripcion) {
        this.idTipoInstalacionRed = idTipoInstalacionRed;
        this.descripcion = descripcion;
    }

    public int getidTipoInstalacionRed() {
        return idTipoInstalacionRed;
    }

    public void setidTipoInstalacionRed(int idTipoInstalacionRed) {
        this.idTipoInstalacionRed = idTipoInstalacionRed;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

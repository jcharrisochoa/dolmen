package co.dolmen.sid.entidad;

import java.io.Serializable;

public class TipoInstalacionRed  implements Serializable {
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

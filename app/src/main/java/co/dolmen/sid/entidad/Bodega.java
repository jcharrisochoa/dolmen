package co.dolmen.sid.entidad;

public class Bodega {
    int idBodega;
    String descripcion;

    public Bodega(){}

    public Bodega(int idBodega, String descripcion) {
        this.idBodega = idBodega;
        this.descripcion = descripcion;
    }

    public int getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(int idBodega) {
        this.idBodega = idBodega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

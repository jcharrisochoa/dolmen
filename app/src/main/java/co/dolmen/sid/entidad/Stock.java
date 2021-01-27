package co.dolmen.sid.entidad;

import java.io.Serializable;

public class Stock implements Serializable {
    Bodega bodega;
    CentroCosto centroCosto;
    Articulo articulo;
    TipoStock tipoStock;
    float cantidad;

    public Stock(){}

    public Stock(Bodega bodega, CentroCosto centroCosto, Articulo articulo, TipoStock tipoStock, float cantidad) {
        this.bodega = bodega;
        this.centroCosto = centroCosto;
        this.articulo = articulo;
        this.tipoStock = tipoStock;
        this.cantidad = cantidad;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public CentroCosto getCentroCosto() {
        return centroCosto;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public TipoStock getTipoStock() {
        return tipoStock;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad){
        this.cantidad = cantidad;
    }

}

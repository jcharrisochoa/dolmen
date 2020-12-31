package co.dolmen.sid.entidad;

public class Stock {
    Bodega bodega;
    CentroCosto centroCosto;
    Articulo articulo;
    TipoStock tipoStock;
    double cantidad;

    public Stock(){}

    public Stock(Bodega bodega, CentroCosto centroCosto, Articulo articulo, TipoStock tipoStock, double cantidad) {
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad){
        this.cantidad = cantidad;
    }

}

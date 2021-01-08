package co.dolmen.sid.entidad;

import java.io.Serializable;

public class MovimientoArticulo implements Serializable {
    private int id_articulo;
    private int id_tipo_stock;
    private double cantidad;
    private String articulo;
    private String tipo_stock;
    private String movimiento;

    public MovimientoArticulo(){

    }

    public MovimientoArticulo(int id_articulo, int id_tipo_stock, double cantidad, String articulo, String tipo_stock, String movimiento) {
        this.id_articulo = id_articulo;
        this.id_tipo_stock = id_tipo_stock;
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.tipo_stock = tipo_stock;
        this.movimiento = movimiento;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public int getId_tipo_stock() {
        return id_tipo_stock;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getArticulo() {
        return articulo;
    }

    public String getTipo_stock() {
        return tipo_stock;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}

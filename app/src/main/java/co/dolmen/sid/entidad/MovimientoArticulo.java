package co.dolmen.sid.entidad;

import java.io.Serializable;

public class MovimientoArticulo implements Serializable {
    private int id_articulo;
    private int id_tipo_stock;
    private int id_bodega;
    private int id_centro_costo;
    private int id_actividad;
    private float cantidad;
    private String articulo;
    private String tipo_stock;
    private String movimiento;

    public MovimientoArticulo(){

    }

    public MovimientoArticulo(int id_actividad,int id_articulo, int id_tipo_stock, float cantidad, String articulo, String tipo_stock, String movimiento,int id_bodega,int id_centro_costo) {
        this.id_actividad = id_actividad;
        this.id_articulo = id_articulo;
        this.id_tipo_stock = id_tipo_stock;
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.tipo_stock = tipo_stock;
        this.movimiento = movimiento;
        this.id_bodega = id_bodega;
        this.id_centro_costo = id_centro_costo;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public int getId_tipo_stock() {
        return id_tipo_stock;
    }

    public float getCantidad() {
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

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_bodega() {
        return id_bodega;
    }

    public int getId_centro_costo(){
        return id_centro_costo;
    }

    public int getId_actividad(){
        return id_actividad;
    }
}

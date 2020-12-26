package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Stock;
import co.dolmen.sid.entidad.TipoStock;

public class StockDB extends Stock implements DatabaseDDL,DatabaseDLM {

    private SQLiteDatabase db;
    private String sql;
    private Stock stock;

    public StockDB (SQLiteDatabase sqLiteDatabase){
        //this.stock = new Stock();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_STOCK +"(" +
                "id_bodega INTEGER NOT NULL," +
                "id_tipo_stock INTEGER NOT NULL," +
                "id_centro_costo INTEGER NOT NULL," +
                "id_articulo INTEGER NOT NULL," +
                "cantidad NUMERIC(15,3) NOT NULL DEFAULT 0" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_STOCK);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Stock) {
            stock = (Stock) o;
            Cursor result = consultarTodo(stock.getBodega().getIdBodega(),stock.getArticulo().getId(),stock.getTipoStock().getId(),stock.getCentroCosto().getIdCentroCosto());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id_bodega", stock.getBodega().getIdBodega());
                contentValues.put("id_tipo_stock", stock.getTipoStock().getId());
                contentValues.put("id_centro_costo", stock.getCentroCosto().getIdCentroCosto());
                contentValues.put("id_articulo", stock.getArticulo().getId());
                contentValues.put("cantidad", stock.getCantidad());
                db.insert(Constantes.TABLA_STOCK, null, contentValues);
            }
            result.close();
            return true;
        }
        else
            return false;
    }

    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_STOCK);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_STOCK+" ORDER BY id_articulo";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int id_bodega,int id_articulo,int id_tipo_stock,int id_centro_costo) {
        String q = "";
        if (id_tipo_stock != 0){
            q += " and id_tipo_stock="+id_tipo_stock;
        }
        if (id_centro_costo != 0){
            q += " and id_centro_costo="+id_centro_costo;
        }

        this.sql = "SELECT sum(cantidad) as cantidad FROM "+ Constantes.TABLA_STOCK+" where id_bodega= "+id_bodega+" and id_articulo="+id_articulo+" "+q;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.MovimientoArticulo;

public class MovimientoArticuloDB extends MovimientoArticulo implements DatabaseDDL,DatabaseDLM {
    SQLiteDatabase db;
    String sql;
    MovimientoArticulo movimientoArticulo;

    public MovimientoArticuloDB(SQLiteDatabase sqLiteDatabase) {
        super();
        this.db = sqLiteDatabase;
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_MOVIMIENTO_ARTICULO +"(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "id_actividad INTEGER NOT NULL," +
                "id_articulo INTEGER NOT NULL," +
                "id_tipo_stock INTEGER NOT NULL," +
                "id_bodega INTEGER NOT NULL," +
                "id_centro_costo INTEGER NOT NULL," +
                "movimiento VARCHAR(12) NOT NULL," +
                "cantidad DECIMAL(10,3) NOT NULL DEFAULT 0" +
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_MOVIMIENTO_ARTICULO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof MovimientoArticulo) {
            movimientoArticulo = (MovimientoArticulo) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_actividad", movimientoArticulo.getId_actividad());
            contentValues.put("id_articulo", movimientoArticulo.getId_articulo());
            contentValues.put("id_tipo_stock", movimientoArticulo.getId_tipo_stock());
            contentValues.put("id_bodega", movimientoArticulo.getId_bodega());
            contentValues.put("id_centro_costo", movimientoArticulo.getId_centro_costo());
            contentValues.put("movimiento", movimientoArticulo.getMovimiento());
            contentValues.put("cantidad", movimientoArticulo.getCantidad());
            db.insert(Constantes.TABLA_MOVIMIENTO_ARTICULO, null, contentValues);
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
        db.execSQL("DELETE FROM  "+Constantes.TABLA_MOVIMIENTO_ARTICULO);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_MOVIMIENTO_ARTICULO;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int id_actividad) {
        this.sql = "SELECT * FROM "+Constantes.TABLA_MOVIMIENTO_ARTICULO+" WHERE id_actividad="+id_actividad;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

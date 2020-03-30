package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoActividad;

public class ElementoDB extends Elemento implements DatabaseDLM,DatabaseDDL {
    SQLiteDatabase db;
    String sql;
    Elemento elemento;

    public ElementoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL(
                "create table "+ Constantes.TABLA_ELEMENTO+
                "("+
                " _id INTEGER PRIMARY KEY,"+
                "elemento_no VARCHAR(12)," +
                "direccion VARCHAR(80)," +
                "id_municipio INTEGER,"+
                "id_barrio INTEGER,"+
                "id_proceso_sgc INTEGER,"+
                "id_contrato INTEGER,"+
                "id_tipologia INTEGER,"+
                "id_mobiliario INTEGER,"+
                "id_referencia INTEGER,"+
                "id_estado_mobiliario INTEGER"+
                ");"
        );
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ELEMENTO);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof Elemento) {
            elemento = (Elemento) o;
            Cursor result = consultarId(elemento.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", elemento.getId());
                contentValues.put("elemento_no", elemento.getElemento_no());
                contentValues.put("direccion", elemento.getDireccion());
                contentValues.put("id_municipio", elemento.getBarrio().getId());
                contentValues.put("id_barrio", elemento.getBarrio().getIdBarrio());
                contentValues.put("id_proceso_sgc", elemento.getProcesoSgc().getId());
                contentValues.put("id_tipologia", elemento.getReferenciaMobiliario().getId());
                contentValues.put("id_mobiliario", elemento.getReferenciaMobiliario().getIdMobiliario());
                contentValues.put("id_referencia", elemento.getReferenciaMobiliario().getIdReferenciaMobiliario());
                contentValues.put("id_estado_mobiliario", elemento.getEstadoMobiliario().getIdEstadoMobiliario());
                db.insert(Constantes.TABLA_ELEMENTO, null, contentValues);
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

    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+Constantes.TABLA_ELEMENTO;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+Constantes.TABLA_ELEMENTO+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarElemento(int idMunicipio,int idProceso,int elementoNo){
        this.sql = "SELECT * FROM "+Constantes.TABLA_ELEMENTO+" WHERE id_municipio="+idMunicipio+" and id_proceso_sgc="+idProceso+" and elemento_no="+elementoNo;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

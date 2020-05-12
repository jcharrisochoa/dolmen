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

    public void iniciarTransaccion(){
        db.beginTransaction();
    }

    public void finalizarTransaccion(){
        db.setTransactionSuccessful();
        db.endTransaction();
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
                //Log.d("VALUE",contentValues.toString());
                db.insert(Constantes.TABLA_ELEMENTO, null, contentValues);
            }
            result.close();
            return true;
        }
        else
            return false;
    }

    public boolean agregarDatos(int id_elemento,String mobiliario_no,String direccion,
                                int id_municipio,int id_barrio,int id_proceso_sgc,int id_tipologia,
            int id_mobiliario,int id_referencia,int id_estado_mobiliario){
        Cursor result = consultarId(id_elemento);

        if(result.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", id_elemento);
            contentValues.put("elemento_no", mobiliario_no);
            contentValues.put("direccion", direccion);
            contentValues.put("id_municipio", id_municipio);
            contentValues.put("id_barrio", id_barrio);
            contentValues.put("id_proceso_sgc", id_proceso_sgc);
            contentValues.put("id_tipologia", id_tipologia);
            contentValues.put("id_mobiliario", id_mobiliario);
            contentValues.put("id_referencia", id_referencia);
            contentValues.put("id_estado_mobiliario", id_estado_mobiliario);
            //Log.d("VALUE",contentValues.toString());
            db.insert(Constantes.TABLA_ELEMENTO, null, contentValues);
        }
        result.close();
        return true;
    }
    @Override
    public void actualizarDatos(Object E) {

    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ELEMENTO);
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
        this.sql = "SELECT e._id,e.elemento_no,e.direccion,e.id_municipio,e.id_barrio,e.id_proceso_sgc,e.id_tipologia,e.id_mobiliario,e.id_referencia,e.id_estado_mobiliario," +
                    "tm.descripcion as tipologia,mb.descripcion as mobiliario,rm.descripcion as referencia,em.descripcion as estado_mobiliario,b.descripcion as barrio " +
                    "FROM "+
                        Constantes.TABLA_ELEMENTO+" e,"+
                        Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" tm,"+
                        Constantes.TABLA_MOBILIARIO+" mb,"+
                        Constantes.TABLA_REFERNCIA_MOBILIARIO+" rm,"+
                        Constantes.TABLA_ESTADO_MOBILIARIO+" em,"+
                        Constantes.TABLA_BARRIO+" b " +
                    "WHERE " +
                    "e.id_tipologia = tm._id and " +
                    "e.id_mobiliario = mb._id and " +
                    "e.id_referencia = rm._id and " +
                    "e.id_estado_mobiliario = em._id and " +
                    "e.id_barrio = b._id and " +
                    "e.id_municipio="+idMunicipio+" and " +
                    "e.id_proceso_sgc="+idProceso+" and " +
                    "e.elemento_no="+elementoNo;

        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.NormaConstruccionRed;

public class NormaConstruccionRedDB extends NormaConstruccionRed implements DatabaseDLM,DatabaseDDL {

    SQLiteDatabase db;
    String sql;

    public NormaConstruccionRedDB(SQLiteDatabase sqLiteDatabase){
        super();
        this.db = sqLiteDatabase;
    }
    @Override
    public void crearTabla() {
        db.execSQL("create table "+ Constantes.TABLA_NORMA_CONSTRUCCION_RED+
                "( _id INTEGER PRIMARY KEY," +
                "id_tipo_estructura INTEGER, " +
                "descripcion VARCHAR(80)," +
                "norma VARCHAR(12)," +
                "id_comercializador_energia INTEGER " +
                ");");
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_NORMA_CONSTRUCCION_RED);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof NormaConstruccionRed) {
            NormaConstruccionRed normaConstruccionRed = (NormaConstruccionRed) o;
            Cursor result = consultarId(normaConstruccionRed.getId());
            if(result.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", normaConstruccionRed.getId());
                contentValues.put("id_tipo_estructura", normaConstruccionRed.getTipoEstructura().getId());
                contentValues.put("descripcion", normaConstruccionRed.getDescripcion());
                contentValues.put("norma", normaConstruccionRed.getNorma());
                contentValues.put("id_comercializador_energia", normaConstruccionRed.getComercializador().getId());
                db.insert(Constantes.TABLA_NORMA_CONSTRUCCION_RED, null, contentValues);
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
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_RED+" ORDER BY descripcion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarId(int id){
        this.sql = "SELECT _id FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_RED+" WHERE _id="+id;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
    public Cursor consultarTodo(int idTipoEstructura){
        this.sql = "SELECT * FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_RED+" WHERE id_tipo_estructura="+idTipoEstructura;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int idTipoEstructura,int idComercializador){
        this.sql = "SELECT * FROM "+ Constantes.TABLA_NORMA_CONSTRUCCION_RED+" WHERE id_tipo_estructura="+idTipoEstructura+" and id_comercializador_energia="+idComercializador;
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

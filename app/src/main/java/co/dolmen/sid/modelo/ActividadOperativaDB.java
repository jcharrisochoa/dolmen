package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import co.dolmen.sid.BaseDatos;
import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Stock;

public class ActividadOperativaDB extends ActividadOperativa implements DatabaseDDL,DatabaseDLM {
    private SQLiteDatabase db;
    private String sql;
    private ActividadOperativa actividad;

    public ActividadOperativaDB (SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    public void iniciarTransaccion(){
        db.beginTransaction();
    }

    public void finalizarTransaccion(){
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void crearTabla() {
        this.sql = "create table "+ Constantes.TABLA_ACTIVIDAD_OPERATIVA +"(" +
                "id_actividad INTEGER NOT NULL,"+
                "id_programa INTEGER NOT NULL,"+
                "id_municipio INTEGER NOT NULL,"+
                "id_proceso_sgc INTEGER NOT NULL,"+
                "id_espacio_publicitario INTEGER, "+
                "id_elemento INTEGER,"+
                "id_tipo_elemento INTEGER,"+
                "id_centro_costo INTEGER NOT NULL,"+
                "id_barrio INTEGER NOT NULL,"+
                "barrio VARCHAR(80),"+
                "id_tipo_reporte_dano INTEGER,"+
                "id_tipo_operacion INTEGER NOT NULL,"+
                "id_equipo INTEGER,"+
                "id_estado_actividad INTEGER NOT NULL,"+
                "id_usuario_descarga INTEGER,"+
                "fecha_programa DATE NOT NULL,"+
                "fch_actividad DATETIME NOT NULL,"+
                "fch_en_sitio DATETIME,"+
                "fch_ejecucion DATETIME,"+
                "direccion VARCHAR (80) NOT NULL,"+
                "latitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                "longitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                "et VARCHAR (12),"+
                "usuario_programa_actividad VARCHAR(60),"+
                "nro_elemento_desmontado VARCHAR(12),"+
                "observacion TEXT,"+
                "pendiente_sincronizar VARCHAR(1) NOT NULL DEFAULT 'N'"+
                ");";
        db.execSQL(this.sql);
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ACTIVIDAD_OPERATIVA);
    }

    @Override
    public boolean agregarDatos(Object o) {
        if(o instanceof ActividadOperativa) {
            actividad = (ActividadOperativa) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_actividad", actividad.getIdActividad());
            contentValues.put("id_programa", actividad.getPrograma().getId());
            contentValues.put("id_municipio", actividad.getPrograma().getMunicipio().getId());
            contentValues.put("id_proceso_sgc", actividad.getPrograma().getProcesoSgc().getId());
            contentValues.put("id_espacio_publicitario", actividad.getIdEspacioPublicitario());
            contentValues.put("id_elemento", actividad.getElemento().getId());
            contentValues.put("id_tipo_elemento", 0);
            contentValues.put("id_centro_costo", actividad.getCentroCosto().getIdCentroCosto());
            contentValues.put("id_barrio", actividad.getBarrio().getIdBarrio());
            contentValues.put("id_tipo_reporte_dano", actividad.getTipoReporteDano().getId());
            contentValues.put("id_tipo_operacion", actividad.getTipoActividad().getId());
            contentValues.put("id_equipo", actividad.getEquipo().getIdEquipo());
            contentValues.put("id_estado_actividad", actividad.getEstadoActividad().getId());
            contentValues.put("fecha_programa", new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(actividad.getFechaPrograma()));
            contentValues.put("fch_actividad", new SimpleDateFormat("yyyy-mm-dd H:m:s", Locale.getDefault()).format(actividad.getFechaActividad()));
            contentValues.put("direccion",actividad.getDireccion());
            contentValues.put("barrio",actividad.getBarrio().getNombreBarrio());
            contentValues.put("et",actividad.getEt());
            contentValues.put("usuario_programa_actividad",actividad.getUsuarioProgramaActividad());
            db.insert(Constantes.TABLA_ACTIVIDAD_OPERATIVA, null, contentValues);
        }
        return false;
    }

    @Override
    public void actualizarDatos(Object o) {
        if(o instanceof ActividadOperativa) {
            actividad = (ActividadOperativa) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("pendiente_sincronizar", "S");
            //contentValues.put("id_tipo_operacion", actividad.getTipoActividad().getId());
            contentValues.put("id_elemento", actividad.getElemento().getId());
            contentValues.put("id_barrio", actividad.getBarrio().getIdBarrio());
            contentValues.put("direccion", actividad.getDireccion());
            contentValues.put("latitud", actividad.getLatitud());
            contentValues.put("longitud", actividad.getLongitud());
            contentValues.put("fch_en_sitio", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format( actividad.getFechaEnSitio()));
            contentValues.put("fch_ejecucion", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format( actividad.getFechaEjecucion()));
            db.update(Constantes.TABLA_ACTIVIDAD_OPERATIVA,contentValues,"where id_actividad="+actividad.getIdActividad(),null);
        }
    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ACTIVIDAD_OPERATIVA);
    }

    @Override
    public Cursor consultarTodo() {
        return null;
    }
}

package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                "centro_costo VARCHAR(45) NOT NULL,"+
                "id_barrio INTEGER NOT NULL,"+
                "barrio VARCHAR(80),"+
                "id_tipo_reporte_dano INTEGER,"+
                "id_tipo_operacion INTEGER NOT NULL,"+
                "id_equipo INTEGER,"+
                "serial_equipo VARCHAR(12),"+
                "id_estado_actividad INTEGER NOT NULL,"+
                "id_usuario_descarga INTEGER,"+
                "fch_programa DATE NOT NULL,"+
                "fch_actividad DATETIME NOT NULL,"+
                "fch_en_sitio DATETIME,"+
                "fch_ejecucion DATETIME,"+
                "direccion VARCHAR (80) NOT NULL,"+
                "latitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                "longitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                "et VARCHAR (12),"+
                "usuario_programa_actividad VARCHAR(60),"+
                "nro_elemento_desmontado VARCHAR(12),"+
                "elemento_no_encontrado VARCHAR(1) NOT NULL DEFAULT 'N',"+
                "afectado_por_vandalismo VARCHAR(1) NOT NULL DEFAULT 'N',"+
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
            contentValues.put("centro_costo", actividad.getCentroCosto().getDescripcionCentroCosto());
            contentValues.put("id_barrio", actividad.getBarrio().getIdBarrio());
            contentValues.put("id_tipo_reporte_dano", actividad.getTipoReporteDano().getId());
            contentValues.put("id_tipo_operacion", actividad.getTipoActividad().getId());
            contentValues.put("id_equipo", actividad.getEquipo().getIdEquipo());
            contentValues.put("serial_equipo", actividad.getEquipo().getSerial());
            contentValues.put("id_estado_actividad", actividad.getEstadoActividad().getId());
            contentValues.put("fch_programa", new SimpleDateFormat("yyyy-MM-dd").format(actividad.getFechaPrograma()));
            contentValues.put("fch_actividad", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actividad.getFechaActividad()));
            contentValues.put("direccion",actividad.getDireccion());
            contentValues.put("barrio",actividad.getBarrio().getNombreBarrio());
            contentValues.put("et",actividad.getEt());
            contentValues.put("usuario_programa_actividad",actividad.getUsuarioProgramaActividad());
            contentValues.put("observacion",actividad.getObservacion());
            db.insert(Constantes.TABLA_ACTIVIDAD_OPERATIVA, null, contentValues);
        }
        return false;
    }

    @Override
    public void actualizarDatos(Object o) {
        if(o instanceof ActividadOperativa) {
            actividad = (ActividadOperativa) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_tipo_operacion", actividad.getTipoActividad().getId());
            contentValues.put("id_elemento", actividad.getElemento().getId());
            contentValues.put("id_barrio", actividad.getBarrio().getIdBarrio());
            contentValues.put("id_equipo", actividad.getEquipo().getIdEquipo());
            contentValues.put("barrio", actividad.getBarrio().getNombreBarrio());
            contentValues.put("direccion", actividad.getDireccion());
            contentValues.put("latitud", actividad.getLatitud());
            contentValues.put("longitud", actividad.getLongitud());
            contentValues.put("fch_en_sitio", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format( actividad.getFechaEnSitio()));
            contentValues.put("fch_ejecucion", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format( actividad.getFechaEjecucion()));
            contentValues.put("id_estado_actividad",actividad.getEstadoActividad().getId());
            contentValues.put("elemento_no_encontrado",actividad.isElementoNoEncontrado());
            contentValues.put("afectado_por_vandalismo",actividad.isAfectadoPorVandalismo());
            contentValues.put("observacion",actividad.getObservacion());
            contentValues.put("pendiente_sincronizar",actividad.getPendienteSincronizar());
            db.update(Constantes.TABLA_ACTIVIDAD_OPERATIVA,contentValues,"id_actividad="+actividad.getIdActividad(),null);
        }
    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ACTIVIDAD_OPERATIVA);
    }

    public void eliminarDatos(char pendiente) {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_ACTIVIDAD_OPERATIVA+ " where pendiente_sincronizar'"+pendiente+"'");
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "select ao.id_municipio,pg.descripcion,m.descripcion as municipio, p.descripcion as proceso,ta.descripcion as tipo_operacion," +
                "ea.descripcion as estado_actividad,e.elemento_no,e.direccion as direccion_elemento,e.id_barrio," +
                "b.descripcion as barrio_elemento,tm.descripcion as tipologia,mb.descripcion as mobiliario,rm.descripcion as referencia,ao.id_actividad," +
                "ao.id_programa,ao.id_proceso_sgc,ao.id_espacio_publicitario,ao.id_elemento,ao.id_centro_costo,ao.centro_costo," +
                "ao.barrio,ao.id_tipo_reporte_dano,tr.descripcion as tipo_reporte_dano,ao.id_tipo_operacion,ao.id_equipo,ao.serial_equipo,ao.id_estado_actividad," +
                "ao.fch_programa,ao.fch_actividad,ao.direccion,ao.et,ao.usuario_programa_actividad," +
                "ao.pendiente_sincronizar,pg.descripcion as programa,ao.id_espacio_publicitario," +
                "e.id_tipologia,e.id_mobiliario,e.id_referencia,ao.elemento_no_encontrado,ao.afectado_por_vandalismo,ao.pendiente_sincronizar,ao.observacion," +
                "ao.fch_ejecucion,ao.id_equipo,eq.serial,eq.codigo,ao.latitud,ao.longitud, e.id_estado_mobiliario,em.descripcion as estado_mobiliario ," +
                "e.id_tipo_balasto,tb.descripcion as tipo_balasto,e.id_tipo_base_fotocelda,tbf.descripcion as tipo_base_fotocelda," +
                "e.id_tipo_brazo,tbz.descripcion as tipo_brazo,e.zona,e.sector,e.id_control_encendido,ce.descripcion as control_encendido," +
                "e.id_tipo_poste,tp.descripcion as tipo_poste,e.id_norma_construccion_poste,ncp.descripcion as norma_construccion_poste," +
                "e.id_tipo_red,trd.descripcion as tipo_red,e.id_tipo_escenario,tsc.descripcion as tipo_escenario,"+
                "e.id_tipo_instalacion_red_alimentacion, tir.descripcion as tipo_instalacion_red,e.id_clase_via,cv.descripcion as clase_via,"+
                "e.id_calibre_conductores,cb.descripcion as calibre_conductor,e.ancho_via,e.interdistancia,e.poste_no,e.estructura_soporte_compartida,e.transformador_compartido,"+
                "e.potencia_transformador "+
                "from "+Constantes.TABLA_ACTIVIDAD_OPERATIVA+" ao " +
                "join "+Constantes.TABLA_PROGRAMA+" pg on(ao.id_programa = pg._id) " +
                "join "+Constantes.TABLA_MUNICIPIO+" m on(ao.id_municipio = m._id) " +
                "join "+Constantes.TABLA_PROCESO+" p on(ao.id_proceso_sgc = p._id) " +
                "join "+Constantes.TABLA_ESTADO_ACTIVIDAD+" ea on(ao.id_estado_actividad = ea._id) " +
                "join "+Constantes.TABLA_TIPO_ACTIVIDAD+" ta on(ao.id_tipo_operacion = ta._id) " +
                "left join "+Constantes.TABLA_TIPO_REPORTE_DANO+" tr on(ao.id_tipo_reporte_dano = tr._id) " +
                "left join "+Constantes.TABLA_ELEMENTO+" e on(ao.id_elemento = e._id) " +
                "left join "+Constantes.TABLA_ESTADO_MOBILIARIO+" em on(e.id_estado_mobiliario = em._id) "+
                "left join "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" tm on(e.id_tipologia = tm._id) " +
                "left join "+Constantes.TABLA_MOBILIARIO+" mb on(e.id_mobiliario = mb._id) " +
                "left join "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" rm on(e.id_referencia = rm._id) " +
                "left join "+Constantes.TABLA_BARRIO+" b on(e.id_barrio = b._id) " +
                "left join "+Constantes.TABLA_EQUIPO+" eq on(ao.id_equipo = eq._id) " +
                "left join "+Constantes.TABLA_TIPO_BALASTO+" tb on(e.id_tipo_balasto = tb._id) "+
                "left join "+Constantes.TABLA_TIPO_BASE_FOTOCELDA+" tbf on(e.id_tipo_base_fotocelda = tbf._id) "+
                "left join "+Constantes.TABLA_TIPO_BRAZO+" tbz on(e.id_tipo_brazo = tbz._id) "+
                "left join "+Constantes.TABLA_CONTROL_ENCENDIDO+" ce on(e.id_control_encendido = ce._id) "+
                "left join "+Constantes.TABLA_TIPO_POSTE+" tp on(e.id_tipo_poste = tp._id) "+
                "left join "+Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" ncp on(e.id_norma_construccion_poste = ncp._id) "+
                "left join "+Constantes.TABLA_TIPO_RED+" trd on(e.id_tipo_red = trd._id) "+
                "left join "+Constantes.TABLA_TIPO_ESCENARIO+" tsc on(e.id_tipo_escenario = tsc._id) "+
                "left join "+Constantes.TABLA_TIPO_INSTALACION_RED+" tir on(e.id_tipo_instalacion_red_alimentacion = tir._id) "+
                "left join "+Constantes.TABLA_CLASE_VIA+" cv on(e.id_clase_via = cv._id) "+
                "left join "+Constantes.TABLA_CALIBRE+" cb on(e.id_calibre_conductores = cb._id) "+
                "order by ao.direccion";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    public Cursor consultarTodo(int id_municipio,int id_proceso_sgc) {
        String q = "";

        if(id_municipio != 0)
            q = q + " and ao.id_municipio ="+id_municipio;

        if(id_proceso_sgc != 0)
            q = q + " and ao.id_proceso_sgc ="+id_proceso_sgc;

        this.sql = "select ao.id_municipio,pg.descripcion,m.descripcion as municipio, p.descripcion as proceso,ta.descripcion as tipo_operacion," +
                "ea.descripcion as estado_actividad,e.elemento_no,e.direccion as direccion_elemento,e.id_barrio," +
                "b.descripcion as barrio_elemento,tm.descripcion as tipologia,mb.descripcion as mobiliario,rm.descripcion as referencia,ao.id_actividad," +
                "ao.id_programa,ao.id_proceso_sgc,ao.id_espacio_publicitario,ao.id_elemento,ao.id_centro_costo,ao.centro_costo," +
                "ao.barrio,ao.id_tipo_reporte_dano,tr.descripcion as tipo_reporte_dano,ao.id_tipo_operacion,ao.id_equipo,ao.serial_equipo,ao.id_estado_actividad," +
                "ao.fch_programa,ao.fch_actividad,ao.direccion,ao.et,ao.usuario_programa_actividad," +
                "ao.pendiente_sincronizar,pg.descripcion as programa,ao.id_espacio_publicitario," +
                "e.id_tipologia,e.id_mobiliario,e.id_referencia,ao.elemento_no_encontrado,ao.afectado_por_vandalismo,ao.pendiente_sincronizar,ao.observacion," +
                "ao.fch_ejecucion,ao.id_equipo,eq.serial,eq.codigo,ao.latitud,ao.longitud, e.id_estado_mobiliario,em.descripcion as estado_mobiliario ," +
                "e.id_tipo_balasto,tb.descripcion as tipo_balasto,e.id_tipo_base_fotocelda,tbf.descripcion as tipo_base_fotocelda," +
                "e.id_tipo_brazo,tbz.descripcion as tipo_brazo,e.zona,e.sector,e.id_control_encendido,ce.descripcion as control_encendido," +
                "e.id_tipo_poste,tp.descripcion as tipo_poste,e.id_norma_construccion_poste,ncp.descripcion as norma_construccion_poste," +
                "e.id_tipo_red,trd.descripcion as tipo_red,e.id_tipo_escenario,tsc.descripcion as tipo_escenario, "+
                "e.id_tipo_instalacion_red_alimentacion, tir.descripcion as tipo_instalacion_red,e.id_clase_via,cv.descripcion as clase_via, "+
                "e.id_calibre_conductores,cb.descripcion as calibre_conductor,e.ancho_via,e.interdistancia,e.poste_no,e.estructura_soporte_compartida,e.transformador_compartido,"+
                "e.potencia_transformador "+
                "from "+Constantes.TABLA_ACTIVIDAD_OPERATIVA+" ao " +
                "join "+Constantes.TABLA_PROGRAMA+" pg on(ao.id_programa = pg._id) " +
                "join "+Constantes.TABLA_MUNICIPIO+" m on(ao.id_municipio = m._id) " +
                "join "+Constantes.TABLA_PROCESO+" p on(ao.id_proceso_sgc = p._id) " +
                "join "+Constantes.TABLA_ESTADO_ACTIVIDAD+" ea on(ao.id_estado_actividad = ea._id) " +
                "join "+Constantes.TABLA_TIPO_ACTIVIDAD+" ta on(ao.id_tipo_operacion = ta._id) " +
                "left join "+Constantes.TABLA_TIPO_REPORTE_DANO+" tr on(ao.id_tipo_reporte_dano = tr._id) " +
                "left join "+Constantes.TABLA_ELEMENTO+" e on(ao.id_elemento = e._id) " +
                "left join "+Constantes.TABLA_ESTADO_MOBILIARIO+" em on(e.id_estado_mobiliario = em._id) "+
                "left join "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" tm on(e.id_tipologia = tm._id) " +
                "left join "+Constantes.TABLA_MOBILIARIO+" mb on(e.id_mobiliario = mb._id) " +
                "left join "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" rm on(e.id_referencia = rm._id) " +
                "left join "+Constantes.TABLA_BARRIO+" b on(e.id_barrio = b._id) " +
                "left join "+Constantes.TABLA_EQUIPO+" eq on(ao.id_equipo = eq._id) " +
                "left join "+Constantes.TABLA_TIPO_BALASTO+" tb on(e.id_tipo_balasto = tb._id) "+
                "left join "+Constantes.TABLA_TIPO_BASE_FOTOCELDA+" tbf on(e.id_tipo_base_fotocelda = tbf._id) "+
                "left join "+Constantes.TABLA_TIPO_BRAZO+" tbz on(e.id_tipo_brazo = tbz._id) "+
                "left join "+Constantes.TABLA_CONTROL_ENCENDIDO+" ce on(e.id_control_encendido = ce._id) "+
                "left join "+Constantes.TABLA_TIPO_POSTE+" tp on(e.id_tipo_poste = tp._id) "+
                "left join "+Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" ncp on(e.id_norma_construccion_poste = ncp._id) "+
                "left join "+Constantes.TABLA_TIPO_RED+" trd on(e.id_tipo_red = trd._id) "+
                "left join "+Constantes.TABLA_TIPO_ESCENARIO+" tsc on(e.id_tipo_escenario = tsc._id) "+
                "left join "+Constantes.TABLA_TIPO_INSTALACION_RED+" tir on(e.id_tipo_instalacion_red_alimentacion = tir._id) "+
                "left join "+Constantes.TABLA_CLASE_VIA+" cv on(e.id_clase_via = cv._id) "+
                "left join "+Constantes.TABLA_CALIBRE+" cb on(e.id_calibre_conductores = cb._id) "+
                "WHERE 1=1 "+q+
                " order by ao.direccion";
        //Log.d("programacion",""+this.sql);
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

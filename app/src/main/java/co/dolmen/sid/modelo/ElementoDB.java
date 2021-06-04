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
                "create table "+ Constantes.TABLA_ELEMENTO+"("+
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
                    "id_estado_mobiliario INTEGER,"+
                    "id_tipo_balasto INTEGER,"+
                    "id_tipo_base_fotocelda INTEGER,"+
                    "id_tipo_brazo INTEGER,"+
                    "id_control_encendido INTEGER,"+
                    "id_tipo_escenario INTEGER,"+
                    "id_clase_via INTEGER,"+
                    "id_tipo_poste INTEGER,"+
                    "id_norma_construccion_poste INTEGER,"+
                    "id_calibre_conductores INTEGER,"+
                    "id_tipo_red INTEGER,"+
                    "id_tipo_instalacion_red_alimentacion INTEGER,"+
                    "zona VARCHAR(1),"+
                    "sector VARCHAR(1),"+
                    "ancho_via INTEGER,"+
                    "interdistancia INTEGER,"+
                    "poste_no VARCHAR(12),"+
                    "latitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                    "longitud NUMERIC (15,13) NOT NULL DEFAULT 0,"+
                    "transformador_compartido VARCHAR(1),"+
                    "estructura_soporte_compartida VARCHAR (1),"+
                    "potencia_transformador DECIMAL(5,2) NOT NULL DEFAULT 0,"+
                    "placa_mt_transformador VARCHAR (20),"+
                    "placa_ct_transformador VARCHAR (20),"+
                    "foto TEXT,"+
                    "temporal VARCHAR(1) NOT NULL DEFAULT 'N'"+
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
                                int id_mobiliario,int id_referencia,int id_estado_mobiliario,int id_tipo_balasto,
                                int id_tipo_base_fotocelda,int id_tipo_brazo,String zona,String sector,
                                float latitud,float longitud,int id_clase_via,int ancho_via,int id_tipo_poste,
                                int id_norma_construccion_poste,String poste_no,int interdistancia,
                                int id_calibre_conductores,int id_tipo_red,int id_tipo_instalacion_red_alimentacion,
                                int id_control_encendido,int id_tipo_escenario,String transformador_compartido, String estructura_soporte_compartida,
                                double potencia_transformador,String placa_mt,String placa_ct
                                ){
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
            contentValues.put("id_tipo_balasto", id_tipo_balasto);
            contentValues.put("id_tipo_base_fotocelda", id_tipo_base_fotocelda);
            contentValues.put("id_tipo_brazo", id_tipo_brazo);
            contentValues.put("id_control_encendido", id_control_encendido);
            contentValues.put("id_tipo_escenario", id_tipo_escenario);
            contentValues.put("id_clase_via", id_clase_via);
            contentValues.put("id_tipo_poste", id_tipo_poste);
            contentValues.put("id_norma_construccion_poste", id_norma_construccion_poste);
            contentValues.put("id_calibre_conductores", id_calibre_conductores);
            contentValues.put("id_tipo_red", id_tipo_red);
            contentValues.put("id_tipo_instalacion_red_alimentacion", id_tipo_instalacion_red_alimentacion);
            contentValues.put("zona", zona);
            contentValues.put("sector", sector);
            contentValues.put("ancho_via", ancho_via);
            contentValues.put("interdistancia", interdistancia);
            contentValues.put("poste_no", poste_no);
            contentValues.put("latitud", latitud);
            contentValues.put("longitud", longitud);
            contentValues.put("transformador_compartido", transformador_compartido);
            contentValues.put("estructura_soporte_compartida", estructura_soporte_compartida);
            contentValues.put("potencia_transformador", potencia_transformador);
            contentValues.put("placa_mt_transformador", placa_mt);
            contentValues.put("placa_ct_transformador", placa_ct);
            //Log.d("VALUE",contentValues.toString());
            db.insert(Constantes.TABLA_ELEMENTO, null, contentValues);
        }
        result.close();
        return true;
    }
    @Override
    public void actualizarDatos(Object o) {
        String estructura_soporte_compartida;
        String transformador_compartido;
        if(o instanceof Elemento) {
            elemento = (Elemento) o;

            estructura_soporte_compartida = (elemento.isPosteExclusivo())?"N":"S";
            transformador_compartido = (elemento.isTransformadorExclusivo())?"N":"S";
            ContentValues contentValues = new ContentValues();
            contentValues.put("direccion", elemento.getDireccion());
            contentValues.put("id_municipio", elemento.getMunicipio().getId());
            contentValues.put("id_barrio", elemento.getBarrio().getIdBarrio());
            contentValues.put("id_estado_mobiliario", elemento.getEstadoMobiliario().getIdEstadoMobiliario());
            contentValues.put("id_tipo_balasto", elemento.getTipoBalasto().getIdTipoBalasto());
            contentValues.put("id_tipo_base_fotocelda", elemento.getTipoBaseFotocelda().getidTipoBaseFotocelda());
            contentValues.put("id_tipo_brazo", elemento.getTipoBrazo().getidTipoBrazo());
            contentValues.put("id_control_encendido", elemento.getControlEncendido().getidControlEncendido());
            contentValues.put("id_tipo_escenario", elemento.getTipoEscenario().getId());
            contentValues.put("id_clase_via", elemento.getClaseVia().getId());
            contentValues.put("id_tipo_poste", elemento.getNormaConstruccionPoste().getTipoPoste().getId());
            contentValues.put("id_norma_construccion_poste", elemento.getNormaConstruccionPoste().getId());
            contentValues.put("id_calibre_conductores", elemento.getCalibre().getId_calibre());
            contentValues.put("id_tipo_red", elemento.getTipoRed().getId());
            contentValues.put("id_tipo_instalacion_red_alimentacion", elemento.getTipoInstalacionRed().getidTipoInstalacionRed());
            contentValues.put("zona", elemento.getZona());
            contentValues.put("sector", elemento.getSector());
            contentValues.put("ancho_via", elemento.getAnchoVia());
            contentValues.put("interdistancia", elemento.getInterdistancia());
            contentValues.put("poste_no", elemento.getPosteNo());
            contentValues.put("latitud", elemento.getLatitud());
            contentValues.put("longitud", elemento.getLongitud());
            contentValues.put("transformador_compartido", transformador_compartido);
            contentValues.put("estructura_soporte_compartida", estructura_soporte_compartida);
            contentValues.put("potencia_transformador", elemento.getPotenciaTransformador());
            contentValues.put("placa_mt_transformador",  elemento.getPlacaMT());
            contentValues.put("placa_ct_transformador", elemento.getPlacaCT());
            contentValues.put("foto", elemento.getEncodeStringFoto());
            db.update(Constantes.TABLA_ELEMENTO,contentValues,"_id="+elemento.getId(),null);
        }
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
        this.sql = "select e._id,e.elemento_no,e.direccion,e.id_municipio,e.id_barrio,e.id_proceso_sgc,e.id_tipologia,e.id_mobiliario,e.id_referencia,e.id_estado_mobiliario,"+
        "tm.descripcion as tipologia,mb.descripcion as mobiliario,rm.descripcion as referencia,em.descripcion as estado_mobiliario,b.descripcion as barrio,"+
        "e.id_tipo_balasto,tb.descripcion as tipo_balasto, e.id_tipo_base_fotocelda,tbf.descripcion as tipo_base_fotocelda,e.id_tipo_brazo,tbz.descripcion as tipo_brazo,"+
        "e.zona,e.sector,e.id_control_encendido,ce.descripcion as control_encendido, e.id_tipo_poste,tp.descripcion as tipo_poste,e.id_norma_construccion_poste,"+
        "ncp.descripcion as norma_construccion_poste,e.id_tipo_red,trd.descripcion as tipo_red,e.id_tipo_escenario, tsc.descripcion as tipo_escenario,"+
        "e.id_tipo_instalacion_red_alimentacion, tir.descripcion as tipo_instalacion_red,e.id_clase_via,cv.descripcion as clase_via, e.id_calibre_conductores,"+
        "cb.descripcion as calibre_conductor,e.ancho_via,e.interdistancia,e.poste_no ,e.estructura_soporte_compartida,e.transformador_compartido,e.potencia_transformador,"+
        "e.placa_mt_transformador,e.placa_ct_transformador "+
        "from "+ Constantes.TABLA_ELEMENTO+" e "+
        "left join "+Constantes.TABLA_BARRIO+" b ON (e.id_barrio = b._id ) " +
        "left join "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" tm on(e.id_tipologia = tm._id) "+
        "left join "+Constantes.TABLA_MOBILIARIO+" mb on(e.id_mobiliario = mb._id) "+
        "left join "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" rm on(e.id_referencia = rm._id) "+
        "left join "+Constantes.TABLA_ESTADO_MOBILIARIO+" em on(e.id_estado_mobiliario = em._id) "+
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
        "where " +
        "e.id_municipio="+idMunicipio+" and " +
        "e.id_proceso_sgc="+idProceso+" and " +
        "e.elemento_no="+elementoNo;
        //Log.d("respuesta",""+this.sql);
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    //--Elementos de Infraestructura Electrica
    public Cursor consultarElemento(int idMunicipio,int idProceso,int elementoNo,int idTipologia){
        String q = "";
        if(elementoNo != 0){
            q = " and e.elemento_no="+elementoNo;
        }
        this.sql = "select e._id,e.elemento_no,e.direccion,e.id_municipio,e.id_barrio,e.id_proceso_sgc,e.id_tipologia,e.id_mobiliario,e.id_referencia,e.id_estado_mobiliario,"+
                "tm.descripcion as tipologia,mb.descripcion as mobiliario,rm.descripcion as referencia,em.descripcion as estado_mobiliario,b.descripcion as barrio,"+
                "e.zona,e.sector, e.id_tipo_poste,tp.descripcion as tipo_poste,e.id_norma_construccion_poste,"+
                "ncp.descripcion as norma_construccion_poste,e.id_tipo_red,trd.descripcion as tipo_red,"+
                "e.id_tipo_instalacion_red_alimentacion, tir.descripcion as tipo_instalacion_red, e.id_calibre_conductores,"+
                "cb.descripcion as calibre_conductor,e.poste_no ,e.estructura_soporte_compartida,e.transformador_compartido,e.potencia_transformador,"+
                "e.placa_mt_transformador,e.placa_ct_transformador "+
                "from "+ Constantes.TABLA_ELEMENTO+" e "+
                "left join "+Constantes.TABLA_BARRIO+" b ON (e.id_barrio = b._id ) " +
                "left join "+Constantes.TABLA_TIPOLOGIA_MOBILIARIO+" tm on(e.id_tipologia = tm._id) "+
                "left join "+Constantes.TABLA_MOBILIARIO+" mb on(e.id_mobiliario = mb._id) "+
                "left join "+Constantes.TABLA_REFERNCIA_MOBILIARIO+" rm on(e.id_referencia = rm._id) "+
                "left join "+Constantes.TABLA_ESTADO_MOBILIARIO+" em on(e.id_estado_mobiliario = em._id) "+
                "left join "+Constantes.TABLA_TIPO_POSTE+" tp on(e.id_tipo_poste = tp._id) "+
                "left join "+Constantes.TABLA_NORMA_CONSTRUCCION_POSTE+" ncp on(e.id_norma_construccion_poste = ncp._id) "+
                "left join "+Constantes.TABLA_TIPO_RED+" trd on(e.id_tipo_red = trd._id) "+
                "left join "+Constantes.TABLA_TIPO_ESCENARIO+" tsc on(e.id_tipo_escenario = tsc._id) "+
                "left join "+Constantes.TABLA_TIPO_INSTALACION_RED+" tir on(e.id_tipo_instalacion_red_alimentacion = tir._id) "+
                "left join "+Constantes.TABLA_CALIBRE+" cb on(e.id_calibre_conductores = cb._id) "+
                "where " +
                "e.id_municipio="+idMunicipio+" and " +
                "e.id_proceso_sgc="+idProceso+" and " +
                "e.id_tipologia="+idTipologia + q ;
        //Log.d("respuesta",""+this.sql);
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }
}

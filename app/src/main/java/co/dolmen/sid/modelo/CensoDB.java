package co.dolmen.sid.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.entidad.Censo;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.Elemento;

public class CensoDB extends Censo implements DatabaseDLM,DatabaseDDL   {
    SQLiteDatabase db;
    String sql;
    Censo censo;

    public CensoDB(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
        this.censo = new Censo();
    }
    @Override
    public void crearTabla() {

        db.execSQL(
                "create table "+ Constantes.TABLA_CENSO_TECNICO+
                        "("+
                        "id INTEGER PRIMARY KEY ASC NOT NULL,"+
                        "id_municipio INTEGER NOT NULL,"+
                        "id_barrio INTEGER NOT NULL,"+
                        "id_tipologia INTEGER NOT NULL,"+
                        "id_mobiliario INTEGER NOT NULL,"+
                        "id_referencia INTEGER DEFAULT NULL,"+
                        "id_estado_mobiliario INTEGER DEFAULT NULL,"+
                        "longitud NUMERIC(15,13) NOT NULL DEFAULT 0,"+
                        "latitud NUMERIC(15,13) NOT NULL DEFAULT 0,"+
                        "direccion VARCHAR(100) DEFAULT NULL,"+
                        "fch_registro datetime NOT NULL,"+
                        "observacion TEXT DEFAULT NULL,"+
                        "id_censo INTEGER DEFAULT NULL,"+
                        "id_elemento INTEGER DEFAULT NULL,"+
                        "mobiliario_no INTEGER DEFAULT NULL,"+
                        "numero_mobiliario_visible VARCHAR(1) NOT NULL DEFAULT 'S',"+
                        "mobiliario_en_sitio VARCHAR(1) NOT NULL DEFAULT 'S',"+
                        "id_sentido INTEGER DEFAULT NULL,"+
                        "cantidad decimal(6,3) NOT NULL DEFAULT 1,"+
                        "estado VARCHAR(1) NOT NULL DEFAULT 'P',"+
                        "id_tipo_poste INTEGER DEFAULT NULL,"+
                        "id_norma_construccion_poste INTEGER DEFAULT NULL,"+
                        "id_tipo_red INTEGER DEFAULT NULL,"+
                        "poste_no VARCHAR(12) DEFAULT NULL,"+
                        "interdistancia INTEGER NOT NULL DEFAULT 0,"+
                        "puesta_a_tierra VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "poste_exclusivo_ap VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "poste_buen_estado VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "sector VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "zona VARCHAR(1) NOT NULL DEFAULT 'U',"+
                        "id_tipo_retenida INTEGER DEFAULT NULL,"+
                        "id_clase_via INTEGER DEFAULT NULL,"+
                        "serial_medidor VARCHAR(12) NOT NULL DEFAULT '0',"+
                        "lectura_medidor INTEGER NOT NULL DEFAULT 0,"+
                        "potencia_transformador NUMERIC(5,1) NOT NULL DEFAULT 0.0,"+
                        "placa_mt_transformador VARCHAR(12) DEFAULT NULL,"+
                        "placa_ct_transformador VARCHAR(12) DEFAULT NULL,"+
                        "id_tipo_escenario INTEGER DEFAULT NULL,"+
                        "mobiliario_buen_estado VARCHAR(1) NOT NULL DEFAULT 'S',"+
                        "brazo_mal_estado VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "visor_mal_estado VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "mobiliario_mal_posicionado VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "mobiliario_obsoleto VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "mobiliario_sin_bombillo VARCHAR(1) NOT NULL DEFAULT 'N',"+
                        "tipo_propietario_transformador VARCHAR(2) NOT NULL DEFAULT 'PV',"+
                        "id_tipo_conductor_electrico INTEGER DEFAULT NULL,"+
                        "id_calibre INTEGER NOT NULL"+
                        ");"
        );
    }

    @Override
    public void borrarTabla() {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_CENSO_TECNICO);
    }

    public boolean agregarDatosCensoCarga(Object o){
        long lastId;
        if(o instanceof Censo) {
            censo = (Censo) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_municipio",censo.getElemento().getBarrio().getId());
            contentValues.put("id_barrio",censo.getElemento().getBarrio().getIdBarrio());
            contentValues.put("id_tipologia",censo.getElemento().getTipologia().getIdTipologia());
            contentValues.put("id_mobiliario",censo.getElemento().getMobiliario().getIdMobiliario());
            contentValues.put("id_referencia",censo.getElemento().getReferenciaMobiliario().getIdReferenciaMobiliario());
            contentValues.put("id_estado_mobiliario",censo.getEstadoMobiliario().getIdEstadoMobiliario());
            contentValues.put("longitud",censo.getLongitud());
            contentValues.put("latitud",censo.getLatitud());
            contentValues.put("direccion",censo.getElemento().getDireccion());
            contentValues.put("fch_registro",censo.getFchRegistro());
            contentValues.put("observacion",censo.getObservacion());
            contentValues.put("id_censo",censo.getId_censo());
            contentValues.put("id_elemento",censo.getElemento().getId());
            contentValues.put("mobiliario_no",censo.getElemento().getElemento_no());
            contentValues.put("numero_mobiliario_visible",censo.getChkSwLuminariaVisible());
            contentValues.put("mobiliario_en_sitio",censo.getChkSwPoseeLuminaria());
            contentValues.put("id_sentido",0);
            contentValues.put("cantidad",1);
            contentValues.put("estado","P");
            contentValues.put("sector",censo.getSector());
            contentValues.put("zona",censo.getZona());
            contentValues.put("serial_medidor",0);
            contentValues.put("lectura_medidor",0);
            contentValues.put("id_calibre",0);
            contentValues.put("mobiliario_buen_estado",censo.getChkSwMobiliarioBuenEstado());

            try {
                lastId = db.insertWithOnConflict(Constantes.TABLA_CENSO_TECNICO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
                censo.setLastId(lastId);
                //Log.d("sql","-"+contentValues);
            }catch (SQLiteException e){
                Log.d("ErrorI",""+e.getMessage());
                return false;
            }finally {
                //db.close();
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean agregarDatos(Object o) {
        long lastId;
        if(o instanceof Censo) {
            censo = (Censo) o;
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_municipio",censo.getElemento().getBarrio().getId());
            contentValues.put("id_barrio",censo.getElemento().getBarrio().getIdBarrio());
            contentValues.put("id_tipologia",censo.getElemento().getTipologia().getIdTipologia());
            contentValues.put("id_mobiliario",censo.getElemento().getMobiliario().getIdMobiliario());
            contentValues.put("id_referencia",censo.getElemento().getReferenciaMobiliario().getIdReferenciaMobiliario());
            contentValues.put("id_estado_mobiliario",censo.getEstadoMobiliario().getIdEstadoMobiliario());
            contentValues.put("longitud",censo.getLongitud());
            contentValues.put("latitud",censo.getLatitud());
            contentValues.put("direccion",censo.getElemento().getDireccion());
            contentValues.put("fch_registro",censo.getFchRegistro());
            contentValues.put("observacion",censo.getObservacion());
            contentValues.put("id_censo",censo.getId_censo());
            contentValues.put("id_elemento",censo.getElemento().getId());
            contentValues.put("mobiliario_no",censo.getElemento().getElemento_no());
            contentValues.put("numero_mobiliario_visible",censo.getChkSwLuminariaVisible());
            contentValues.put("mobiliario_en_sitio",censo.getChkSwPoseeLuminaria());
            contentValues.put("id_sentido",0);
            contentValues.put("cantidad",1);
            contentValues.put("estado","P");
            contentValues.put("id_tipo_poste",censo.getNormaConstruccionPoste().getTipoPoste().getId());
            contentValues.put("id_norma_construccion_poste",censo.getNormaConstruccionPoste().getId());
            contentValues.put("id_tipo_red",censo.getTipoRed().getId());
            contentValues.put("poste_no",censo.getPosteNo());
            contentValues.put("interdistancia",censo.getInterdistancia());
            contentValues.put("puesta_a_tierra",censo.getChkSwPuestaTierra());
            contentValues.put("poste_exclusivo_ap",censo.getChkSwPosteExclusivoAp());
            contentValues.put("poste_buen_estado",censo.getChkSwPosteBuenEstado());
            contentValues.put("sector",censo.getSector());
            contentValues.put("zona",censo.getZona());
            contentValues.put("id_tipo_retenida",censo.getRetenidaPoste().getId());
            contentValues.put("id_clase_via",censo.getClaseVia().getId());
            contentValues.put("serial_medidor",0);
            contentValues.put("lectura_medidor",0);
            contentValues.put("potencia_transformador",censo.getPotenciaTransformador());
            contentValues.put("placa_mt_transformador",censo.getPlacaMtTransformador());
            contentValues.put("placa_ct_transformador",censo.getPlacaCtTransformador());
            contentValues.put("id_tipo_escenario",censo.getTipoEscenario().getId());
            contentValues.put("mobiliario_buen_estado",censo.getChkSwMobiliarioBuenEstado());
            contentValues.put("tipo_propietario_transformador",censo.getTipoPropietarioTransformador());
            contentValues.put("brazo_mal_estado",censo.getBrazoMalEstado());
            contentValues.put("visor_mal_estado",censo.getVisorMalEstado());
            contentValues.put("mobiliario_mal_posicionado",censo.getMobiliarioMalPosicionado());
            contentValues.put("mobiliario_obsoleto",censo.getMobiliarioObsoleto());
            contentValues.put("mobiliario_sin_bombillo",censo.getSinBombillo());
            contentValues.put("id_calibre",censo.getCalibre().getId_calibre());

            try {
                lastId = db.insertWithOnConflict(Constantes.TABLA_CENSO_TECNICO, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
                censo.setLastId(lastId);
            }catch (SQLiteException e){
                Log.d("ErrorI",""+e.getMessage());
                return false;
            }finally {
                //db.close();
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public void eliminarDatos() {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENSO_TECNICO);
    }

    public void eliminarDatos(int id) {
        db.execSQL("DELETE FROM  "+Constantes.TABLA_CENSO_TECNICO+ " WHERE id="+id);
    }

    @Override
    public Cursor consultarTodo() {
        this.sql = "SELECT * FROM "+ Constantes.TABLA_CENSO_TECNICO+" limit 0,50";
        Cursor result = db.rawQuery(this.sql, null);
        return result;
    }

    @Override
    public void actualizarDatos(Object E) {

    }
}

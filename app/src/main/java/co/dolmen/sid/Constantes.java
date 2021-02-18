package co.dolmen.sid;

public final class Constantes {
    //--Camara
    public static final String IMAGE_DIRECTORY_NAME = "sdcard";
    public static final int TIMEOUT = 150000;
    public static final int CONS_TOMAR_FOTO = 100;
    public static final int CONS_SELECCIONAR_IMAGEN = 200;
    public static final String TAG = "DOLMEN-DEBUGER";
    public static final int MIN_UPDATE_TIME = 2000;
    public static final int MIN_UPDATE_DISTANCE = 0;
    //--Base Datos
    public static final int VERSION_BASEDATOS = 11;
    public static int OLD_VERSION_BASEDATOS;
    public static final String NOMBRE_BASEDATOS = "sid.db";

    public static final String TABLA_ESTADO_MOBILIARIO         = "estado_mobiliario";
    public static final String TABLA_TIPOLOGIA_MOBILIARIO      = "tipologia_mobiliario";
    public static final String TABLA_MOBILIARIO                = "mobiliario";
    public static final String TABLA_REFERNCIA_MOBILIARIO      = "referencia_mobiliario";
    public static final String TABLA_BARRIO                    = "barrio";
    public static final String TABLA_SENTIDO                   = "sentido";
    public static final String TABLA_UNIDAD_MEDIDA             = "unidad_medida";
    public static final String TABLA_TIPO_RED                  = "tipo_red";
    public static final String TABLA_TIPO_POSTE                = "tipo_poste";
    public static final String TABLA_CLASE_VIA                 = "clase_via"; //M1,M2
    public static final String TABLA_TIPO_INTERSECCION         = "tipo_interseccion"; //CL CR KM
    public static final String TABLA_PROCESO                   = "proceso_sgc";
    public static final String TABLA_MUNICIPIO                 = "municipio";
    public static final String TABLA_TIPO_ACTIVIDAD            = "tipo_actividad_operativa";
    public static final String TABLA_CONTRATO                  = "contrato";
    public static final String TABLA_ACTA_CONTRATO             = "acta_contrato";
    public static final String TABLA_PROVEEDOR                 = "proveedor";
    public static final String TABLA_PROGRAMA                  = "programa";
    public static final String TABLA_ESTADO_ACTIVIDAD          = "estado_actividad";
    public static final String TABLA_POTENCIA_TIPO_LUZ         = "potencia_tipo_luz";
    public static final String TABLA_TIPO_ESPACIO              = "tipo_espacio";
    public static final String TABLA_ELEMENTO                  = "elemento";
    public static final String TABLA_TIPO_TENSION              = "tipo_tension";
    public static final String TABLA_RETENIDA_POSTE            = "retenida_poste";
    public static final String TABLA_NORMA_CONSTRUCCION_POSTE  = "norma_construccion_poste";
    public static final String TABLA_TIPO_ESTRUCTURA           = "tipo_estructura";
    public static final String TABLA_NORMA_CONSTRUCCION_RED     = "norma_construccion_red";
    public static final String TABLA_CENSO_TECNICO              = "censo_tecnico";
    public static final String TABLA_CENSO_TECNICO_TIPO_ARMADO  = "censo_tecnico_tipo_armado";
    public static final String TABLA_CENSO_TECNICO_ARCHIVO      = "censo_tecnico_archivo";
    public static final String TABLA_CENSO_ASIGNADO             = "censo_asignado";
    public static final String TABLA_TIPO_REPORTE_DANO          = "tipo_reporte_dano";
    public static final String TABLA_TIPO_ESCENARIO             = "tipo_escenario";
    public static final String TABLA_CONDUCTOR_ELECTRICO        = "tipo_conductor_electrico";
    public static final String TABLA_CALIBRE                    = "calibre";
    public static final String TABLA_TIPO_STOCK                 = "tipo_stock";
    public static final String TABLA_ARTICULO                   = "articulo";
    public static final String TABLA_STOCK                      = "stock";
    public static final String TABLA_ACTIVIDAD_OPERATIVA        = "actividad_operativa";
    public static final String TABLA_TIPO_BRAZO                 = "tipo_brazo";
    public static final String TABLA_TIPO_BALASTO               = "tipo_balasto";
    public static final String TABLA_CONTROL_ENCENDIDO          = "control_encendido";
    public static final String TABLA_TIPO_BASE_FOTOCELDA        = "tipo_base_fotocelda";
    public static final String TABLA_TIPO_INSTALACION_RED       = "tipo_instalacion_red";
    public static final String TABLA_BODEGA                     = "bodega";
    public static final String TABLA_EQUIPO                     = "equipo";
    public static final String TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA= "archivo_actividad_operativa";
    public static final String TABLA_MOVIMIENTO_ARTICULO        = "movimiento_articulo";
    public static final String TABLA_CENTRO_COSTO               = "centro_costo";
    public static final String TABLA_ELEMENTO_DESMONTADO        = "elemento_desmontado";
    public static final String TABLA_VATIAJE_DESMONTADO         = "vatiaje_desmontado";
}
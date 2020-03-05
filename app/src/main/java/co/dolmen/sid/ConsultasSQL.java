package co.dolmen.sid;

public class ConsultasSQL {
    private static final String TABLA_ESTADO_MOBILIARIO         = "estado_mobiliario";
    private static final String TABLA_TIPOLOGIA_MOBILIARIO      = "tipologia_mobiliario";
    private static final String TABLA_MOBILIARIO                = "mobiliario";
    private static final String TABLA_REFERNCIA_MOBILIARIO      = "referencia_mobiliario";
    private static final String TABLA_BARRIO                    = "barrio";
    private static final String TABLA_SENTIDO                   = "sentido";
    private static final String TABLA_UNIDAD_MEDIDA             = "unidad_medida";
    private static final String TABLA_TIPO_RED                  = "tipo_red";
    private static final String TABLA_TIPO_POSTE                = "tipo_poste";
    private static final String TABLA_CLASE_VIA                 = "clase_via"; //M1,M2
    private static final String TABLA_TIPO_VIA                  = "tipo_via"; //CL CR KM
    private static final String TABLA_PROCESO                   = "proceso_sgc";
    private static final String TABLA_MUNICIPIO                 = "municipio";
    private static final String TABLA_TIPO_ACTIVIDAD            = "tipo_actividad_operativa";
    private static final String TABLA_TIPO_REPORTE              = "tipo_reporte";
    private static final String TABLA_CONTRATO                  = "contrato";
    private static final String TABLA_ACTA_CONTRATO             = "acta_contrato";
    private static final String TABLA_PROVEEDOR                 = "proveedor";
    private static final String TABLA_PROGRAMA                  = "programa";
    private static final String TABLA_ESTADO_ACTIVIDAD          = "estado_actividad";
    private static final String TABLA_POTENCIA_TIPO_LUZ         = "potencia_tipo_luz";
    private static final String TABLA_TIPO_STOCK                = "tipo_stock";
    private static final String TABLA_ARTICULO                  = "articulo";
    private static final String TABLA_CENTRO_COSTO              = "centro_costo";
    private static final String TABLA_TIPO_ESPACIO              = "tipo_espacio";
    private static final String TABLA_ELEMENTO                  = "elemento";
    private static final String TABLA_STOCK                           = "stock";
    private static final String TABLA_ACTIVIDAD_OPERATIVA       = "actividad_operativa";
    private static final String TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA   = "archivo_actividad_operativa";
    private static final String TABLA_ORDEN_INVENTARIO          = "orden_inventario";
    private static final String TABLA_DETALLE_ORDEN_INVENTARIO  = "detalle_orden_inventario";


    public static final String CREAR_TABLA_ESTADO_MOBILILARIO = "create table "+TABLA_ESTADO_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER, descripcion VARCHAR(45));";
    public static final String CREAR_TABLA_TIPOLOGIA_MOBILIARIO = "create table "+TABLA_TIPOLOGIA_MOBILIARIO+"( _id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER, descripcion VARCHAR(45));";
    public static final String CREAR_TABLA_MOBILIARIO = "create table "+TABLA_MOBILIARIO+" (_id INTEGER PRIMARY KEY,id_tipologia INTEGER,descripcion VARCHAR(45))";
    public static final String CREAR_TABLA_REFERENCIA = "create table "+TABLA_REFERNCIA_MOBILIARIO+" (_id INTEGER PRIMARY KEY,id_mobiliario INTEGER,descripcion VARCHAR(45))";
    public static final String CREAR_TABLA_BARRIO = "create table "+TABLA_BARRIO+" (_id INTEGER PRIMARY KEY,id_municipio INTEGER,descripcion VARCHAR(80))";
    public static final String CREATE_SENTIDO = "create table "+TABLA_SENTIDO+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(80))";
    public static final String CREATE_UNIDAD_MEDIDA = "create table "+TABLA_UNIDAD_MEDIDA+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45), desc_corta VARCHAR(12))";
    public static final String CREATE_TIPO_RED = "create table "+TABLA_TIPO_RED+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45))";
    public static final String CREATE_TIPO_POSTE = "create table "+TABLA_TIPO_POSTE+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45))";
    public static final String CREATE_CLASE_VIA = "create table "+TABLA_CLASE_VIA+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45),abreviatura VARCHAR(2))";
    public static final String CREATE_TIPO_VIA = "create table "+TABLA_TIPO_VIA+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45),abreviatura VARCHAR(2))";
    public static final String CREATE_PROCESO = "create table "+TABLA_PROCESO+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45))";
    public static final String CREATE_MUNICIPIO = "create table "+TABLA_MUNICIPIO+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(45))";
    public static final String CREATE_TIPO_ACTIVIDAD = "create table "+TABLA_TIPO_ACTIVIDAD+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,descripcion VARCHAR(45))";
    public static final String CREATE_TIPO_REPORTE = "create table "+TABLA_TIPO_REPORTE+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,descripcion VARCHAR(80))";
    public static final String CREATE_CONTRATO = "create table "+TABLA_CONTRATO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER,descripcion VARCHAR(60))";
    public static final String CREATE_ACTA_CONTRATO = "create table "+TABLA_ACTA_CONTRATO+" (_id INTEGER PRIMARY KEY,id_contrato INTEGER,descripcion VARCHAR(200))";
    public static final String CREATE_PROVEEDOR = "create table "+TABLA_PROVEEDOR+" (_id INTEGER PRIMARY KEY,descripcion VARCHAR(200))";
    public static final String CREATE_PROGRAMA = "create table "+TABLA_PROGRAMA+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";

    //--
    public static final String CREATE_ESTADO_ACTIVIDAD = "create table "+TABLA_ESTADO_ACTIVIDAD+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_POTENCIA_TIPO_LUZ = "create table "+TABLA_POTENCIA_TIPO_LUZ+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_TIPO_STOCK = "create table "+TABLA_TIPO_STOCK+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_ARTICULO = "create table "+TABLA_ARTICULO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_CENTRO_COSTO = "create table "+TABLA_CENTRO_COSTO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_TIPO_ESPACIO = "create table "+TABLA_TIPO_ESPACIO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_ELEMENTO = "create table "+TABLA_ELEMENTO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_STOCK = "create table "+TABLA_STOCK+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_ACTIVIDAD_OPERATIVA = "create table "+TABLA_ACTIVIDAD_OPERATIVA+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_ARCHIVO_ACTIVIDAD_OPERATIVA = "create table "+TABLA_ARCHIVO_ACTIVIDAD_OPERATIVA+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_ORDEN_INVENTARIO = "create table "+TABLA_ORDEN_INVENTARIO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";
    public static final String CREATE_TABLA_DETALLE_ORDEN_INVENTARIO = "create table "+TABLA_DETALLE_ORDEN_INVENTARIO+" (_id INTEGER PRIMARY KEY,id_proceso_sgc INTEGER,id_municipio INTEGER)";


    public String setDropTable(String table){
        return "DROP TABLE IF EXISTS "+table;
    }
}

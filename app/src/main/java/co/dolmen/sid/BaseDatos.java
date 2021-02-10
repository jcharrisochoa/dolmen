package co.dolmen.sid;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.dolmen.sid.modelo.ActaContratoDB;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.modelo.ArchivoActividadDB;
import co.dolmen.sid.modelo.ArticuloDB;
import co.dolmen.sid.modelo.BarrioDB;
import co.dolmen.sid.modelo.BodegaDB;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.CensoArchivoDB;
import co.dolmen.sid.modelo.CensoAsignadoDB;
import co.dolmen.sid.modelo.CensoDB;
import co.dolmen.sid.modelo.CensoTipoArmadoDB;
import co.dolmen.sid.modelo.ContratoDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EquipoDB;
import co.dolmen.sid.modelo.MobiliarioDB;
import co.dolmen.sid.modelo.MovimientoArticuloDB;
import co.dolmen.sid.modelo.MunicipioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.NormaConstruccionRedDB;
import co.dolmen.sid.modelo.ProcesoSgcDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.ProveedorDB;
import co.dolmen.sid.modelo.ReferenciaMobiliarioDB;
import co.dolmen.sid.modelo.RetenidaPosteDB;
import co.dolmen.sid.modelo.SentidoDB;
import co.dolmen.sid.modelo.StockDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoBalastoDB;
import co.dolmen.sid.modelo.TipoBaseFotoceldaDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoConductorElectricoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoEspacioDB;
import co.dolmen.sid.modelo.TipoEstructuraDB;
import co.dolmen.sid.modelo.TipoInstalacionRedDB;
import co.dolmen.sid.modelo.TipoInterseccionDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.modelo.TipoStockDB;
import co.dolmen.sid.modelo.TipoTensionDB;
import co.dolmen.sid.modelo.TipologiaDB;
import co.dolmen.sid.modelo.UnidadMedidaDB;
import co.dolmen.sid.modelo.VatiajeDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.EstadoActividadDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.utilidades.MiBaseDatos;

public class BaseDatos extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public BaseDatos(Context context){
        super(context, Constantes.NOMBRE_BASEDATOS,null,Constantes.VERSION_BASEDATOS);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //onUpgrade(sqLiteDatabase,Constantes.VERSION_BASEDATOS,Constantes.VERSION_BASEDATOS+1);
        try {
            MiBaseDatos miBaseDatos = new MiBaseDatos(sqLiteDatabase);
            miBaseDatos.crearTabla();
        }catch (SQLException e){
            e.printStackTrace();
            Log.d("DataBase","Error:"+e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            MiBaseDatos miBaseDatos = new MiBaseDatos(sqLiteDatabase);
            miBaseDatos.borrarTabla();
            onCreate(sqLiteDatabase);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
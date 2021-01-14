package co.dolmen.sid;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.utilidades.DataSpinner;
import co.dolmen.sid.utilidades.ViewPagerAdapter;

public class EjecutaActividad extends AppCompatActivity {

    private ActividadOperativa actividadOperativa;
    private TextView txtTituloTabActividad;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;
    String TAG = "programacion";

    AlertDialog.Builder alert;

    //--Fragment
    private FragmentFotoAntes fragmentFotoAntes;
    private FragmentFotoDespues fragmentFotoDespues;
    private FragmentMateriales fragmentMateriales;
    private FragmentInformacion fragmentInformacion;
    private FragmentElemento fragmentElemento;

    private TextView txtDireccion;
    private TextView txtObservacion;
    private Spinner sltBarrio;

    private Spinner sltTipoActividad;
    private Spinner sltEstadoActividad;

    private Switch  swElementoNoEncontrado;
    private Switch  swAfectadoVandalismo;

    private ArrayList<DataSpinner> barrioActividad;
    private ArrayList<MovimientoArticulo> movimientoArticulos;
    private ArrayList<DataSpinner> tipoActividad;
    private ArrayList<DataSpinner> estadoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecuta_actividad);

        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        conn = new BaseDatos(EjecutaActividad.this);
        database = conn.getReadableDatabase();

        Intent i = getIntent();
        actividadOperativa = (ActividadOperativa)i.getSerializableExtra("actividadOperativa");

        txtTituloTabActividad  = findViewById(R.id.txt_titulo_tab_actividad);
        txtTituloTabActividad.setText("Act No "+actividadOperativa.getIdActividad()+" / Elem No "+actividadOperativa.getElemento().getElemento_no());

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        Bundle bundle = new Bundle();
        bundle.putSerializable("actividadOperativa",actividadOperativa);

        fragmentFotoAntes = new FragmentFotoAntes();
        fragmentFotoDespues = new FragmentFotoDespues();
        fragmentMateriales = new FragmentMateriales();
        fragmentInformacion = new FragmentInformacion();
        fragmentElemento    = new FragmentElemento();
        fragmentFotoAntes.setArguments(bundle);
        fragmentFotoDespues.setArguments(bundle);
        fragmentInformacion.setArguments(bundle);
        fragmentMateriales.setArguments(bundle);
        fragmentElemento.setArguments(bundle);

        tabLayout.setupWithViewPager(viewPager);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fragmentFotoAntes,"Antes");
        viewPagerAdapter.addFragment(fragmentFotoDespues,"Despues");
        viewPagerAdapter.addFragment(fragmentMateriales,"Material");
        viewPagerAdapter.addFragment(fragmentInformacion,"Info");
        viewPagerAdapter.addFragment(fragmentElemento,"Elem");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_material);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_info);
        tabLayout.getTabAt(4).setIcon(R.drawable.icon_marker);

        //--
        FloatingActionButton fabGuardar = findViewById(R.id.fab_guardar);
        FloatingActionButton fabCancelar = findViewById(R.id.fab_cancelar);

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar(view);
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Cancelar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent i = new Intent(EjecutaActividad.this,DetalleActividad.class);
                i.putExtra("actividadOperativa",actividadOperativa);
                startActivity(i);
                EjecutaActividad.this.finish();
            }
        });
    }

    private void guardar(final View view){
        Log.d(TAG,"id elemento="+actividadOperativa.getElemento().getId());
        //--
        txtDireccion       = fragmentInformacion.getView().findViewById(R.id.txt_direccion);
        txtObservacion     = fragmentInformacion.getView().findViewById(R.id.txt_observacion);

        sltBarrio           = fragmentInformacion.getView().findViewById(R.id.slt_barrio);
        sltTipoActividad    = fragmentInformacion.getView().findViewById(R.id.slt_tipo_actividad);
        sltEstadoActividad  = fragmentInformacion.getView().findViewById(R.id.slt_estado_actividad);

        swElementoNoEncontrado  = fragmentInformacion.getView().findViewById(R.id.sw_elemento_no_encontrado);
        swAfectadoVandalismo    = fragmentInformacion.getView().findViewById(R.id.sw_afectado_vandalismo);

        barrioActividad     = fragmentInformacion.barrioList;
        tipoActividad       = fragmentInformacion.tipoActividadList;
        estadoActividad     = fragmentInformacion.estadoActividadList;
        movimientoArticulos = fragmentMateriales.movimientoArticuloArrayList;


        if(validarFotoAntes()){
            if(validarFotoDespues()){
                if(validarInfo(view)){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        guardarFormulario('R', database);
                        //Snackbar.make(view, "Guardar Remoto", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        //
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                //Snackbar.make(view, "Guardar local", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                guardarFormulario('L', database);
                            }
                        });
                        alert.create().show();
                    }
                }
            }
            else{
                Snackbar.make(view, "Foto despues es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        else{
            Snackbar.make(view, "Foto antes es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private boolean validarFotoAntes(){
        if (fragmentFotoAntes.encodeStringFoto_1.isEmpty() &&  fragmentFotoAntes.encodeStringFoto_2.isEmpty() &&
                fragmentFotoAntes.encodeStringFoto_3.isEmpty() && fragmentFotoAntes.encodeStringFoto_4.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validarFotoDespues(){
        if (fragmentFotoDespues.encodeStringFoto_1.isEmpty() &&  fragmentFotoDespues.encodeStringFoto_2.isEmpty() &&
                fragmentFotoDespues.encodeStringFoto_3.isEmpty() && fragmentFotoDespues.encodeStringFoto_4.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validarInfo(View view){
        if(actividadOperativa.getElemento().getId()==0){
            Snackbar.make(view, "Elemento es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        else{
            if(barrioActividad.get(sltBarrio.getSelectedItemPosition()).getId()==0){
                Snackbar.make(view, "Barrio es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return false;
            }
            else{
                if(txtDireccion.getText().toString().trim()==""){
                    Snackbar.make(view, "Direccion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return false;
                }
                else{
                    if(tipoActividad.get(sltTipoActividad.getSelectedItemPosition()).getId()==0){
                        Snackbar.make(view, "Tipo Operacion es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        return false;
                    }
                    else{
                        if(estadoActividad.get(sltEstadoActividad.getSelectedItemPosition()).getId()==0){
                            Snackbar.make(view, "Estado Actividad es Requerido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }
            }
        }
    }

    private void guardarFormulario(char tipoAlmacenamiento, SQLiteDatabase sqLiteDatabase){
        switch (tipoAlmacenamiento) {
            case 'L':
                almacenarDatosLocal(sqLiteDatabase);
                break;
            case 'R':
                almacenarDatosEnRemoto();
                break;
        }
    }

    private void almacenarDatosLocal(SQLiteDatabase sqLiteDatabase) {

    }

    private void almacenarDatosEnRemoto() {

    }

}
package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Barrio;
import co.dolmen.sid.entidad.Bodega;
import co.dolmen.sid.entidad.CentroCosto;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Equipo;
import co.dolmen.sid.entidad.EstadoActividad;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.Municipio;
import co.dolmen.sid.entidad.ProcesoSgc;
import co.dolmen.sid.entidad.Programa;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoActividad;
import co.dolmen.sid.entidad.TipoReporteDano;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.ActividadOperativaDB;
import co.dolmen.sid.utilidades.AdapterData;

public class ListaActividad extends AppCompatActivity  {

    private SharedPreferences config;
    private SQLiteOpenHelper conn;
    private SQLiteDatabase database;
    private AlertDialog.Builder alert;

    private ArrayList<ActividadOperativa> actividadOperativaArrayList;
    private RecyclerView recyclerView;
    AdapterData adapterData;

    private FloatingActionButton fabCancelar;
    private ImageButton btnBuscarActividad;
    private ImageButton btnCancelarBusqueda;

    private Switch swBuscarPorElemento;
    private Switch swBuscarPorDireccion;
    private Switch swBuscarPorActividad;

    private EditText txtDatoBusqueda;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;
    private int idMobiliarioBusqueda;
    private int idReferenciaBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad);
        setTitle(R.string.btn_mis_actividades);

        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        /*nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");*/

        conn = new BaseDatos(ListaActividad.this);
        database = conn.getReadableDatabase();

        alert = new AlertDialog.Builder(this);

        actividadOperativaArrayList = new ArrayList<ActividadOperativa>();

        swBuscarPorElemento     = findViewById(R.id.sw_buscar_elemento);
        swBuscarPorDireccion    = findViewById(R.id.sw_buscar_direccion);
        swBuscarPorActividad    = findViewById(R.id.sw_buscar_actividad);

        fabCancelar             = findViewById(R.id.fab_cancelar);
        txtDatoBusqueda         = findViewById(R.id.txt_dato_busqueda);

        btnBuscarActividad      = findViewById(R.id.btn_buscar_actividad);
        btnCancelarBusqueda     = findViewById(R.id.btn_cancelar_busqueda);

        recyclerView = findViewById(R.id.rw_actividades);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        );

        try {
            consultarActividad(database);
        } catch (ParseException e) {
            Log.d("Error",e.getMessage());
        }
        adapterData = new AdapterData(actividadOperativaArrayList);

        adapterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaActividad.this,DetalleActividad.class);
                ActividadOperativa actividadOperativa  =  actividadOperativaArrayList.get(recyclerView.getChildAdapterPosition(v));
                i.putExtra("actividadOperativa",actividadOperativa);
                startActivity(i);
                ListaActividad.this.finish();
               // v.setBackgroundColor(R.color.colorAccent);
            }
        });

        recyclerView.setAdapter(adapterData);

        txtDatoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        btnBuscarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        btnCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDatoBusqueda.setText("");
                swBuscarPorElemento.setChecked(true);
                swBuscarPorDireccion.setChecked(false);
                swBuscarPorActividad.setChecked(false);
                filter(txtDatoBusqueda.getText().toString(),
                        swBuscarPorElemento.isChecked(),
                        swBuscarPorDireccion.isChecked(),
                        swBuscarPorActividad.isChecked()
                );
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Cancelar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                database.close();
                Intent i = new Intent(ListaActividad.this, Menu.class);
                startActivity(i);
                ListaActividad.this.finish();
            }
        });
    }

    private void filter(String dato,boolean elemento,boolean direccion,boolean actividad){
        ArrayList<ActividadOperativa> filterActividadOperativa = new ArrayList<ActividadOperativa>();

        for(ActividadOperativa a:actividadOperativaArrayList){
            if(elemento) {
                if (a.getElemento().getElemento_no().contains(dato)) {
                    filterActividadOperativa.add(a);
                }
            }
            if(direccion) {
                if (a.getDireccion().toUpperCase().contains(dato.toUpperCase())) {
                    filterActividadOperativa.add(a);
                }
            }
            if(actividad) {
                if (String.valueOf(a.getIdActividad()).contains(dato)) {
                    filterActividadOperativa.add(a);
                }
            }
        }

        adapterData.filterList(filterActividadOperativa);

    }

    public void consultarActividad(SQLiteDatabase database) throws ParseException {
        actividadOperativaArrayList.clear();
        ActividadOperativa actividadOperativa;
        ActividadOperativaDB actividadOperativaDB;
        actividadOperativaDB = new ActividadOperativaDB(database);

        try {
            Cursor cursor = actividadOperativaDB.consultarTodo(idDefaultMunicipio, idDefaultProceso);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    Barrio barrio = new Barrio();
                    barrio.setId(cursor.getInt(cursor.getColumnIndex("id_municipio")));
                    barrio.setDescripcion(cursor.getString(cursor.getColumnIndex("municipio")));
                    barrio.setIdBarrio(0);
                    barrio.setNombreBarrio(cursor.getString(cursor.getColumnIndex("barrio")));

                    Elemento elemento = new Elemento();
                    elemento.setId(cursor.getInt(cursor.getColumnIndex("id_elemento")));
                    if (cursor.getString(cursor.getColumnIndex("elemento_no")) == null) {
                        elemento.setElemento_no("-");
                        elemento.setTipologia(new Tipologia());
                        elemento.setMobiliario(new Mobiliario());
                        elemento.setReferenciaMobiliario(new ReferenciaMobiliario());
                    } else {
                        elemento.setElemento_no(cursor.getString(cursor.getColumnIndex("elemento_no")));
                        elemento.setTipologia(
                                new Tipologia(
                                        cursor.getInt(cursor.getColumnIndex("id_tipologia")),
                                        cursor.getString(cursor.getColumnIndex("tipologia"))
                                )
                        );
                        elemento.setMobiliario(new Mobiliario(
                                cursor.getInt(cursor.getColumnIndex("id_mobiliario")),
                                cursor.getString(cursor.getColumnIndex("mobiliario"))
                        ));

                        elemento.setReferenciaMobiliario(
                                new ReferenciaMobiliario(
                                        cursor.getInt(cursor.getColumnIndex("id_referencia")),
                                        cursor.getString(cursor.getColumnIndex("referencia"))
                                )
                        );
                    }

                    ProcesoSgc procesoSgc = new ProcesoSgc();
                    procesoSgc.setId(cursor.getInt(cursor.getColumnIndex("id_proceso_sgc")));
                    procesoSgc.setDescripcion(cursor.getString(cursor.getColumnIndex("proceso")));

                    EstadoActividad estadoActividad = new EstadoActividad();
                    estadoActividad.setId(cursor.getInt(cursor.getColumnIndex("id_estado_actividad")));
                    estadoActividad.setDescripcion(cursor.getString(cursor.getColumnIndex("estado_actividad")));

                    TipoActividad tipoActividad = new TipoActividad();
                    tipoActividad.setId(cursor.getInt(cursor.getColumnIndex("id_tipo_operacion")));
                    tipoActividad.setDescripcion(cursor.getString(cursor.getColumnIndex("tipo_operacion")));

                    Equipo equipo = new Equipo(
                            cursor.getInt(cursor.getColumnIndex("id_equipo")),
                            cursor.getString(cursor.getColumnIndex("serial_equipo"))
                    );

                    CentroCosto centroCosto = new CentroCosto();
                    centroCosto.setIdCentroCosto(cursor.getInt(cursor.getColumnIndex("id_centro_costo")));
                    centroCosto.setDescripcionCentroCosto(cursor.getString(cursor.getColumnIndex("centro_costo")));

                    TipoReporteDano tipoReporteDano = new TipoReporteDano();
                    tipoReporteDano.setId(cursor.getInt(cursor.getColumnIndex("id_tipo_reporte_dano")));
                    tipoReporteDano.setDescripcion(cursor.getString(cursor.getColumnIndex("tipo_reporte_dano")));

                    Programa programa = new Programa();
                    programa.setId(cursor.getInt(cursor.getColumnIndex("id_programa")));
                    programa.setDescripcion(cursor.getString(cursor.getColumnIndex("programa")));
                    programa.setProcesoSgc(procesoSgc);
                    programa.setFechaPrograma(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("fch_programa"))));
                    programa.setMunicipio((Municipio) barrio);

                    actividadOperativa = new ActividadOperativa(
                            cursor.getInt(cursor.getColumnIndex("id_actividad")),
                            cursor.getInt(cursor.getColumnIndex("id_espacio_publicitario")),
                            programa,
                            procesoSgc,
                            elemento,
                            centroCosto,
                            barrio,
                            estadoActividad,
                            tipoReporteDano,
                            tipoActividad,
                            equipo,
                            new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("fch_programa"))),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(cursor.getColumnIndex("fch_actividad"))),
                            cursor.getString(cursor.getColumnIndex("direccion")),
                            cursor.getString(cursor.getColumnIndex("et")),
                            cursor.getString(cursor.getColumnIndex("usuario_programa_actividad"))
                    );
                    actividadOperativaArrayList.add(actividadOperativa);
                }
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}
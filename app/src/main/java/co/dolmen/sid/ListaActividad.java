package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

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

    SharedPreferences config;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    AlertDialog.Builder alert;

    ArrayList<ActividadOperativa> actividadOperativaArrayList;
    RecyclerView recyclerView;

    Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad);
        setTitle(R.string.btn_mis_actividades);

        config = getSharedPreferences("config", MODE_PRIVATE);
        /*idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");*/

        conn = new BaseDatos(ListaActividad.this);
        database = conn.getReadableDatabase();

        alert = new AlertDialog.Builder(this);

        btnCancelar = findViewById(R.id.btn_cancelar);

        recyclerView = findViewById(R.id.rw_actividades);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        );
        actividadOperativaArrayList = new ArrayList<ActividadOperativa>();
        try {
            consultarActividad(database);
        } catch (ParseException e) {
            Log.d("Error",e.getMessage());
        }
        AdapterData adapterData = new AdapterData(actividadOperativaArrayList);

        adapterData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
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


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.close();
                Intent i = new Intent(ListaActividad.this, Menu.class);
                startActivity(i);
                ListaActividad.this.finish();
            }
        });
    }

    public void consultarActividad(SQLiteDatabase database) throws ParseException {
        ActividadOperativa actividadOperativa;
        ActividadOperativaDB actividadOperativaDB;
        actividadOperativaDB = new ActividadOperativaDB(database);
        Cursor cursor = actividadOperativaDB.consultarTodo();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()){

               Barrio barrio = new Barrio();
                barrio.setId(cursor.getInt(cursor.getColumnIndex("id_municipio")));
                barrio.setDescripcion(cursor.getString(cursor.getColumnIndex("municipio")));
                barrio.setIdBarrio(0);
                barrio.setNombreBarrio(cursor.getString(cursor.getColumnIndex("barrio")));

                Elemento elemento = new Elemento();
                elemento.setId(cursor.getInt(cursor.getColumnIndex("id_elemento")));
                if(cursor.getString(cursor.getColumnIndex("elemento_no")) == null){
                    elemento.setElemento_no("-");
                    elemento.setTipologia(new Tipologia());
                    elemento.setMobiliario(new Mobiliario());
                    elemento.setReferenciaMobiliario(new ReferenciaMobiliario());
                }
                else {
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
                programa.setMunicipio((Municipio)barrio);

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
    }

}
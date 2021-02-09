package co.dolmen.sid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Calibre;
import co.dolmen.sid.entidad.ClaseVia;
import co.dolmen.sid.entidad.ControlEncendido;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.NormaConstruccionPoste;
import co.dolmen.sid.entidad.ReferenciaMobiliario;
import co.dolmen.sid.entidad.TipoBalasto;
import co.dolmen.sid.entidad.TipoBaseFotocelda;
import co.dolmen.sid.entidad.TipoBrazo;
import co.dolmen.sid.entidad.TipoEscenario;
import co.dolmen.sid.entidad.TipoInstalacionRed;
import co.dolmen.sid.entidad.TipoPoste;
import co.dolmen.sid.entidad.TipoRed;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.CalibreDB;
import co.dolmen.sid.modelo.ClaseViaDB;
import co.dolmen.sid.modelo.ControlEncendidoDB;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.EstadoMobiliarioDB;
import co.dolmen.sid.modelo.NormaConstruccionPosteDB;
import co.dolmen.sid.modelo.TipoBalastoDB;
import co.dolmen.sid.modelo.TipoBaseFotoceldaDB;
import co.dolmen.sid.modelo.TipoBrazoDB;
import co.dolmen.sid.modelo.TipoEscenarioDB;
import co.dolmen.sid.modelo.TipoInstalacionRedDB;
import co.dolmen.sid.modelo.TipoPosteDB;
import co.dolmen.sid.modelo.TipoRedDB;
import co.dolmen.sid.utilidades.DataSpinner;
import co.dolmen.sid.utilidades.HandleListenLocation;
import co.dolmen.sid.utilidades.MiLocalizacion;

import static java.lang.Integer.parseInt;

public class FragmentElemento extends Fragment {

    ImageButton btnBuscarElemento;
    ImageButton btnLimpiarBusquedaElemento;
    ImageButton btnCapturarGPS;

    TextView txtMobiliario;
    TextView txtReferencia;
    TextView txtMobiliarioNo;
    TextView txtDireccion;
    TextView gpsLatitud;
    TextView gpsLongitud;
    TextView gpsAltitud;
    TextView gpsPrecision;
    TextView gpsVelocidad;
    TextView gpsDireccion;

    EditText editMobiliarioNo;
    EditText txtLatitud;
    EditText txtLongitud;
    EditText txtInterdistancia;
    EditText txtPotenciaTransformador;
    EditText txtMtTransformador;
    EditText txtCtTransformador;
    EditText txtPosteNo;
    EditText txtAnchoVia;

    RadioButton rdZonaUrbano;
    RadioButton rdZonaRural;
    RadioButton rdSectorNormal;
    RadioButton rdSectorSubNormal;

    Switch swPosteExclusivoAp;
    Switch swTranformadorExclusivoAP;

    Spinner sltEstadoMobiliario;
    Spinner sltClaseVia;
    Spinner sltTipoPoste;
    Spinner sltNormaConstruccionPoste;
    Spinner sltTipoRed;
    Spinner sltTipoEscenario;
    Spinner sltCalibreConexionElemento;
    Spinner sltTipoBrazo;
    Spinner sltTipoBalasto;
    Spinner sltTipoBaseFotocelda;
    Spinner sltTipoInstalacionRed;
    Spinner sltControlEncendido;

    View view;
    ActividadOperativa actividadOperativa;
    AlertDialog.Builder alertBuscarElemento;
    AlertDialog.Builder alert;
    //--
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;

    ArrayList<DataSpinner> estadoMobiliarioList;
    ArrayList<DataSpinner> claseViaList;
    ArrayList<DataSpinner> tipoPosteList;
    ArrayList<DataSpinner> tipoRedList;
    ArrayList<DataSpinner> normaConstruccionPosteList;
    ArrayList<DataSpinner> tipoEscenarioList;
    ArrayList<DataSpinner> calibreList;
    ArrayList<DataSpinner> tipoBrazoList;
    ArrayList<DataSpinner> tipoBalastoList;
    ArrayList<DataSpinner> tipoBaseFotoceldaList;
    ArrayList<DataSpinner> tipoInstalacionRedList;
    ArrayList<DataSpinner> controlEncendidoList;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;

    String zona ="U";
    String sector = "N";


    LocationManager ubicacion;

    public FragmentElemento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ubicacion = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constantes.MIN_UPDATE_TIME, Constantes.MIN_UPDATE_DISTANCE, new miLocalizacion());
        }

        config = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
        idUsuario = config.getInt("id_usuario", 0);
        idDefaultProceso = config.getInt("id_proceso", 0);
        idDefaultContrato = config.getInt("id_contrato", 0);
        idDefaultMunicipio = config.getInt("id_municipio", 0);

        Bundle bundle = this.getArguments();
        actividadOperativa = (ActividadOperativa) bundle.getSerializable("actividadOperativa");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_elemento,container,false);

        alert = new AlertDialog.Builder(view.getContext());
        alert.setCancelable(false);
        alert.setTitle(R.string.titulo_alerta);
        alert.setIcon(R.drawable.icon_problem);

        btnBuscarElemento   = view.findViewById(R.id.btn_buscar_elemento);
        btnLimpiarBusquedaElemento  = view.findViewById(R.id.btn_limpiar_busqueda_elemento);
        btnCapturarGPS              = view.findViewById(R.id.btn_capturar_gps);

        gpsLatitud          = view.findViewById(R.id.gps_latitud);
        gpsLongitud         = view.findViewById(R.id.gps_longitud);

        gpsAltitud          = view.findViewById(R.id.gps_altitud);
        gpsPrecision        = view.findViewById(R.id.gps_precision);
        gpsVelocidad        = view.findViewById(R.id.gps_velocidad);
        gpsDireccion        = view.findViewById(R.id.gps_direccion);

        txtMobiliario       = view.findViewById(R.id.txt_mobiliario);
        txtReferencia       = view.findViewById(R.id.txt_referencia);
        txtMobiliarioNo     = view.findViewById(R.id.txt_mobiliario_numero);
        txtDireccion        = view.findViewById(R.id.txt_direccion);
        txtLatitud          = view.findViewById(R.id.txt_latitud);
        txtLongitud         = view.findViewById(R.id.txt_longitud);
        txtInterdistancia           = view.findViewById(R.id.txt_interdistancia);
        txtPotenciaTransformador    = view.findViewById(R.id.txt_potencia_transformador);
        txtMtTransformador          = view.findViewById(R.id.txt_mt_transformador);
        txtCtTransformador          = view.findViewById(R.id.txt_ct_transformador);
        txtPosteNo                  = view.findViewById(R.id.txt_poste_no);
        txtAnchoVia                 = view.findViewById(R.id.txt_ancho_via);


        editMobiliarioNo    = view.findViewById(R.id.txt_mobiliario_no);
        editMobiliarioNo.setText(String.valueOf(actividadOperativa.getElemento().getElemento_no()));

        sltEstadoMobiliario         = view.findViewById(R.id.slt_estado_mobiliario);
        sltClaseVia                 = view.findViewById(R.id.slt_clase_via);
        sltTipoPoste                = view.findViewById(R.id.slt_tipo_poste);
        sltNormaConstruccionPoste   = view.findViewById(R.id.slt_norma_construccion_poste);
        sltTipoRed                  = view.findViewById(R.id.slt_tipo_red);
        sltTipoEscenario            = view.findViewById(R.id.slt_tipo_escenario);
        sltCalibreConexionElemento  = view.findViewById(R.id.slt_calibre_conexion_elemento);
        sltTipoBrazo                = view.findViewById(R.id.slt_tipo_brazo);
        sltTipoBalasto              = view.findViewById(R.id.slt_tipo_balasto);
        sltTipoBaseFotocelda        = view.findViewById(R.id.slt_tipo_base_fotocelda);
        sltTipoInstalacionRed       = view.findViewById(R.id.slt_tipo_instalacion_red);
        sltControlEncendido         = view.findViewById(R.id.slt_control_encendido);
        rdZonaUrbano                = view.findViewById(R.id.rd_urbano);
        rdZonaRural                 = view.findViewById(R.id.rd_rural);
        rdSectorNormal              = view.findViewById(R.id.rd_normal);
        rdSectorSubNormal           = view.findViewById(R.id.rd_subnormal);
        swPosteExclusivoAp          = view.findViewById(R.id.sw_poste_exclulsivo_alumbrado_publico);
        swTranformadorExclusivoAP   = view.findViewById(R.id.sw_transformador_exclusivo_ap);

        txtLatitud.setEnabled(false);
        txtLongitud.setEnabled(false);

        rdZonaUrbano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zona = "U";
            }
        });

        rdZonaRural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zona = "R";
            }
        });

        rdSectorNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sector = "N";
            }
        });

        rdSectorSubNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sector = "S";
            }
        });

        sltTipoPoste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarNormaConstruccionPoste(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCapturarGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!estadoGPS()){
                    alert.setTitle(getString(R.string.titulo_alerta));
                    alert.setMessage(getString(R.string.alert_gps_deshabilitado));
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();
                }
                else {
                    txtLatitud.setText(gpsLatitud.getText().toString().trim());
                    txtLongitud.setText(gpsLongitud.getText().toString().trim());
                }
            }
        });

        btnBuscarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarElemento(database);
            }
        });

        btnLimpiarBusquedaElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFrmBuscarElemento();
            }
        });

        setFrmElemento();


        return view;
    }

    private void setFrmElemento() {

        txtMobiliarioNo.setText(actividadOperativa.getElemento().getElemento_no());
        txtDireccion.setText(actividadOperativa.getElemento().getDireccion());
        txtMobiliario.setText(actividadOperativa.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(actividadOperativa.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());

        txtAnchoVia.setText(String.valueOf(actividadOperativa.getElemento().getAnchoVia()));
        txtInterdistancia.setText(String.valueOf(actividadOperativa.getElemento().getInterdistancia()));
        txtPosteNo.setText(actividadOperativa.getElemento().getPosteNo());
        txtMtTransformador.setText(actividadOperativa.getElemento().getPlacaMT());
        txtCtTransformador.setText(actividadOperativa.getElemento().getPlacaCT());
        txtPotenciaTransformador.setText(String.valueOf(actividadOperativa.getElemento().getPotenciaTransformador()));

        zona = actividadOperativa.getElemento().getZona();
        sector = actividadOperativa.getElemento().getSector();

        if(actividadOperativa.getElemento().getZona().contentEquals("U")){
            rdZonaUrbano.setChecked(true);
            rdZonaRural.setChecked(false);
        }
        else{
            rdZonaUrbano.setChecked(false);
            rdZonaRural.setChecked(true);
        }

        if(actividadOperativa.getElemento().getSector().contentEquals("N")){
            rdSectorNormal.setChecked(true);
            rdSectorSubNormal.setChecked(false);
        }
        else{
            rdSectorNormal.setChecked(false);
            rdSectorSubNormal.setChecked(true);
        }


        swPosteExclusivoAp.setChecked(actividadOperativa.getElemento().isPosteExclusivo());
        swTranformadorExclusivoAP.setChecked(actividadOperativa.getElemento().isTransformadorExclusivo());

        cargarTipoBalasto(database);
        cargarTipoBaseFotocelda(database);
        cargarTipoBrazo(database);
        cargarControlEncendido(database);
        cargarEstadoMobiliario(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTipoEscenario(database);
        cargarCalibre(database);
        cargarNormaConstruccionPoste(database);
        cargarTipoInstalacionRed(database);
    }
    //--
    private void buscarElemento(SQLiteDatabase sqLiteDatabase) {
        if (editMobiliarioNo.getText().toString().trim().length() == 0) {
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_elemento_buscar);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        } else {
            alertBuscarElemento = new AlertDialog.Builder(view.getContext());
            alertBuscarElemento.setCancelable(false);
            alertBuscarElemento.setIcon(android.R.drawable.ic_dialog_alert);
            try {
                ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
                Cursor cursorElemento = elementoDB.consultarElemento(idDefaultMunicipio, idDefaultProceso, Integer.parseInt(editMobiliarioNo.getText().toString()));
                if (cursorElemento.getCount() == 0) {
                    alertBuscarElemento.setTitle(R.string.titulo_alerta);
                    alertBuscarElemento.setMessage(getText(R.string.alert_elemento_no_encontrado) + " sobre el Elemento: " + editMobiliarioNo.getText());
                    alertBuscarElemento.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetFrmBuscarElemento();
                        }
                    });
                    alertBuscarElemento.create().show();

                } else {
                    if (cursorElemento.getCount() > 1) {
                        alertBuscarElemento.setMessage("Existe mas de un elemento con el mismo número,Seleccione");
                        alertBuscarElemento.setNeutralButton("Aceptar", null);
                        alertBuscarElemento.create().show();
                    } else {
                        cursorElemento.moveToFirst();

                        Elemento elemento = actividadOperativa.getElemento();
                        elemento.setId(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("_id"))));
                        elemento.setElemento_no(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));

                        elemento.setTipologia(
                                new Tipologia(
                                        cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipologia")),
                                        cursorElemento.getString(cursorElemento.getColumnIndex("tipologia"))
                                )
                        );
                        elemento.setMobiliario(new Mobiliario(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_mobiliario")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario"))
                        ));

                        elemento.setReferenciaMobiliario(
                                new ReferenciaMobiliario(
                                        cursorElemento.getInt(cursorElemento.getColumnIndex("id_referencia")),
                                        cursorElemento.getString(cursorElemento.getColumnIndex("referencia"))
                                )
                        );
                        elemento.setEstadoMobiliario(new EstadoMobiliario(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario"))
                        ));

                        elemento.setTipoBalasto(new TipoBalasto(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_balasto")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_balasto"))
                        ));

                        elemento.setNormaConstruccionPoste(new NormaConstruccionPoste(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_norma_construccion_poste")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("norma_construccion_poste")),
                                new TipoPoste(
                                        cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_poste")),
                                        cursorElemento.getString(cursorElemento.getColumnIndex("tipo_poste"))
                                )
                        ));

                        elemento.setTipoBaseFotocelda(new TipoBaseFotocelda(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_base_fotocelda")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_base_fotocelda"))
                        ));

                        elemento.setTipoBrazo(new TipoBrazo(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_brazo")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_brazo"))
                        ));

                        elemento.setControlEncendido(new ControlEncendido(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_control_encendido")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("control_encendido"))
                        ));
                        elemento.setTipoRed(new TipoRed(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_red")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_red"))
                        ));

                        elemento.setTipoInstalacionRed(new TipoInstalacionRed(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_instalacion_red_alimentacion")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_instalacion_red"))
                        ));

                        elemento.setTipoEscenario(new TipoEscenario(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_tipo_escenario")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("tipo_escenario"))
                        ));

                        elemento.setClaseVia(new ClaseVia(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_clase_via")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("clase_via"))
                        ));

                        elemento.setCalibre(new Calibre(
                                cursorElemento.getInt(cursorElemento.getColumnIndex("id_calibre_conductores")),
                                cursorElemento.getString(cursorElemento.getColumnIndex("calibre_conductor"))
                        ));

                        zona = cursorElemento.getString(cursorElemento.getColumnIndex("zona"));
                        sector = cursorElemento.getString(cursorElemento.getColumnIndex("sector"));

                        elemento.setZona(cursorElemento.getString(cursorElemento.getColumnIndex("zona")));
                        elemento.setSector(cursorElemento.getString(cursorElemento.getColumnIndex("sector")));
                        elemento.setAnchoVia(cursorElemento.getInt(cursorElemento.getColumnIndex("ancho_via")));
                        elemento.setInterdistancia(cursorElemento.getInt(cursorElemento.getColumnIndex("interdistancia")));
                        elemento.setPosteNo(cursorElemento.getString(cursorElemento.getColumnIndex("poste_no")));
                        elemento.setTransformadorExclusivo((cursorElemento.getString(cursorElemento.getColumnIndex("transformador_compartido")).contentEquals("S"))?false:true);
                        elemento.setPosteExclusivo((cursorElemento.getString(cursorElemento.getColumnIndex("estructura_soporte_compartida")).contentEquals("S"))?false:true);
                        elemento.setPotenciaTransformador(cursorElemento.getDouble(cursorElemento.getColumnIndex("potencia_transformador")));
                        elemento.setDireccion(cursorElemento.getString(cursorElemento.getColumnIndex("direccion")));
                        elemento.setPlacaMT(cursorElemento.getString(cursorElemento.getColumnIndex("placa_mt_transformador")));
                        elemento.setPlacaCT(cursorElemento.getString(cursorElemento.getColumnIndex("placa_ct_transformador")));
                        actividadOperativa.setElemento(elemento);

                        setFrmElemento();

                    }

                }
                cursorElemento.close();
            }catch (SQLException e){
                Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //--
    private void resetFrmBuscarElemento() {

        Elemento elemento = actividadOperativa.getElemento();
        elemento.setId(0);
        elemento.setElemento_no("");
        elemento.setTipologia(new Tipologia());
        elemento.setMobiliario(new Mobiliario());
        elemento.setReferenciaMobiliario(new ReferenciaMobiliario());
        elemento.setEstadoMobiliario(new EstadoMobiliario());
        elemento.setTipoBalasto(new TipoBalasto(0,""));
        elemento.setNormaConstruccionPoste(new NormaConstruccionPoste());
        elemento.setTipoBaseFotocelda(new TipoBaseFotocelda());
        elemento.setTipoBrazo(new TipoBrazo());
        elemento.setControlEncendido(new ControlEncendido());
        elemento.setTipoRed(new TipoRed());
        elemento.setTipoInstalacionRed(new TipoInstalacionRed());
        elemento.setTipoEscenario(new TipoEscenario());
        elemento.setClaseVia(new ClaseVia());
        elemento.setCalibre(new Calibre());
        elemento.setZona("U");
        elemento.setSector("N");
        elemento.setAnchoVia(0);
        elemento.setInterdistancia(0);
        elemento.setPosteNo("");
        elemento.setTransformadorExclusivo(true);
        elemento.setPosteExclusivo(true);
        elemento.setPotenciaTransformador(0);
        actividadOperativa.setElemento(elemento);

        editMobiliarioNo.setText("");
        txtMobiliarioNo.setText("");
        txtMobiliario.setText("");
        txtReferencia.setText("");
        txtDireccion.setText("");
        txtAnchoVia.setText("");
        txtInterdistancia.setText("");
        txtPosteNo.setText("");
        txtMtTransformador.setText("");
        txtCtTransformador.setText("");
        txtPotenciaTransformador.setText("");
        zona = "U";
        sector = "N";
        rdZonaUrbano.setChecked(true);
        rdZonaRural.setChecked(false);
        rdSectorNormal.setChecked(true);
        rdSectorSubNormal.setChecked(false);
        swPosteExclusivoAp.setChecked(false);
        swTranformadorExclusivoAP.setChecked(false);

        cargarTipoBalasto(database);
        cargarTipoBaseFotocelda(database);
        cargarTipoBrazo(database);
        cargarControlEncendido(database);
        cargarEstadoMobiliario(database);
        cargarClaseVia(database);
        cargarTipoPoste(database);
        cargarTipoRed(database);
        cargarTipoEscenario(database);
        cargarCalibre(database);
        cargarNormaConstruccionPoste(database);
        cargarTipoInstalacionRed(database);
    }
    //--
    private void cargarTipoBrazo(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoBrazoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoBrazoDB tipoBrazoDB = new TipoBrazoDB(sqLiteDatabase);
        Cursor cursor = tipoBrazoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoBrazoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoBrazo().getidTipoBrazo()== cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoBrazoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoBrazo.setAdapter(dataAdapter);
        sltTipoBrazo.setSelection(pos);
    }
    //--
    private void cargarTipoBalasto(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoBalastoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoBalastoDB tipoBalastoDB = new TipoBalastoDB(sqLiteDatabase);
        Cursor cursor = tipoBalastoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoBalastoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoBalasto().getIdTipoBalasto()== cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoBalastoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoBalasto.setAdapter(dataAdapter);
        sltTipoBalasto.setSelection(pos);
    }
    //--
    private void cargarTipoBaseFotocelda(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoBaseFotoceldaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoBaseFotoceldaDB tipoBaseFotoceldaDB = new TipoBaseFotoceldaDB(sqLiteDatabase);
        Cursor cursor = tipoBaseFotoceldaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoBaseFotoceldaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoBaseFotocelda().getidTipoBaseFotocelda()== cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoBaseFotoceldaList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoBaseFotocelda.setAdapter(dataAdapter);
        sltTipoBaseFotocelda.setSelection(pos);
    }
    //--
    private void cargarControlEncendido(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        controlEncendidoList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ControlEncendidoDB controlEncendidoDB = new ControlEncendidoDB(sqLiteDatabase);
        Cursor cursor = controlEncendidoDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        controlEncendidoList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getControlEncendido().getidControlEncendido()== cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    controlEncendidoList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltControlEncendido.setAdapter(dataAdapter);
        sltControlEncendido.setSelection(pos);
    }
    //--
    private void cargarEstadoMobiliario(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        estadoMobiliarioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        EstadoMobiliarioDB estadoMobiliarioDB = new EstadoMobiliarioDB(sqLiteDatabase);
        Cursor cursor = estadoMobiliarioDB.consultarTodo(idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        estadoMobiliarioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    if (    cursor.getInt(0) == 10 ||
                            cursor.getInt(0) == 11 ||
                            cursor.getInt(0) == 13 ||
                            cursor.getInt(0) == 15){
                        i++;
                        if(actividadOperativa.getElemento().getEstadoMobiliario().getIdEstadoMobiliario() == cursor.getInt(0)){
                            pos = i;
                        }

                        dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                        estadoMobiliarioList.add(dataSpinner);
                        labels.add(cursor.getString(2).toUpperCase());
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltEstadoMobiliario.setAdapter(dataAdapter);
        sltEstadoMobiliario.setSelection(pos);
    }
    //--
    private void cargarClaseVia(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        claseViaList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ClaseViaDB claseViaDB = new ClaseViaDB(sqLiteDatabase);
        Cursor cursor = claseViaDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        claseViaList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getClaseVia().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    claseViaList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltClaseVia.setAdapter(dataAdapter);
        sltClaseVia.setSelection(pos);
    }
    //--
    private void cargarTipoPoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoPosteDB tipoPosteDB = new TipoPosteDB(sqLiteDatabase);
        Cursor cursor = tipoPosteDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoPosteList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getNormaConstruccionPoste().getTipoPoste().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoPosteList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoPoste.setAdapter(dataAdapter);
        sltTipoPoste.setSelection(pos);
    }
    //--
    private void cargarTipoRed(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos=0;
        tipoRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoRedDB tipoRedDB = new TipoRedDB(sqLiteDatabase);
        Cursor cursor = tipoRedDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoRedList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoRed().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoRedList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoRed.setAdapter(dataAdapter);
        sltTipoRed.setSelection(pos);
    }
    //--
    private void cargarTipoEscenario(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoEscenarioList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoEscenarioDB tipoEscenarioDB = new TipoEscenarioDB(sqLiteDatabase);
        Cursor cursor = tipoEscenarioDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoEscenarioList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoEscenario().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoEscenarioList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoEscenario.setAdapter(dataAdapter);
        sltTipoEscenario.setSelection(pos);
    }
    //--
    private void cargarCalibre(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        calibreList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        CalibreDB calibreDB = new CalibreDB(sqLiteDatabase);
        Cursor cursor = calibreDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        calibreList.add(dataSpinner);

        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getCalibre().getId_calibre() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    calibreList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltCalibreConexionElemento.setAdapter(dataAdapter);
        sltCalibreConexionElemento.setAdapter(dataAdapter);
        sltCalibreConexionElemento.setSelection(pos);
    }
    //--
    private void cargarNormaConstruccionPoste(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        int idTipoPoste = tipoPosteList.get(sltTipoPoste.getSelectedItemPosition()).getId();
        normaConstruccionPosteList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        NormaConstruccionPosteDB normaConstruccionPosteDB = new NormaConstruccionPosteDB(sqLiteDatabase);
        Cursor cursor = normaConstruccionPosteDB.consultarTodo(idTipoPoste);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        normaConstruccionPosteList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getNormaConstruccionPoste().getId() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    normaConstruccionPosteList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltNormaConstruccionPoste.setAdapter(dataAdapter);
        sltNormaConstruccionPoste.setSelection(pos);
    }
    //--
    private void cargarTipoInstalacionRed(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        int pos = 0;
        tipoInstalacionRedList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoInstalacionRedDB tipoInstalacionRedDB = new TipoInstalacionRedDB(sqLiteDatabase);
        Cursor cursor = tipoInstalacionRedDB.consultarTodo();
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoInstalacionRedList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    if(actividadOperativa.getElemento().getTipoInstalacionRed().getidTipoInstalacionRed() == cursor.getInt(0)){
                        pos = i;
                    }
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(1).toUpperCase());
                    tipoInstalacionRedList.add(dataSpinner);
                    labels.add(cursor.getString(1).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoInstalacionRed.setAdapter(dataAdapter);
        sltTipoInstalacionRed.setSelection(pos);
    }
    //--
    public boolean estadoGPS() {
        ubicacion = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        return (ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }
    //--
    private class miLocalizacion implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //coordenadaActiva = false;
            float mts =  Math.round(location.getAccuracy()*100)/100;
            float alt =  Math.round(location.getAltitude()*100)/100;
            gpsLatitud.setText(String.valueOf(location.getLatitude()));
            gpsLongitud.setText(String.valueOf(location.getLongitude()));
            gpsAltitud.setText(alt+ "Mts");
            gpsPrecision.setText(mts+ " Mts");
            gpsVelocidad.setText(location.getSpeed()+" Km/h");
            try {
                if(getContext()!=null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> listDireccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    String miDireccion = listDireccion.get(0).getThoroughfare() + " " +
                            listDireccion.get(0).getFeatureName() + ", " +
                            listDireccion.get(0).getLocality() + "/" +
                            listDireccion.get(0).getAdminArea();

                    gpsDireccion.setText(miDireccion);
                }
                //--direccion.get(0).getAddressLine(0)
            }catch (IOException e ){
                gpsDireccion.setText("Sin Internet para convertir coordenadas en dirección");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getContext(),s+" Activo",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getContext(),s+" Inactivo",Toast.LENGTH_LONG).show();
        }
    }
}
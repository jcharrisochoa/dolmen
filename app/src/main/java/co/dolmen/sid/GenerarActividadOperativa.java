package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.ProgramaDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.utilidades.DataSpinner;
import cz.msebera.android.httpclient.Header;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GenerarActividadOperativa extends AppCompatActivity {

    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    AlertDialog.Builder alert;
    SharedPreferences config;

    private String  encodeString;
    private String titulo;
    private String nombreMunicipio;
    private String nombreProceso;
    private String nombreContrato;
    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;

    Elemento elemento;

    private String path;
    //-
    private Integer idMunicipio;
    private Integer idProceso;
    private Integer idContrato;

    private TextView txtNombreMunicipio;
    private TextView txtNombreProceso;
    private TextView txtNombreContrato;

    private Spinner sltTipoActividad;
    private Spinner sltPrograma;

    private Button btnCancelar;
    private Button btnGuardar;
    private ImageButton btnBuscar;
    private Button btnFoto;
    private Button btnTomarFoto;
    private Button btnBorrarFoto;
    private ImageView imgFoto;


    private EditText inElemento;
    private EditText txtIdElemento;
    private EditText txtTipologia;
    private EditText txtMobiliario;
    private EditText txtReferencia;
    private EditText txtBarrio;
    private EditText txtDireccion;
    private EditText txtEstadoMobiliario;
    private EditText txtObservacion;
    private EditText txtIdPrograma;


    List<DataSpinner> tipoActividadList;
    List<DataSpinner> programacionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_actividad_operativa);

        setTitle(getText(R.string.titulo_generar_actividad_operativa));
        config = getSharedPreferences("config", MODE_PRIVATE);
        idUsuario           = config.getInt("id_usuario", 0);
        idDefaultProceso    = config.getInt("id_proceso", 0);
        idDefaultContrato   = config.getInt("id_contrato", 0);
        idDefaultMunicipio  = config.getInt("id_municipio", 0);
        nombreMunicipio = config.getString("nombreMunicipio", "");
        nombreProceso = config.getString("nombreProceso", "");
        nombreContrato = config.getString("nombreContrato", "");

        conn = new BaseDatos(GenerarActividadOperativa.this);
        database = conn.getReadableDatabase();

        //--
        alert = new AlertDialog.Builder(this);

        btnCancelar     = findViewById(R.id.btn_cancelar);
        btnGuardar      = findViewById(R.id.btn_guardar);
        btnBuscar       = findViewById(R.id.btn_buscar_elemento);
        btnTomarFoto    = findViewById(R.id.btn_tomar_foto);
        btnBorrarFoto   = findViewById(R.id.btn_borrar_foto);

        txtNombreMunicipio  = findViewById(R.id.txtNombreMunicipio);
        txtNombreProceso    = findViewById(R.id.txtNombreProceso);
        txtNombreContrato   = findViewById(R.id.txtNombreContrato);

        txtNombreMunicipio.setText(nombreMunicipio);
        txtNombreProceso.setText(nombreProceso);
        txtNombreContrato.setText(nombreContrato);

        txtIdElemento       = findViewById(R.id.txtIdElmento);
        txtIdElemento.setVisibility(View.INVISIBLE);

        inElemento          = findViewById(R.id.txt_buscar_elemento);
        txtTipologia        = findViewById(R.id.txtTipologia);
        txtMobiliario       = findViewById(R.id.txtMobiliario);
        txtReferencia       = findViewById(R.id.txtReferencia);
        txtBarrio           = findViewById(R.id.txtBarrio);
        txtDireccion        = findViewById(R.id.txtDireccion);
        txtEstadoMobiliario = findViewById(R.id.txtEstadoMobiliario);
        txtObservacion      = findViewById(R.id.txtObservacion);
        sltTipoActividad      = findViewById(R.id.slt_tipo_actividad);
        sltPrograma         = findViewById(R.id.sltPrograma);

        //--Acciones sobre Controles
        txtTipologia.setEnabled(false);
        txtMobiliario.setEnabled(false);
        txtReferencia.setEnabled(false);
        txtBarrio.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtEstadoMobiliario.setEnabled(false);

        imgFoto = findViewById(R.id.foto);

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });
        btnBorrarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFoto.setImageResource(R.drawable.imagen_no_disponible);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GenerarActividadOperativa.this, Menu.class);
                startActivity(i);
                GenerarActividadOperativa.this.finish();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarElemento(database);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarAccionReporte()){
                    ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()) {
                        Toast.makeText(getApplicationContext(),"Conectando con "+networkInfo.getTypeName()+" / "+networkInfo.getExtraInfo(),Toast.LENGTH_LONG).show();
                        generarActividadOperativa();
                    }
                    else{
                        alert.setTitle(R.string.titulo_alerta);
                        alert.setMessage(R.string.alert_conexion);
                        alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                    }
                }
                else{
                    alert.setTitle(R.string.titulo_alerta);
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();
                }
            }
        });
        cargarTipoReporte(database);
        cargarPrograma(database);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap;
        ByteArrayOutputStream stream;
        byte[] array;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constantes.CONS_SELECCIONAR_IMAGEN:
                    String encode = "";
                    Uri selectionPath = data.getData();
                    //Log.d("Path",""+selectionPath.getPath());

                    try {
                        InputStream s = getContentResolver().openInputStream(selectionPath);
                        bitmap = BitmapFactory.decodeStream(s,null,options);
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        array = stream.toByteArray();
                        encode = Base64.encodeToString(array,0);
                        //Log.d("Path",""+encode);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    imgFoto.setImageURI(selectionPath);
                    encodeString = encode;

                    break;
                case Constantes.CONS_TOMAR_FOTO:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path, options);

                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    array = stream.toByteArray();

                    imgFoto.setImageBitmap(bitmap);
                    encodeString = Base64.encodeToString(array,0);

                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_cancelar_camara), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.alert_error_camara), Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarTipoReporte(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        tipoActividadList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        TipoActividadDB tipoActividadDB = new TipoActividadDB(sqLiteDatabase);
        Cursor cursor = tipoActividadDB.consultarTodo(idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        tipoActividadList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(2).toUpperCase());
                    tipoActividadList.add(dataSpinner);
                    labels.add(cursor.getString(2).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltTipoActividad.setAdapter(dataAdapter);
    }

    private void cargarPrograma(SQLiteDatabase sqLiteDatabase) {
        int i = 0;
        programacionList = new ArrayList<DataSpinner>();
        List<String> labels = new ArrayList<>();
        ProgramaDB programaDB = new ProgramaDB(sqLiteDatabase);
        Cursor cursor = programaDB.consultarTodo(idDefaultMunicipio,idDefaultProceso);
        DataSpinner dataSpinner = new DataSpinner(i, getText(R.string.seleccione).toString());
        programacionList.add(dataSpinner);
        labels.add(getText(R.string.seleccione).toString());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    i++;
                    dataSpinner = new DataSpinner(cursor.getInt(0), cursor.getString(0).toUpperCase());
                    programacionList.add(dataSpinner);
                    labels.add(cursor.getString(0).toUpperCase());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sltPrograma.setAdapter(dataAdapter);
    }

    private void buscarElemento(SQLiteDatabase sqLiteDatabase){
        if(inElemento.getText().toString().trim().length() == 0){
            alert.setTitle(R.string.titulo_alerta);
            alert.setMessage(R.string.alert_elemento_buscar);
            alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
        else{

            try{
                ElementoDB elementoDB = new ElementoDB(sqLiteDatabase);
                Cursor cursorElemento = elementoDB.consultarElemento(idDefaultMunicipio,idDefaultProceso,parseInt(inElemento.getText().toString()));
                if(cursorElemento.getCount() == 0) {
                    alert.setTitle(R.string.titulo_alerta);
                    alert.setMessage(getText(R.string.alert_elemento_no_encontrado)+" sobre el Elemento: "+inElemento.getText());
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            inElemento.setText("");
                        }
                    });
                    alert.create().show();
                }
                else {

                    cursorElemento.moveToFirst();

                    elemento = new Elemento();
                    elemento.setId(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("_id"))));
                    elemento.setElemento_no(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));

                    EstadoMobiliario estadoMobiliario = new EstadoMobiliario();
                    estadoMobiliario.setIdEstadoMobiliario(cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario")));

                    elemento.setEstadoMobiliario(estadoMobiliario);

                    txtIdElemento.setText(cursorElemento.getString(cursorElemento.getColumnIndex("_id")));
                    txtTipologia.setText(cursorElemento.getString(cursorElemento.getColumnIndex("tipologia")));
                    txtMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario")));
                    txtReferencia.setText(cursorElemento.getString(cursorElemento.getColumnIndex("referencia")));
                    txtBarrio.setText(cursorElemento.getString(cursorElemento.getColumnIndex("barrio")));
                    txtDireccion.setText(cursorElemento.getString(cursorElemento.getColumnIndex("direccion")));
                    txtEstadoMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario")));

                }
                cursorElemento.close();
            }
            catch (SQLException e){
                Toast.makeText(getApplicationContext(),"ERROR:"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validarAccionReporte(){
        if(txtIdElemento.getText().toString().isEmpty()){
            alert.setMessage(R.string.alert_elemento_buscar);
            return false;
        }
        else {
            if (tipoActividadList.get(sltTipoActividad.getSelectedItemPosition()).getId() == 0) {
                alert.setMessage(R.string.alert_tipo_actividad);
                return false;
            }
            else{
                if (programacionList.get(sltPrograma.getSelectedItemPosition()).getId() == 0) {
                    alert.setMessage(R.string.alert_programa_operativo);
                    return false;
                }
                else {
                    if (txtObservacion.getText().toString().isEmpty()) {
                        alert.setMessage(R.string.alert_observacion);
                        return false;
                    } else {
                        if (encodeString == null || encodeString == "") {
                            alert.setMessage(R.string.alert_fotografia);
                            return false;
                        } else
                            return true;
                    }
                }
            }
        }
    }

    private void cargarImagen(){
        final CharSequence[] opciones = {getText(R.string.tomar_foto).toString(), getText(R.string.cargar_imagen).toString(), getText(R.string.btn_cancelar)};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(GenerarActividadOperativa.this);
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) { //Tomar Foto
                    case 0:
                        int PERMISSIONS_REQUEST_CAMERA = 0;
                        if (ContextCompat.checkSelfPermission(GenerarActividadOperativa.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(GenerarActividadOperativa.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            tomarFoto();
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen
                        int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                        if (ContextCompat.checkSelfPermission(GenerarActividadOperativa.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(GenerarActividadOperativa.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/");
                            startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"), Constantes.CONS_SELECCIONAR_IMAGEN);
                        }
                        dialogInterface.dismiss();
                        break;
                    case 2: //Cancelar
                        dialogInterface.dismiss();
                        break;
                    default:
                        dialogInterface.dismiss();
                }

            }
        });
        alertOpciones.show();
    }

    public void tomarFoto() {
        String nombreImagen = "";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constantes.IMAGE_DIRECTORY_NAME);
        boolean iscreada = mediaStorageDir.exists();

        if (iscreada == false) {
            iscreada = mediaStorageDir.mkdir();
        } else {
            nombreImagen = "IMG_" + (System.currentTimeMillis() / 100) + ".jpg";
        }

        path = mediaStorageDir.getPath() + File.separator + nombreImagen;
        File imagen = new File(path);

        Uri photoURI = FileProvider.getUriForFile(GenerarActividadOperativa.this, getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
    }

    private void generarActividadOperativa(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("idmunicipio", idDefaultMunicipio);
        requestParams.put("idprocesosgc", idDefaultProceso);
        requestParams.put("idusuario", idUsuario);
        requestParams.put("idprograma", programacionList.get(sltPrograma.getSelectedItemPosition()).getId());
        requestParams.put("idtipoactividad", tipoActividadList.get(sltTipoActividad.getSelectedItemPosition()).getId());
        requestParams.put("idelemento", txtIdElemento.getText().toString());
        requestParams.put("barrio", txtBarrio.getText().toString());
        requestParams.put("direccion", txtDireccion.getText().toString());
        requestParams.put("observacion", txtObservacion.getText().toString());
        requestParams.put("encode_string",encodeString);

        client.setTimeout(Constantes.TIMEOUT);
        RequestHandle post = client.post(ServicioWeb.urlGenerarActividadOperativa, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Integer request;
                String mensaje;
               // Log.d("RESPUESTA",""+respuesta);

                try {
                    JSONObject o = new JSONObject(new String(responseBody));
                    mensaje         = o.getString("mensaje");
                    request         = o.getInt("request");
                    alert.setTitle(R.string.titulo_alerta);
                    alert.setMessage(mensaje);
                    alert.setNeutralButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetFrm();
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();

                } catch (JSONException e) {
                    Toast.makeText(GenerarActividadOperativa.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String respuesta = new String(responseBody);
               // Log.d("RESPUESTA",""+respuesta);
                Toast.makeText(GenerarActividadOperativa.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void resetFrm(){
        txtIdElemento.setText("");
        inElemento.setText("");
        txtTipologia.setText("");
        txtMobiliario.setText("");
        txtReferencia.setText("");
        txtDireccion.setText("");
        txtBarrio.setText("");
        txtEstadoMobiliario.setText("");
        txtObservacion.setText("");
        imgFoto.setImageResource(R.drawable.imagen_no_disponible);
        sltTipoActividad.setSelection(0);
        sltPrograma.setSelection(0);
        encodeString=null;
    }
}

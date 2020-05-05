package co.dolmen.sid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.EstadoMobiliario;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.modelo.TipoActividadDB;
import co.dolmen.sid.modelo.TipoReporteDanoDB;
import co.dolmen.sid.utilidades.DataSpinner;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        cargarTipoReporte(database);
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
}

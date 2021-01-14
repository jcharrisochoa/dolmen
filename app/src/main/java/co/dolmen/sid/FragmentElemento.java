package co.dolmen.sid;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;
import co.dolmen.sid.entidad.Mobiliario;
import co.dolmen.sid.entidad.Tipologia;
import co.dolmen.sid.modelo.ElementoDB;
import co.dolmen.sid.utilidades.DataSpinner;

import static java.lang.Integer.parseInt;

public class FragmentElemento extends Fragment {

    ImageButton btnBuscarElemento;
    ImageButton btnLimpiarBusquedaElemento;

    TextView txtMobiliario;
    TextView txtReferencia;
    TextView txtMobiliarioNo;

    EditText editMobiliarioNo;

    View view;
    ActividadOperativa actividadOperativa;
    AlertDialog.Builder alertBuscarElemento;
    AlertDialog.Builder alert;
    //--
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;

    private int idUsuario;
    private int idDefaultMunicipio;
    private int idDefaultProceso;
    private int idDefaultContrato;


    public FragmentElemento() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new BaseDatos(getContext());
        database = conn.getReadableDatabase();

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

        txtMobiliario       = view.findViewById(R.id.txt_mobiliario);
        txtReferencia       = view.findViewById(R.id.txt_referencia);
        txtMobiliarioNo     = view.findViewById(R.id.txt_mobiliario_numero);

        editMobiliarioNo    = view.findViewById(R.id.txt_mobiliario_no);

        txtMobiliarioNo.setText(actividadOperativa.getElemento().getElemento_no());
        txtMobiliario.setText(actividadOperativa.getElemento().getMobiliario().getDescripcionMobiliario());
        txtReferencia.setText(actividadOperativa.getElemento().getReferenciaMobiliario().getDescripcionReferenciaMobiliario());

        editMobiliarioNo.setText(String.valueOf(actividadOperativa.getElemento().getElemento_no()));



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

        return view;
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
                Cursor cursorElemento = elementoDB.consultarElemento(idDefaultMunicipio, idDefaultProceso, parseInt(editMobiliarioNo.getText().toString()));
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

                        Elemento elemento = new Elemento();
                        elemento.setId(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("_id"))));
                        elemento.setElemento_no(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));

                        Tipologia tipologia = new Tipologia();
                        tipologia.setIdTipologia(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("id_tipologia"))));
                        tipologia.setDescripcionTipologia(cursorElemento.getString(cursorElemento.getColumnIndex("tipologia")));
                        elemento.setTipologia(tipologia);

                        Mobiliario mobiliario = new Mobiliario();
                        mobiliario.setIdMobiliario(Integer.parseInt(cursorElemento.getString(cursorElemento.getColumnIndex("id_mobiliario"))));
                        mobiliario.setDescripcionMobiliario(cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario")));
                        elemento.setMobiliario(mobiliario);

                       /* EstadoMobiliario estadoMobiliario = new EstadoMobiliario();
                        estadoMobiliario.setIdEstadoMobiliario(cursorElemento.getInt(cursorElemento.getColumnIndex("id_estado_mobiliario")));
                        estadoMobiliario.setDescripcionEstadoMobiliario(cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario")));
                        elemento.setEstadoMobiliario(estadoMobiliario);*/

                        actividadOperativa.setElemento(elemento);

                        txtMobiliarioNo.setText(cursorElemento.getString(cursorElemento.getColumnIndex("elemento_no")));
                        //editDireccion.setText(cursorElemento.getString(cursorElemento.getColumnIndex("direccion")));
                        txtMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("mobiliario")));
                        txtReferencia.setText(cursorElemento.getString(cursorElemento.getColumnIndex("referencia")));
                        //txtEstadoMobiliario.setText(cursorElemento.getString(cursorElemento.getColumnIndex("estado_mobiliario")));

                    }

                }
                cursorElemento.close();
            }catch (SQLException e){
                Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void resetFrmBuscarElemento() {
        editMobiliarioNo.setText("");
        txtMobiliarioNo.setText("");
        txtMobiliario.setText("");
        txtReferencia.setText("");
        actividadOperativa.setElemento(new Elemento());
    }
}
package co.dolmen.sid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class FragmentFotoDespues extends Fragment {

    View view;
    Button btnDelFoto1;
    Button btnDelFoto2;
    Button btnDelFoto3;
    Button btnDelFoto4;
    ImageView imgAntfoto1;
    ImageView imgAntfoto2;
    ImageView imgAntfoto3;
    ImageView imgAntfoto4;
    private  String path;
    private boolean accionarFoto1;
    private boolean accionarFoto2;
    private boolean accionarFoto3;
    private boolean accionarFoto4;
    String encodeStringFoto_1 = "";
    String encodeStringFoto_2 = "";
    String encodeStringFoto_3 = "";
    String encodeStringFoto_4 = "";

    public FragmentFotoDespues(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foto_despues,container,false);

        btnDelFoto1 = view.findViewById(R.id.btn_borrar_foto_1);
        btnDelFoto2 = view.findViewById(R.id.btn_borrar_foto_2);
        btnDelFoto3 = view.findViewById(R.id.btn_borrar_foto_3);
        btnDelFoto4 = view.findViewById(R.id.btn_borrar_foto_4);

        imgAntfoto1 = view.findViewById(R.id.foto_despues_1);
        imgAntfoto2 = view.findViewById(R.id.foto_despues_2);
        imgAntfoto3 = view.findViewById(R.id.foto_despues_3);
        imgAntfoto4 = view.findViewById(R.id.foto_despues_4);


        imgAntfoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto1 = true;
                cargarImagen();
            }
        });

        imgAntfoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto2 = true;
                cargarImagen();
            }
        });

        imgAntfoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto3 = true;
                cargarImagen();
            }
        });

        imgAntfoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionarFoto4 = true;
                cargarImagen();
            }
        });

        btnDelFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(imgAntfoto1);
            }
        });
        btnDelFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(imgAntfoto2);
            }
        });
        btnDelFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(imgAntfoto3);
            }
        });
        btnDelFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarFoto(imgAntfoto4);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        InputStream s = getActivity().getContentResolver().openInputStream(selectionPath);
                        bitmap = BitmapFactory.decodeStream(s,null,options);
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        array = stream.toByteArray();
                        encode = Base64.encodeToString(array,0);
                        //Log.d("Path",""+encode);
                    } catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (accionarFoto1) {
                        imgAntfoto1.setImageURI(selectionPath);
                        //imgAntfoto1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        //imgAntfoto1.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        encodeStringFoto_1 = encode;
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        imgAntfoto2.setImageURI(selectionPath);
                        encodeStringFoto_2 = encode;
                        accionarFoto2 = false;
                    }

                    if (accionarFoto3) {
                        imgAntfoto3.setImageURI(selectionPath);
                        encodeStringFoto_3 = encode;
                        accionarFoto3 = false;
                    }

                    if (accionarFoto4) {
                        imgAntfoto4.setImageURI(selectionPath);
                        encodeStringFoto_4 = encode;
                        accionarFoto4 = false;
                    }

                    break;
                case Constantes.CONS_TOMAR_FOTO:

                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path, options);

                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    array = stream.toByteArray();

                    if (accionarFoto1) {
                        imgAntfoto1.setImageBitmap(bitmap);
                        encodeStringFoto_1 = Base64.encodeToString(array, 0);
                        accionarFoto1 = false;
                    }

                    if (accionarFoto2) {
                        imgAntfoto2.setImageBitmap(bitmap);
                        encodeStringFoto_2 = Base64.encodeToString(array, 0);
                        accionarFoto2 = false;
                    }

                    if (accionarFoto3) {
                        imgAntfoto3.setImageBitmap(bitmap);
                        encodeStringFoto_3 = Base64.encodeToString(array, 0);
                        accionarFoto3 = false;
                    }

                    if (accionarFoto4) {
                        imgAntfoto4.setImageBitmap(bitmap);
                        encodeStringFoto_4 = Base64.encodeToString(array, 0);
                        accionarFoto4 = false;
                    }

                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            accionarFoto1 = false;
            accionarFoto2 = false;
            accionarFoto3 = false;
            accionarFoto4 = false;
            Toast.makeText(getContext(), getText(R.string.alert_cancelar_camara), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), getText(R.string.alert_error_camara), Toast.LENGTH_SHORT).show();
        }

    }

    public void quitarFoto(ImageView imageView){
        imageView.setImageResource(R.drawable.icon_no_photography);
        switch (imageView.getId()){
            case R.id.foto_despues_1:
                encodeStringFoto_1 = "";
                break;
            case R.id.foto_despues_2:
                encodeStringFoto_2 = "";
                break;
            case R.id.foto_despues_3:
                encodeStringFoto_3 = "";
                break;
            case R.id.foto_despues_4:
                encodeStringFoto_4 = "";
                break;
        }
    }
    //--
    private void cargarImagen(){
        final CharSequence[] opciones = {getText(R.string.tomar_foto).toString(), getText(R.string.cargar_imagen).toString(), getText(R.string.btn_cancelar)};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle(getText(R.string.app_name));
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int PERMISSIONS_REQUEST_INTERNAL_STORAGE = 0;
                int PERMISSIONS_REQUEST_CAMERA = 0;
                switch (i) { //Tomar Foto
                    case 0:
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        }
                        else {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_INTERNAL_STORAGE);
                            }
                            else {
                                tomarFoto();
                            }
                        }
                        dialogInterface.dismiss();
                        break;
                    case 1: //Seleccionar Imagen

                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
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
    //--
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

        Uri photoURI = FileProvider.getUriForFile(getContext(), getString(R.string.file_provider_authority), imagen);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, Constantes.CONS_TOMAR_FOTO);
    }
}
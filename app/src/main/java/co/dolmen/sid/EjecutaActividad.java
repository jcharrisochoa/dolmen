package co.dolmen.sid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.TextView;

import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.utilidades.ViewPagerAdapter;

public class EjecutaActividad extends AppCompatActivity {

    private ActividadOperativa actividadOperativa;
    private TextView txtTituloTabActividad;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    SQLiteOpenHelper conn;
    SQLiteDatabase database;
    SharedPreferences config;

    //--Fragment
    private FragmentFotoAntes fragmentFotoAntes;
    private FragmentFotoDespues fragmentFotoDespues;
    private FragmentMateriales fragmentMateriales;
    private FragmentInformacion fragmentInformacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecuta_actividad);

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
        fragmentInformacion.setArguments(bundle);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fragmentFotoAntes,"Antes");
        viewPagerAdapter.addFragment(fragmentFotoDespues,"Despues");
        viewPagerAdapter.addFragment(fragmentMateriales,"Material");
        viewPagerAdapter.addFragment(fragmentInformacion,"Info");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_material);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_info);


        FloatingActionButton fabGuardar = findViewById(R.id.fab_guardar);
        FloatingActionButton fabCancelar = findViewById(R.id.fab_cancelar);

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Guardar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


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

}
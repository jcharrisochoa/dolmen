package co.dolmen.sid.utilidades;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;


public class MiLocalizacion implements LocationListener {

    Context context;
    double latitud;
    double longitud;

    public MiLocalizacion(Context context){
        this.context = context;
    }

    public double getLatitud() {
        return latitud;
    }

    private void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    private void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public void onLocationChanged(Location location) {
        //coordenadaActiva = false;
        float mts =  Math.round(location.getAccuracy()*100)/100;
        float alt =  Math.round(location.getAltitude()*100)/100;
        //Log.d("programacion","Lat:"+location.getLatitude()+",Lon:"+location.getLongitude()+",Context="+context.getClass().getSimpleName());
        setLatitud(location.getLatitude());
        setLongitud(location.getLongitude());
        /*viewLatitud.setText(""+location.getLatitude());
        viewLongitud.setText(""+location.getLongitude());
        viewAltitud.setText(alt+ "Mts");
        viewPrecision.setText(mts+ " Mts");
        viewVelocidad.setText(location.getSpeed()+" Km/h");
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> listDireccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String miDireccion = listDireccion.get(0).getThoroughfare()+" "+
                    listDireccion.get(0).getFeatureName()+", "+
                    listDireccion.get(0).getLocality()+"/"+
                    listDireccion.get(0).getAdminArea();

            viewDireccion.setText(miDireccion);
            //--direccion.get(0).getAddressLine(0)
        }catch (IOException e){
            viewDireccion.setText("Sin Internet para convertir coordenadas en dirección");
        }*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(context,s+" Activo",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(context,s+" Inactivo",Toast.LENGTH_LONG).show();
    }

}

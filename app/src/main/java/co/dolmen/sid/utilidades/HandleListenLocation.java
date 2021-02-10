package co.dolmen.sid.utilidades;

import android.location.Location;

public interface HandleListenLocation<T> {
    public void onSuccess(T object);
    public void onFailure(Exception e);
}

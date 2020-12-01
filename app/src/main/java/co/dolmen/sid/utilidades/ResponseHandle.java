package co.dolmen.sid.utilidades;

public interface ResponseHandle {
    void onSuccess(byte[] response);
    void onFailure(Exception e);
}

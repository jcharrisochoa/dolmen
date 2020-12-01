package co.dolmen.sid.utilidades;

public interface HandleTaskResponse<T> {
    public void onSuccess(T object);
    public void onFailure(Exception e);
}

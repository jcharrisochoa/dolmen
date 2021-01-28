package co.dolmen.sid.utilidades;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.dolmen.sid.R;
import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.MovimientoArticulo;
import co.dolmen.sid.modelo.StockDB;
import cz.msebera.android.httpclient.client.UserTokenHandler;

public class AdapterMovimientoArticulo extends RecyclerView.Adapter<AdapterMovimientoArticulo.ViewHolderData>{

    private ArrayList<MovimientoArticulo> movimientoArticuloArrayList;
    private SQLiteDatabase database;
    private int idBodega;
    private int idCentroCosto;

    public AdapterMovimientoArticulo(ArrayList<MovimientoArticulo> movimientoArticuloArrayList, SQLiteDatabase sqLiteDatabase,int idBodega,int idCentroCosto) {
        this.movimientoArticuloArrayList = movimientoArticuloArrayList;
        this.database = sqLiteDatabase;
        this.idBodega = idBodega;
        this.idCentroCosto = idCentroCosto;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_descarga,null,false);
        return new AdapterMovimientoArticulo.ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMovimientoArticulo.ViewHolderData holder, final int position) {
        holder.asignarDatos(movimientoArticuloArrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(v.getContext());
                alert.setCancelable(false);
                alert.setTitle(R.string.titulo_alerta);
                alert.setIcon(R.drawable.icon_problem);
                String msg = holder.itemView.getContext().getString(R.string.alert_eliminiar_articulo)+
                        " "+movimientoArticuloArrayList.get(position).getArticulo()+
                        " \nCant: "+movimientoArticuloArrayList.get(position).getCantidad();
                alert.setMessage(msg);
                alert.setPositiveButton(R.string.btn_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeItem(position,holder.itemView);
                        dialogInterface.cancel();
                    }
                });
                alert.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimientoArticuloArrayList.size();
    }

    public void addItem(MovimientoArticulo movimientoArticulo){
        movimientoArticuloArrayList.add(movimientoArticulo);
        notifyDataSetChanged();
    }

    public void removeItem(int position,View v){
        /*
        Caso desmontado util
        Se debe nivelar el stock ,stock en lista
         */
        int pos= -1;
        float stockBase =  0;
        float cantListNegativo = 0;
        float cantListPositivo = 0;
        MovimientoArticulo movimientoArticuloTmp;
        if(movimientoArticuloArrayList.get(position).getMovimiento().equals(v.getContext().getText(R.string.movimiento_entrada))){

            //Buscar movimiento de salida
            movimientoArticuloTmp = new MovimientoArticulo(
                    movimientoArticuloArrayList.get(position).getId_articulo(),
                    movimientoArticuloArrayList.get(position).getId_tipo_stock(),
                    movimientoArticuloArrayList.get(position).getCantidad(),
                    movimientoArticuloArrayList.get(position).getArticulo(),
                    movimientoArticuloArrayList.get(position).getTipo_stock(),
                    v.getContext().getText(R.string.movimiento_salida).toString()
            );
            pos = getPositionItem(movimientoArticuloTmp);

            if(pos>=0){

                StockDB stockDB = new StockDB(database);
                Cursor cursor = stockDB.consultarTodo(idBodega,
                        movimientoArticuloArrayList.get(position).getId_articulo(),
                        movimientoArticuloArrayList.get(position).getId_tipo_stock(),
                        idCentroCosto
                );
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    stockBase = cursor.getFloat(cursor.getColumnIndex("cantidad"));
                }

                cantListNegativo = movimientoArticuloArrayList.get(pos).getCantidad();
                cantListPositivo = movimientoArticuloArrayList.get(position).getCantidad();

                //Log.d("programacion","position:"+position+",pos:"+pos+",Base:"+stockBase+",cantListNegativo:"+cantListNegativo+",cantListPositivo:"+cantListPositivo);
                if(stockBase>0) {
                    if (stockBase - cantListNegativo < 0) {
                        movimientoArticuloTmp.setCantidad(cantListPositivo*(-1));
                        updateItem(movimientoArticuloTmp, pos);
                    }
                }
                else{
                    if(cantListPositivo>=cantListNegativo){
                        movimientoArticuloArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, movimientoArticuloArrayList.size());
                        //position--;
                    }
                }
            }
        }
        //Log.d("programacion","position:"+position+",pos:"+pos+",Base:"+stockBase+",cantListNegativo:"+cantListNegativo+",cantListPositivo:"+cantListPositivo);
        movimientoArticuloArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movimientoArticuloArrayList.size());

    }

    public void updateItem(MovimientoArticulo movimientoArticulo,int position){
        float cantidad = 0;
        cantidad = movimientoArticuloArrayList.get(position).getCantidad()+movimientoArticulo.getCantidad();
        movimientoArticuloArrayList.get(position).setCantidad(cantidad);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movimientoArticuloArrayList.size());
    }

    public ArrayList<MovimientoArticulo> getMovimientoArticuloArrayList(){
        return movimientoArticuloArrayList;
    }

    public int getPositionItem(MovimientoArticulo movimientoArticulo){
        int pos = -1;
        int i=0;
        if (getItemCount() > 0){

            for (MovimientoArticulo m : movimientoArticuloArrayList) {
                //Log.d("programacion","m.articulo:"+String.valueOf(m.getId_articulo())+"="+String.valueOf(movimientoArticulo.getId_articulo()));
                //Log.d("programacion","m.Tipo stock:"+String.valueOf(m.getId_tipo_stock())+"="+String.valueOf(movimientoArticulo.getId_tipo_stock()));
                //Log.d("programacion","m.Movimiento:"+String.valueOf(m.getMovimiento())+"="+String.valueOf(movimientoArticulo.getMovimiento()));
                if (String.valueOf(m.getId_articulo()).contentEquals(String.valueOf(movimientoArticulo.getId_articulo()))
                        && String.valueOf(m.getId_tipo_stock()).contentEquals(String.valueOf(movimientoArticulo.getId_tipo_stock()))
                        && m.getMovimiento().contentEquals(movimientoArticulo.getMovimiento())) {
                    pos = i;
                    return pos;
                }
                i++;
            }
        }
        return pos;
    }

    public class ViewHolderData extends RecyclerView.ViewHolder{
        private TextView itemTipoStock;
        private TextView itemArticulo;
        private TextView itemCantidad;
        private TextView itemMovimiento;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            itemTipoStock   = itemView.findViewById(R.id.txt_tipo_stock);
            itemArticulo    = itemView.findViewById(R.id.txt_descripcion_articulo);
            itemMovimiento  = itemView.findViewById(R.id.txt_tipo_movimiento);
            itemCantidad = itemView.findViewById(R.id.txt_cantidad);
        }

        public void asignarDatos(MovimientoArticulo movimientoArticulo){
            itemTipoStock.setText(movimientoArticulo.getTipo_stock());
            itemArticulo.setText(movimientoArticulo.getArticulo());
            itemMovimiento.setText(movimientoArticulo.getMovimiento());
            itemCantidad.setText(String.valueOf(movimientoArticulo.getCantidad()));
        }

    }
}

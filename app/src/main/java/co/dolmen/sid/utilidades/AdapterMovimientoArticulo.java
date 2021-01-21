package co.dolmen.sid.utilidades;

import android.app.Dialog;
import android.content.DialogInterface;
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
import co.dolmen.sid.entidad.MovimientoArticulo;
import cz.msebera.android.httpclient.client.UserTokenHandler;

public class AdapterMovimientoArticulo extends RecyclerView.Adapter<AdapterMovimientoArticulo.ViewHolderData>{

    private ArrayList<MovimientoArticulo> movimientoArticuloArrayList;

    public AdapterMovimientoArticulo(ArrayList<MovimientoArticulo> movimientoArticuloArrayList) {
        this.movimientoArticuloArrayList = movimientoArticuloArrayList;
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
                        removeItem(position);
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

    public void removeItem(int position){
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
                //Log.d("programacion","m.articulo:"+String.valueOf(m.getId_tipo_stock())+"="+String.valueOf(movimientoArticulo.getId_tipo_stock()));
                //Log.d("programacion","m.articulo:"+String.valueOf(m.getMovimiento())+"="+String.valueOf(movimientoArticulo.getMovimiento()));
                if (String.valueOf(m.getId_articulo()).contentEquals(String.valueOf(movimientoArticulo.getId_articulo())) &&
                        String.valueOf(m.getId_tipo_stock()).contentEquals(String.valueOf(movimientoArticulo.getId_tipo_stock())) &&
                        m.getMovimiento().contentEquals(movimientoArticulo.getMovimiento())) {
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

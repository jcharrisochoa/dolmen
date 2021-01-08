package co.dolmen.sid.utilidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.dolmen.sid.R;
import co.dolmen.sid.entidad.MovimientoArticulo;

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
    public void onBindViewHolder(@NonNull AdapterMovimientoArticulo.ViewHolderData holder, int position) {
        holder.asignarDatos(movimientoArticuloArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return movimientoArticuloArrayList.size();
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

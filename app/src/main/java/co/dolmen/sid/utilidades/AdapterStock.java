package co.dolmen.sid.utilidades;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.dolmen.sid.R;
import co.dolmen.sid.entidad.Stock;

public class AdapterStock  extends RecyclerView.Adapter<AdapterStock.ViewHolderData> {

    private ArrayList<Stock> stocksList;

    public AdapterStock(ArrayList<Stock> stocksList) {
        this.stocksList = stocksList;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_stock,null,false);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        holder.asignarDatos(stocksList.get(position));
    }

    @Override
    public int getItemCount() {
        return stocksList.size();
    }


    public void filterList(ArrayList<Stock> list){
        this.stocksList = list;
        notifyDataSetChanged();
    }

    public class ViewHolderData  extends RecyclerView.ViewHolder{
        private TextView itemTipoStock;
        private TextView itemArticulo;
        private TextView itemCantidad;
        private TextView itemCodigo;

        public ViewHolderData(View itemView) {
            super(itemView);
            itemTipoStock   = itemView.findViewById(R.id.txt_tipo_stock);
            itemArticulo    = itemView.findViewById(R.id.txt_descripcion_articulo);
            itemCodigo  = itemView.findViewById(R.id.txt_codigo_articulo);
            itemCantidad = itemView.findViewById(R.id.txt_cantidad);
        }

        public void asignarDatos(Stock stock){
            itemTipoStock.setText(stock.getTipoStock().getDescripcion());
            itemArticulo.setText(stock.getArticulo().getDescripcion());
            itemCodigo.setText(String.valueOf(stock.getArticulo().getId()));
            itemCantidad.setText(String.valueOf(stock.getCantidad()));
        }
    }
}

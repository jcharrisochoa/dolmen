package co.dolmen.sid.utilidades;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
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
        private TextView itemCentroCosto;

        public ViewHolderData(View itemView) {
            super(itemView);
            itemTipoStock   = itemView.findViewById(R.id.txt_tipo_stock);
            itemArticulo    = itemView.findViewById(R.id.txt_descripcion_articulo);
            itemCantidad    = itemView.findViewById(R.id.txt_cantidad);
            itemCentroCosto = itemView.findViewById(R.id.txt_centro_costo);
        }

        public void asignarDatos(Stock stock){
            itemTipoStock.setText(stock.getTipoStock().getDescripcion());
            itemCentroCosto.setText("("+stock.getCentroCosto().getCodigo()+") "+stock.getCentroCosto().getDescripcionCentroCosto());
            itemArticulo.setText("("+stock.getArticulo().getId()+") "+stock.getArticulo().getDescripcion());
            itemCantidad.setText(String.valueOf(stock.getCantidad()));

            if(stock.getCantidad()<0){
                itemCantidad.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccentCancel));
            }
            else{
                if(stock.getCantidad()>0 && stock.getCantidad()<=1){
                    itemCantidad.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
                }
                else
                    itemCantidad.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
            }
        }
    }
}

package co.dolmen.sid.utilidades;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import co.dolmen.sid.Constantes;
import co.dolmen.sid.R;
import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.Elemento;

public class AdapterElemento extends RecyclerView.Adapter<AdapterElemento.ViewHolderData> implements View.OnClickListener {

    private  ArrayList<Elemento> elementoArrayList;

    private View.OnClickListener listener;

    public AdapterElemento(ArrayList<Elemento> elementoArrayList) {
        this.elementoArrayList = elementoArrayList;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        //Enlaza nuestro adapter con el archivo xml item_actividad
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transformador,null,false);
        view.setOnClickListener(this);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        //Comunica el adaptardor la y la subclase ViewHolderData
        holder.asignarDatos(elementoArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return elementoArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void filterList(ArrayList<Elemento> list){
        this.elementoArrayList = list;
        Log.d(Constantes.TAG,"->List:"+toString());
        notifyDataSetChanged();
    }

    public void notifySetChange() {
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Elemento> list) {
        elementoArrayList.clear();
        elementoArrayList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        //notifyDataSetChanged();
        elementoArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, elementoArrayList.size());
    }

    public ArrayList<Elemento>getElementoArrayList(){
        return elementoArrayList;
    }

    public int getPositionItem(int id_elemento){
        int pos = -1;
        int i = 0;
        for (Elemento e : elementoArrayList) {
            if(e.getId() == id_elemento){
                pos = i;
                return pos;
            }
            i++;
        }
        return pos;
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView itemCT;
        TextView itemPotencia;
        TextView itemElemento;
        TextView itemDireccion;
        TextView itemMT;
        TextView itemCantidad;

        public ViewHolderData(View itemView) {
            super(itemView);
            itemElemento = itemView.findViewById(R.id.item_mobiliario_no);
            itemPotencia = itemView.findViewById(R.id.item_potencia);
            itemMT = itemView.findViewById(R.id.item_mt);
            itemCT = itemView.findViewById(R.id.item_ct);
            itemDireccion = itemView.findViewById(R.id.item_direccion);
            itemCantidad = itemView.findViewById(R.id.item_cantidad);;
        }

        public void asignarDatos(Elemento elemento) {
            itemElemento.setText(elemento.getElemento_no());
            itemDireccion.setText(elemento.getDireccion());
            itemPotencia.setText(String.valueOf(elemento.getPotenciaTransformador()));
            itemMT.setText(elemento.getPlacaMT());
            itemCT.setText(elemento.getPlacaCT());
        }

    }

    @Override
    public String toString(){
        String cad = "";
        int i = 0;
        for(Elemento e:elementoArrayList) {
            cad = cad +"pos:"+ i + "Act:"+ e.getId()+",Dir:"+e.getDireccion();
            i++;
        }
        return cad;
    }
}

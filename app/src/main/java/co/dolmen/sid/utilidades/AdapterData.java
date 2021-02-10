package co.dolmen.sid.utilidades;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.dolmen.sid.R;
import co.dolmen.sid.entidad.ActividadOperativa;
import co.dolmen.sid.entidad.MovimientoArticulo;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderData> implements View.OnClickListener {

    private  ArrayList<ActividadOperativa> actividadOperativaArrayList;

    private View.OnClickListener listener;

    public AdapterData(ArrayList<ActividadOperativa> actividadOperativaArrayList) {
        this.actividadOperativaArrayList = actividadOperativaArrayList;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        //Enlaza nuestro adapter con el archivo xml item_actividad
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_operativa,null,false);
        view.setOnClickListener(this);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        //Comunica el adaptardor la y la subclase ViewHolderData
        holder.asignarDatos(actividadOperativaArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return actividadOperativaArrayList.size();
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

    public void filterList(ArrayList<ActividadOperativa> list){
        this.actividadOperativaArrayList = list;
        notifyDataSetChanged();
    }

    public void notifySetChange() {
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<ActividadOperativa> list) {
        actividadOperativaArrayList.clear();
        actividadOperativaArrayList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        //notifyDataSetChanged();
        actividadOperativaArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, actividadOperativaArrayList.size());
    }

    public int getPositionItem(int id_actividad){
        int pos = -1;
        int i = 0;
        for (ActividadOperativa ao : actividadOperativaArrayList) {
            if(ao.getIdActividad() == id_actividad){
                pos = i;
                return pos;
            }
            i++;
        }
        return pos;
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView itemActividad;
        TextView itemFecha;
        TextView itemElemento;
        TextView itemDireccion;
        TextView itemEstado;
        TextView itemPrograma;
        TextView itemTipoOperacion;
        ImageView itemImgElemento;

        public ViewHolderData(View itemView) {
            super(itemView);
            itemActividad = itemView.findViewById(R.id.item_actividad);
            itemFecha = itemView.findViewById(R.id.item_fecha);
            itemElemento = itemView.findViewById(R.id.item_elemento_no);
            itemDireccion = itemView.findViewById(R.id.item_direccion);
            itemEstado = itemView.findViewById(R.id.item_estado);
            itemPrograma = itemView.findViewById(R.id.item_programa);
            itemTipoOperacion = itemView.findViewById(R.id.item_tipo_operacion);
            itemImgElemento = itemView.findViewById(R.id.item_img_elemento);
        }

        public void asignarDatos(ActividadOperativa actividadOperativa) {
            itemActividad.setText(String.valueOf(actividadOperativa.getIdActividad()));
            itemDireccion.setText(actividadOperativa.getDireccion());
            itemElemento.setText(String.valueOf(actividadOperativa.getElemento().getElemento_no()));
            itemEstado.setText(actividadOperativa.getEstadoActividad().getDescripcion());
            itemFecha.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actividadOperativa.getFechaActividad()));
            itemPrograma.setText("Prg:"+String.valueOf(actividadOperativa.getPrograma().getId()));
            itemTipoOperacion.setText(actividadOperativa.getTipoActividad().getDescripcion());
            if(actividadOperativa.getEstadoActividad().getId()!=2){
                itemImgElemento.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorLightPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                itemEstado.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            }
            else{
                if(actividadOperativa.getPendienteSincronizar().contentEquals("S")){
                    itemImgElemento.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    itemEstado.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorVerifed));
                }
                else {
                    itemImgElemento.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorVerifed), android.graphics.PorterDuff.Mode.SRC_IN);
                    itemEstado.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorVerifed));
                }
            }
        }

    }
}

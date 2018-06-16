package ar.com.eduit.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.reservassala.R;

public class AlquilerAdapter  extends BaseAdapter {

    private List<ODeAlquiler> alquileres;

    public AlquilerAdapter(List<ODeAlquiler> alquileres) {
        this.alquileres = alquileres;
    }

    public List<ODeAlquiler> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<ODeAlquiler> alquileres) {
        this.alquileres = alquileres;
    }

    @Override
    public int getCount() {
        return alquileres.size();
    }

    @Override
    public Object getItem(int position) {
        return alquileres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alquileres.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvAlquiler;
        TextView tvOdAlquilerID;


        //tomo el layout de item a mostrar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alquiler, parent, false);
        // tomo la reserva segun la posicion.
        ODeAlquiler alquiler = (ODeAlquiler) getItem(position);

        tvAlquiler = (TextView) view.findViewById(R.id.tvAlquiler);
        tvOdAlquilerID = (TextView) view.findViewById(R.id.tvOdAlquilerID);
        tvOdAlquilerID.setText(String.valueOf(alquiler.getId()));
        tvAlquiler.setText(alquiler.getName());
        return view;
    }


    public int getPosition (long id){

        int res = -1;
        for (int i=0 ; i<alquileres.size();i++){
            if (alquileres.get(i).getId() == id){
                return i;
            }

        }

        return res;
    }
}

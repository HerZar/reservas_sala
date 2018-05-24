package ar.com.eduit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.reservassala.R;

public class AlquilerArrayAdapter extends ArrayAdapter<ODeAlquiler> {

    private Context contexto;
    private List<ODeAlquiler> alquileres;

    public AlquilerArrayAdapter(@NonNull Context context, int resource, @NonNull List<ODeAlquiler> objects) {

        super(context, resource, objects);
        contexto = context;
        alquileres= objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView tvAlquiler;
        TextView tvOdAlquilerID;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alquiler, parent, false);
        ODeAlquiler alquiler = (ODeAlquiler) getItem(position);
        tvAlquiler = (TextView) view.findViewById(R.id.tvAlquiler);
        tvOdAlquilerID = (TextView) view.findViewById(R.id.tvOdAlquilerID);
        tvOdAlquilerID.setText(String.valueOf(alquiler.getId()));
        tvAlquiler.setText(alquiler.getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView tvAlquiler;
        TextView tvOdAlquilerID;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alquiler, parent, false);
        ODeAlquiler alquiler = (ODeAlquiler) getItem(position);
        tvAlquiler = (TextView) view.findViewById(R.id.tvAlquiler);
        tvOdAlquilerID = (TextView) view.findViewById(R.id.tvOdAlquilerID);
        tvOdAlquilerID.setText(String.valueOf(alquiler.getId()));
        tvAlquiler.setText(alquiler.getName());
        return view;

    }

    @Override
    public long getItemId(int position) {
        return alquileres.get(position).getId();
    }

    @Nullable
    @Override
    public ODeAlquiler getItem(int position) {
        return alquileres.get(position);
    }
}

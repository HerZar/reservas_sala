package ar.com.eduit.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.reservassala.R;

public class ReservaAdapter extends BaseAdapter {


    private List<Reserva> reservas;
    private LinearLayout llmainl;
    private Context contexto;


    public ReservaAdapter(List<Reserva> reservas, Context cont) {

        this.reservas = reservas;
        this.contexto = cont;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Object getItem(int position) {
        return reservas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        // auxiliares
        String dayAux = "";
        String monthAux = "";
        String yearAux = "";
        String hourAux = "";
        String minAux = "";


        //tomo el layout de item a mostrar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reserva, parent, false);
        // tomo la reserva segun la posicion.
        Reserva reserva = (Reserva) getItem(position);

        //Referencio las vistas
        TextView twOdAlquiler = (TextView) view.findViewById(R.id.twOdAlquiler);
        TextView twNombre = (TextView) view.findViewById(R.id.twNombre);
        TextView twFecha = (TextView) view.findViewById(R.id.twFecha);
        TextView twHorario = (TextView) view.findViewById(R.id.twHorario);
        TextView twHorarioFin = (TextView) view.findViewById(R.id.twHorarioFin);

        llmainl = (LinearLayout) view.findViewById(R.id.mainLayout);

        //Cargo el objeto de alquiler.
        ODeAlquiler alquiler = null;
        try {
            alquiler = RepoODAlquiler.getInstance(contexto).getODeAlquilerById(reserva.getOdalquilerID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        twOdAlquiler.setText(contexto.getResources().getString(R.string.Reserva) + alquiler.getName());
        //Cargo Nombre al cual esta asignada la reserva al view.
        //twNombre.setText(Resources.getSystem().getString(R.string.reservado_a) + reserva.getNombre());
        twNombre.setText(parent.getContext().getString(R.string.reservado_a) +" "+ reserva.getNombre());

        //Cargo Fecha de la reserva al view.
        if (reserva.getInicio().get(Calendar.DAY_OF_MONTH) < 10) {
            dayAux = dayAux + "0";
        }
        dayAux = dayAux + reserva.getInicio().get(Calendar.DAY_OF_MONTH);
        int auxMonth = reserva.getInicio().get(Calendar.MONTH) + 1;
        if (auxMonth < 10) {
            monthAux = monthAux + "0";
        }
        monthAux = monthAux + (auxMonth);
        if (reserva.getInicio().get(Calendar.YEAR) < 10) {
            yearAux = yearAux + "0";
        }
        yearAux = yearAux + reserva.getInicio().get(Calendar.YEAR);
        String Fecha = "";
        if (reserva.isFijo()) {
            Fecha = parent.getContext().getString(R.string.fijo_los)  +" "+ UtilCalendar.getDiaSemana(parent.getContext(),reserva.getInicio().get(Calendar.DAY_OF_WEEK) - 1) +
                    " "+ parent.getContext().getString(R.string.a_partir_del)  + " " + dayAux + "/" + monthAux + "/" + yearAux;
        } else {
            Fecha = dayAux + "/" + monthAux + "/" + yearAux
                    + " " + UtilCalendar.getDiaSemana(parent.getContext(),reserva.getInicio().get(Calendar.DAY_OF_WEEK) - 1);
        }

        twFecha.setText(Fecha);

        //Cargo horario de Inicio de reserva al view.
        hourAux = "";
        minAux = "";
        if (reserva.getInicio().get(Calendar.HOUR_OF_DAY) < 10) {
            hourAux = hourAux + "0";
        }
        hourAux = hourAux + reserva.getInicio().get(Calendar.HOUR_OF_DAY);
        if (reserva.getInicio().get(Calendar.MINUTE) < 10) {
            minAux = minAux + "0";
        }
        minAux = minAux + reserva.getInicio().get(Calendar.MINUTE);
        String hora = hourAux + ":" + minAux;
        twHorario.setText(parent.getContext().getString(R.string.de_desde)  +" "+ hora);

        //Cargo horario de fin de reserva al view.
        hourAux = "";
        minAux = "";
        if (reserva.getFin().get(Calendar.HOUR_OF_DAY) < 10) {
            hourAux = hourAux + "0";
        }
        hourAux = hourAux + reserva.getFin().get(Calendar.HOUR_OF_DAY);
        if (reserva.getFin().get(Calendar.MINUTE) < 10) {
            minAux = minAux + "0";
        }
        minAux = minAux + reserva.getFin().get(Calendar.MINUTE);
        hora = hourAux + ":" + minAux;
        twHorarioFin.setText( " "+ parent.getContext().getString(R.string.a_hasta)  +" "+ hora);

        if (reserva.isForToday()){
            llmainl.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.fondo_reservas_dia));
//            final RippleDrawable ripple = (RippleDrawable) llmainl.getBackground();
//            llmainl.setOnTouchListener(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    switch (view.getId()){
//                        case  R.id.mainLayout:
//                            ripple.setHotspot(motionEvent.getX(),motionEvent.getY());
//                            ripple.setColor(ColorStateList.valueOf(parent.getResources().getColor(R.color.fondo_aplicacion)));
//                    }
//                    return false;
//                }
//
//            });
        }

        return view;
    }


}

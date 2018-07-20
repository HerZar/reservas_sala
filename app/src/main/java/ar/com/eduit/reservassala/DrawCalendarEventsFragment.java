package ar.com.eduit.reservassala;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.UtilCalendar;


public class DrawCalendarEventsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_AMOUNTOFDAYS = "amountofdays";
    private static final String ARG_CURRENTDAY = "currentday";
    private static final String ARG_ODALQUILER = "odalquiler";
    private static final int DEF_DESPLAZAMIENTOIZQ = 30;


    private int amountOfDays;
    private Calendar currentDay;
    private ODeAlquiler oda;

    private ViewGroup clagenda;
    private RelativeLayout reservaDeCalendario;

    private int widthX;
    //private List<View> viewListadoReservas;

    public DrawCalendarEventsFragment() {
        // Required empty public constructor
    }


    public static DrawCalendarEventsFragment newInstance(Integer param1, Calendar param2, ODeAlquiler param3) {
        DrawCalendarEventsFragment fragment = new DrawCalendarEventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_AMOUNTOFDAYS, param1);
        args.putSerializable(ARG_CURRENTDAY, param2);
        args.putSerializable(ARG_ODALQUILER, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amountOfDays = getArguments().getInt(ARG_AMOUNTOFDAYS);
            currentDay = (Calendar) getArguments().getSerializable(ARG_CURRENTDAY);
            oda = (ODeAlquiler) getArguments().getSerializable(ARG_ODALQUILER);
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            widthX = width / (int) getResources().getDisplayMetrics().density;
            widthX = widthX - 10;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draw_calendar_events, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clagenda = getView().findViewById(R.id.clagenda);
        try {
            loadReservaCalendarDay(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadReservaCalendarDay(Context context) throws Exception {

        List<Reserva> listRes = null;
        if (filtroUnObjectoDeAlquiler(oda.getId())) {
            listRes = RepoReserva.getInstance(context).
                    getReservasDelDiaCorrienteYFijosFiltrado(currentDay, oda.getId());
        } else {
            listRes = RepoReserva.getInstance(context).
                    getReservasDelDiaCorrienteYFijos(currentDay);
        }
        listRes = filtrarFijosDeOtroDiaDeSemana(listRes);

        List<List<Reserva>> matriz = loadMatrizReservas(listRes);

        dibujarCalendario(matriz);
    }

    private List<List<Reserva>> loadMatrizReservas(List<Reserva> lista) {
        List<List<Reserva>> matriz;
        matriz = new ArrayList<>();

        for (Reserva reserva : lista) {
            int col = 0;
            boolean colocado = false;
            while (!colocado) {
                if (matrizNoTieneLaColumna(matriz, col)) {
                    matriz.add(new ArrayList<Reserva>());
                }
                for (Reserva rec : matriz.get(col)){
                    if (estaSuperpuesto(reserva, rec)){
                        col = col + 1;
                    } else {
                        matriz.get(col).add(reserva);
                        colocado = true;
                    }
                }

            }
        }
        return matriz;
    }

    private void dibujarCalendario(List<List<Reserva>> matriz) {

        for (List<Reserva> li : matriz) {
            for (Reserva reserva : li) {
                int solapados = getCantSolapados(reserva, matriz, matriz.indexOf(li));
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                reservaDeCalendario = (RelativeLayout) inflater.inflate(R.layout.calendar_reserva_view, null, false);

                cargarReserva(reserva);
                int anchoX = ((widthX - DEF_DESPLAZAMIENTOIZQ) / amountOfDays) / (solapados+matriz.indexOf(li));
                int despX = (anchoX * matriz.indexOf(li));
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        getStart(reserva),
                        getResources().getDisplayMetrics());
                params.setMarginStart((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, despX, getResources().getDisplayMetrics()));
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        getSize(reserva)
                        , getResources().getDisplayMetrics());
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        anchoX
                        , getResources().getDisplayMetrics());

                reservaDeCalendario.setLayoutParams(params);

                reservaDeCalendario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TextView idReserva = (TextView) view.findViewById(R.id.tvreservaid);


                        Toast.makeText(getContext(), "Tocaste la reserva " + idReserva.getText(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


                clagenda.addView(reservaDeCalendario);
                //viewListadoReservas.add(reservaDeCalendario);
            }
        }
    }

    private int getCantSolapados(Reserva reserva, List<List<Reserva>> matriz, int currentColumn) {
        int maximo = 0;
        //for (List<Reserva> lista: matriz) {

        if (currentColumn < matriz.size()) {
            for (Reserva rec : matriz.get(currentColumn + 1)) {
                if (estaSuperpuesto(reserva, rec)) {
                    int respuesta = getCantSolapados(rec, matriz, currentColumn + 1);
                    if (respuesta > maximo) {
                        maximo = respuesta;
                    }
                }
            }
        }

        return maximo + 1;
    }

    private boolean estaSuperpuesto(Reserva res, Reserva item) {
        //Recupero lista de reservas para controlar que no exista una en el mismo dia y periodo.


        if (res.compareToIgnoringODalquiler(item) == 0) {
            if (!item.isFijo() && res.isFijo()) {
                if ((UtilCalendar.compareDateTo(item.getInicio(), res.getInicio()) >= 0)
                        && (UtilCalendar.compareDateTo(item.getInicio(), res.getInicio()) != 2)) {
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;

    }

    private void cargarReserva(Reserva reserva) {

        TextView reservaId = reservaDeCalendario.findViewById(R.id.tvreservaid);
        TextView reservaNombre = reservaDeCalendario.findViewById(R.id.tvreservanombre);
        TextView reservaOda = reservaDeCalendario.findViewById(R.id.tvreservaoda);

        reservaId.setText(String.valueOf(reserva.getId()));
        reservaNombre.setText(reserva.getNombre());

        //Cargo el objeto de alquiler.
        ODeAlquiler alquiler = null;
        try {
            alquiler = RepoODAlquiler.getInstance(getContext()).getODeAlquilerById(reserva.getOdalquilerID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        reservaOda.setText(alquiler.getName());

    }


    private int getStart(Reserva item) {

        Calendar primeraHora = Calendar.getInstance();
        primeraHora.setTime(item.getInicio().getTime());
        primeraHora.set(Calendar.MILLISECOND, 0);
        primeraHora.set(Calendar.SECOND, 0);
        primeraHora.set(Calendar.MINUTE, 0);
        primeraHora.set(Calendar.HOUR_OF_DAY, 0);

        long startTime = (item.getInicio().getTime().getTime() - primeraHora.getTimeInMillis());
        startTime = (startTime / 1000) / 60;

        return (int) startTime;
    }

    private int getSize(Reserva item) {

        long startTime = (item.getFin().getTime().getTime() - item.getInicio().getTime().getTime());
        startTime = (startTime / 1000) / 60;

        return (int) startTime;
    }

    private boolean matrizNoTieneLaColumna(List<List<Reserva>> matriz, int col) {
        return (matriz.size() < col + 1);
    }

    private boolean filtroUnObjectoDeAlquiler(long odaid) {
        return (odaid > 0);
    }

    private List<Reserva> filtrarFijosDeOtroDiaDeSemana(List<Reserva> listres) {
        List<Reserva> listaFiltrada = new ArrayList<>();
        for (Reserva a : listres) {
            if (UtilCalendar.sonDelMismoDiaDeLaSemana(a.getInicio(), currentDay)) {
                listaFiltrada.add(a);
            }
        }
        return listaFiltrada;
    }


//    private void limpiarReservas(){
//        for (View reserva: viewListadoReservas) {
//            clagenda.removeView(reserva);
//        }
//        viewListadoReservas.clear();
//    }


}

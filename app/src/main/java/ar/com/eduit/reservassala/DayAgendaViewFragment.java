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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.AlquilerAdapter;
import ar.com.eduit.utils.UtilCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayAgendaViewFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    //Elementos de lista de Objetos de Alquiler
    private List<ODeAlquiler> lOdeAlquiler;
    private AlquilerAdapter odaAdapter;
    private Spinner psODeAlquiler;
    //-----------------------------

    private Calendar currentDay;

    private Button btnAnterior;
    private Button btnSiguiente;
    private TextView tvCurrentDay;


    //---------------------------------
    //--Objetos del calendario.
    private ViewGroup clagenda;
    private RelativeLayout reservaDeCalendario;



    private int widthX;
    private List<View> viewListadoReservas;



    public DayAgendaViewFragment() {
        // Required empty public constructor
    }

    public static DayAgendaViewFragment newInstance(){
        DayAgendaViewFragment fragment = new DayAgendaViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewListadoReservas=new ArrayList<>();
        currentDay = Calendar.getInstance();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        widthX = width/(int)getResources().getDisplayMetrics().density ;
        widthX = widthX-10;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_agenda_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializo la lista de objetos de alquiler
        psODeAlquiler = (Spinner) getView().findViewById(R.id.sp_odalquiler);
        lOdeAlquiler = new ArrayList<>();
        odaAdapter = new AlquilerAdapter(lOdeAlquiler);
        psODeAlquiler.setAdapter(odaAdapter);

        btnAnterior = (Button) getView().findViewById(R.id.btndiaanterior);
        btnSiguiente = (Button) getView().findViewById(R.id.btndiasiguiente);
        tvCurrentDay = (TextView) getView().findViewById(R.id.tvdiacorriente);
        btnAnterior.setOnClickListener(cargarDiaAnterior());
        btnSiguiente.setOnClickListener(cargarDiaSiguiente());
        tvCurrentDay.setText(cargarTextoFecha());

        clagenda = getView().findViewById(R.id.clagenda);
        cargarHorarios(clagenda);

    }

    private String cargarTextoFecha() {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        return format.format(currentDay.getTime());
    }

    private View.OnClickListener cargarDiaSiguiente() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentDay.add(Calendar.DATE,1);
                tvCurrentDay.setText(cargarTextoFecha());
                limpiarReservas();
                try {
                    loadReservaCalendarDay(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener cargarDiaAnterior() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentDay.add(Calendar.DATE,-1);
                tvCurrentDay.setText(cargarTextoFecha());
                limpiarReservas();
                try {
                    loadReservaCalendarDay(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } ;
    }

    @Override
    public void onResume() {


        try {
            loadIOdeAlquilerSpinner(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            loadReservaCalendarDay(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();

    }


    private void loadIOdeAlquilerSpinner(Context context) throws Exception {
        lOdeAlquiler = RepoODAlquiler.getInstance(context).getAllODeAlquilers();
        if (lOdeAlquiler.size() !=1) {
            lOdeAlquiler.add(new ODeAlquiler(getContext().getResources().getString(R.string.todos)));
        }

        odaAdapter.setAlquileres(lOdeAlquiler);
        psODeAlquiler.setPopupBackgroundResource(R.color.fondo_aplicacion);
        odaAdapter.notifyDataSetChanged();
        psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
    }

    private void loadReservaCalendarDay(Context context) throws Exception {
        ODeAlquiler oda = (ODeAlquiler) psODeAlquiler.getSelectedItem();
        List<Reserva> listRes=null;
        if (filtroUnObjectoDeAlquiler(oda.getId())){
            listRes = RepoReserva.getInstance(context).
                    getReservasDelDiaCorrienteYFijosFiltrado(currentDay,oda.getId());
        }
        else {
            listRes = RepoReserva.getInstance(context).
                    getReservasDelDiaCorrienteYFijos(currentDay);
        }
        listRes= filtrarFijosDeOtroDiaDeSemana(listRes);

        List<List<Reserva>> matriz = loadMatrizReservas(listRes);

        dibujarCalendario(matriz);
    }


    private List<List<Reserva>> loadMatrizReservas( List<Reserva> lista){
        List<List<Reserva>> matriz;
        matriz = new ArrayList<>();

        for (Reserva reserva: lista) {
            int col = 0;
            boolean colocado = false;
            while (!colocado){
                if(matrizNoTieneLaColumna(matriz,col)){
                    matriz.add(new ArrayList<Reserva>());
                }
                if(estaSuperpuesto(reserva,matriz.get(col))){
                    col=col+1;
                }else{
                    matriz.get(col).add(reserva);
                    colocado=true;
                }
            }
        }
        return matriz;
    }


    private void dibujarCalendario(List<List<Reserva>> matriz){

        for (List<Reserva> li: matriz) {
            for (Reserva reserva: li) {
                int solapados = getCantSolapados(reserva, matriz)+1;
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                reservaDeCalendario = (RelativeLayout) inflater.inflate(R.layout.calendar_reserva_view, null, false);

                cargarReserva(reserva);
                int anchoX = (widthX-50)/solapados;
                int despX = 50+(anchoX*matriz.indexOf(li));
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        getStart(reserva),
                        getResources().getDisplayMetrics());
                params.setMarginStart((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, despX, getResources().getDisplayMetrics()));
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        getSize(reserva)
                        , getResources().getDisplayMetrics());
                params.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        anchoX
                        , getResources().getDisplayMetrics());

                reservaDeCalendario.setLayoutParams(params);

                reservaDeCalendario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TextView idReserva = (TextView) view.findViewById(R.id.tvreservaid);


                        Toast.makeText(getContext(),"Tocaste la reserva "+ idReserva.getText(),
                                Toast.LENGTH_SHORT).show();
                    }
                });



                clagenda.addView(reservaDeCalendario);
                viewListadoReservas.add(reservaDeCalendario);
            }
        }
    }

    private int getCantSolapados(Reserva reserva, List<List<Reserva>> matriz) {
        int respuesta=0;
        for (List<Reserva> lista: matriz) {
            if(estaSuperpuesto(reserva, lista)){
                respuesta = respuesta+1;
            }
        }
        return respuesta;
    }

    private boolean estaSuperpuesto(Reserva res, List<Reserva> listRes) {
        //Recupero lista de reservas para controlar que no exista una en el mismo dia y periodo.
        boolean resultado = false;
            for (Reserva item : listRes) {
                if (res.compareToIgnoringODalquiler(item) == 0) {
                    if (!item.isFijo() && res.isFijo()) {
                        if ((UtilCalendar.compareDateTo(item.getInicio(), res.getInicio()) >= 0)
                                && (UtilCalendar.compareDateTo(item.getInicio(), res.getInicio()) != 2)) {
                            resultado = true;
                        }
                    } else {
                        resultado = true;
                    }
                }
            }
        return resultado;

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

    private boolean matrizNoTieneLaColumna(List<List<Reserva>> matriz, int col){
        return (matriz.size()<col+1);
    }

    private boolean filtroUnObjectoDeAlquiler(long odaid){
        return (odaid>0);
    }

    private List<Reserva> filtrarFijosDeOtroDiaDeSemana(List<Reserva> listres){
        List<Reserva> listaFiltrada = new ArrayList<>();
        for (Reserva a: listres) {
            if(UtilCalendar.sonDelMismoDiaDeLaSemana(a.getInicio(),currentDay)){
                listaFiltrada.add(a);
            }
        }
        return listaFiltrada;
    }

    private void cargarHorarios(View view) {

        for (int i = 0; i < 24; i++) {
            View include = view.findViewById(getLayoutHourID(i));
            TextView hour = (TextView) include.findViewById(R.id.tvhourofday);

            hour.setText(i + "hs ");
        }

    }

    private int getLayoutHourID(int hour) {
        int respuesta = 0;

        switch (hour) {
            case 0:
                respuesta = R.id.hour1;
                break;
            case 1:
                respuesta = R.id.hour2;
                break;
            case 2:
                respuesta = R.id.hour3;
                break;
            case 3:
                respuesta = R.id.hour4;
                break;
            case 4:
                respuesta = R.id.hour5;
                break;
            case 5:
                respuesta = R.id.hour6;
                break;
            case 6:
                respuesta = R.id.hour7;
                break;
            case 7:
                respuesta = R.id.hour8;
                break;
            case 8:
                respuesta = R.id.hour9;
                break;
            case 9:
                respuesta = R.id.hour10;
                break;
            case 10:
                respuesta = R.id.hour11;
                break;
            case 11:
                respuesta = R.id.hour12;
                break;
            case 12:
                respuesta = R.id.hour13;
                break;
            case 13:
                respuesta = R.id.hour14;
                break;
            case 14:
                respuesta = R.id.hour15;
                break;
            case 15:
                respuesta = R.id.hour16;
                break;
            case 16:
                respuesta = R.id.hour17;
                break;
            case 17:
                respuesta = R.id.hour18;
                break;
            case 18:
                respuesta = R.id.hour19;
                break;
            case 19:
                respuesta = R.id.hour20;
                break;
            case 20:
                respuesta = R.id.hour21;
                break;
            case 21:
                respuesta = R.id.hour22;
                break;
            case 22:
                respuesta = R.id.hour23;
                break;
            case 23:
                respuesta = R.id.hour24;
                break;

        }

        return respuesta;
    }

    private void limpiarReservas(){
        for (View reserva: viewListadoReservas) {
            clagenda.removeView(reserva);
        }
        viewListadoReservas.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            //resumeList(getContext());
            //Luego de seleccionar un Ode alquiler del spinner tengo que revalcular
            // las reservas mostradas en el calendario
            // TO DO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

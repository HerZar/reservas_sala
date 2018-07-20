package ar.com.eduit.reservassala;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.utils.AlquilerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayAgendaViewFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final int AMOUNT_OF_DAYS_DROWED = 1;
    //Elementos de lista de Objetos de Alquiler
    private List<ODeAlquiler> lOdeAlquiler;
    private AlquilerAdapter odaAdapter;
    private Spinner psODeAlquiler;
    //-----------------------------

    private static final int AMOUNT_OF_DAYS = 1;
    private Calendar currentDay;

    private Button btnAnterior;
    private Button btnSiguiente;
    private TextView tvCurrentDay;


    //---------------------------------
    //--Objetos del calendario.
    private ViewGroup clagenda;
    private RelativeLayout reservaDeCalendario;



    //   private int widthX;
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

        psODeAlquiler.setOnItemSelectedListener(this);

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
                getChildFragmentManager().beginTransaction().replace(R.id.fldrawcalendarevents,
                        DrawCalendarEventsFragment.newInstance(AMOUNT_OF_DAYS_DROWED,currentDay,(ODeAlquiler) psODeAlquiler.getSelectedItem())).commit();

            }
        };
    }

    private View.OnClickListener cargarDiaAnterior() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentDay.add(Calendar.DATE,-1);
                tvCurrentDay.setText(cargarTextoFecha());
                getChildFragmentManager().beginTransaction().replace(R.id.fldrawcalendarevents,
                        DrawCalendarEventsFragment.newInstance(AMOUNT_OF_DAYS_DROWED,currentDay,(ODeAlquiler) psODeAlquiler.getSelectedItem())).commit();

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

        getChildFragmentManager().beginTransaction().replace(R.id.fldrawcalendarevents,
                DrawCalendarEventsFragment.newInstance(AMOUNT_OF_DAYS_DROWED,currentDay,(ODeAlquiler) psODeAlquiler.getSelectedItem())).commit();

        super.onResume();

    }


    private void cargarHorarios(View view) {

        for (int i = 0; i < 24; i++) {
            View include = view.findViewById(getLayoutHourID(i));
            TextView hour = (TextView) include.findViewById(R.id.tvhourofday);

            hour.setText(i + "hs ");
        }

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            //resumeList(getContext());
            //Luego de seleccionar un Ode alquiler del spinner tengo que revalcular
            // las reservas mostradas en el calendario
            // TO DO
            getChildFragmentManager().beginTransaction().replace(R.id.fldrawcalendarevents,
                    DrawCalendarEventsFragment.newInstance(AMOUNT_OF_DAYS_DROWED,currentDay,(ODeAlquiler) psODeAlquiler.getSelectedItem())).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

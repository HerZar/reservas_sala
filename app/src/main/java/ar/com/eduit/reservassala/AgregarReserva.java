package ar.com.eduit.reservassala;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.AlquilerAdapter;
import ar.com.eduit.utils.DatePickerFragment;
import ar.com.eduit.utils.ReservaAdapter;
import ar.com.eduit.utils.TimePickerFragment;
import ar.com.eduit.utils.UtilCalendar;

public class AgregarReserva extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView idTv;
    private EditText nombreEt;
    private EditText fechaEt;
    private EditText horaEt;
    private EditText horaEtFin;
    private CheckBox fijoCb;
    private LinearLayout llReservados;

    private ListView lvListaReservas;
    private ReservaAdapter resAdapter;
    private List<Reserva> listRes;

    //Elementos de lista de Objetos de Alquiler
    private List<ODeAlquiler> lOdeAlquiler;
    private AlquilerAdapter odaAdapter;
    private Spinner psODeAlquiler;
    //-----------------------------

    private long itemID;
    private Dialog dialogoAgregar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_reserva);
        //getSupportActionBar().hide();
        idTv = (TextView) findViewById(R.id.tvID);
        nombreEt = (EditText) findViewById(R.id.etNombre);
        fechaEt = (EditText) findViewById(R.id.etFecha);
        horaEt = (EditText) findViewById(R.id.etHora);
        horaEtFin = (EditText) findViewById(R.id.etHoraFin);
        llReservados = (LinearLayout) findViewById(R.id.llReservados);
        lvListaReservas = (ListView) findViewById(R.id.lvListaReservas);
        dialogoAgregar = new Dialog(AgregarReserva.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        //deshabilitamos el título por defecto
        dialogoAgregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        //dialogoAgregar.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        dialogoAgregar.setContentView(R.layout.init_alquiler_dialog);

        // creo el radio butom para saber si la sala es fija en ese dia y horario.
        fijoCb = (CheckBox) findViewById(R.id.cbFijo);
        psODeAlquiler = (Spinner) findViewById(R.id.sp_odalquiler);
        psODeAlquiler.setOnItemSelectedListener(this);
        //Inicializo la lista de objetos de alquiler
        lOdeAlquiler = new ArrayList<>();
        odaAdapter = new AlquilerAdapter(lOdeAlquiler);
        psODeAlquiler.setAdapter(odaAdapter);
        try {
            resumeAlquileres(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            itemID = getIntent().getExtras().getLong("itemID");
            cargarVistas(itemID);
        } catch (Exception e) {
            //Continua sin cargar una reserva... reserva nueva.
            e.printStackTrace();
        }

        //Inicializo la lista de Reservas
        listRes = new ArrayList<>();
        resAdapter = new ReservaAdapter(listRes, getApplicationContext());
        lvListaReservas.setAdapter(resAdapter);
    }

    //Este metodo carga una reserva sobre la vista para la edicion de la misma.
    private void cargarVistas(long itemID) {
        try {
            Reserva reserva = RepoReserva.getInstance(getApplicationContext()).getReservaById(itemID);
            //Cargo el ID de la reserva.

            //Cargo el select del spinner
            AlquilerAdapter adapter = (AlquilerAdapter) psODeAlquiler.getAdapter();
            psODeAlquiler.setSelection(adapter.getPosition(reserva.getOdalquilerID()));;
            idTv.setText(String.valueOf(reserva.getId()));
            //Cargo Nombre al cual esta asignada la reserva al view.
            nombreEt.setText(reserva.getNombre());

            String dayAux = "";
            String monthAux = "";
            String yearAux = "";
            String hourAux = "";
            String minAux = "";


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
            String Fecha = dayAux + "/" + monthAux + "/" + yearAux
                    + " " + UtilCalendar.getDiaSemana(getApplicationContext(), reserva.getInicio().get(Calendar.DAY_OF_WEEK) - 1);


            fechaEt.setText(Fecha);

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
            horaEt.setText(hora);

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
            horaEtFin.setText(hora);
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),"no se encontro la reserva",Toast.LENGTH_SHORT);
        }
    }

    public void etFechaClick(View view) {
        showDatePickerDialog(view);
    }

    private void showDatePickerDialog(View view) {

        final EditText dayAux = (EditText) findViewById(view.getId());
        int day;
        int month;
        int year;

        if (!fechaEt.getText().toString().isEmpty() && !fechaEt.getText().toString().equals(null)) {
            day = Integer.parseInt(dayAux.getText().toString().substring(0, 2));
            month = Integer.parseInt(dayAux.getText().toString().substring(3, 5));
            month = month - 1;
            year = Integer.parseInt(dayAux.getText().toString().substring(6, 10));
        } else {
            day = -1;
            month = -1;
            year = -1;
        }

        DatePickerFragment newFragment = DatePickerFragment.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        String dayAux = "";
                        String monthAux = "";
                        String yearAux = "";
                        Calendar auxFecha = Calendar.getInstance();
                        auxFecha.set(Calendar.DAY_OF_MONTH, day);
                        auxFecha.set(Calendar.MONTH, month);
                        auxFecha.set(Calendar.YEAR, year);

                        if (day < 10) {
                            dayAux = dayAux + "0";
                        }
                        dayAux = dayAux + day;
                        if (month + 1 < 10) {
                            monthAux = monthAux + "0";
                        }
                        monthAux = monthAux + (month + 1);
                        if (year < 10) {
                            yearAux = yearAux + "0";
                        }
                        yearAux = yearAux + year;
                        final String selectedDate = dayAux + "/" + monthAux + "/" + yearAux
                                + " " + UtilCalendar.getDiaSemana(getApplicationContext(), auxFecha.get(Calendar.DAY_OF_WEEK) - 1);

                        fechaEt.setText(selectedDate);
                    }
                }, year, month, day);
        newFragment.show(getFragmentManager(), "datePicker");

    }


    public void etHoraClick(View view) {
        showTimePieckerDialog(view);

    }

    public void etHoraClickFin(View view) {
        showTimePieckerDialog(view);
    }

    /*este codigo dibuja los dialogos del datepicker para las fechas.
     * @param view : esta variable permite referenciar la vista que invoca al dialogo.
     */
    private void showTimePieckerDialog(View view) {
        final EditText timeAux = (EditText) findViewById(view.getId());

        int hour;
        int min;
        if (!timeAux.getText().toString().isEmpty() && !timeAux.getText().toString().equals(null)) {
            hour = Integer.parseInt(timeAux.getText().toString().substring(0, 2));
            min = Integer.parseInt(timeAux.getText().toString().substring(3, 5));
        } else {
            hour = -1;
            min = -1;
        }
        TimePickerFragment newTimeFragment = TimePickerFragment.newInstance(
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String hourAux = "";
                        String minAux = "";
                        if (hourOfDay < 10) {
                            hourAux = hourAux + "0";
                        }
                        hourAux = hourAux + hourOfDay;
                        if (minute < 10) {
                            minAux = minAux + "0";
                        }
                        minAux = minAux + minute;
                        final String selectedTime = hourAux + ":" + minAux;
                        timeAux.setText(selectedTime);
                    }
                }, hour, min
        );


        newTimeFragment.show(getFragmentManager(), "timePicker");

    }


    public void guardarReserva(View view) {
        if (reservaCompleta()) {
            Reserva res = new Reserva();
            res.setNombre(nombreEt.getText().toString());
            res.setFijo(fijoCb.isChecked());
            res.setInicio(UtilCalendar.getCalendarFromString(fechaEt.getText().toString(), horaEt.getText().toString()));
            res.setFin(UtilCalendar.getCalendarFromString(fechaEt.getText().toString(), horaEtFin.getText().toString()));
            ODeAlquiler odaAux = (ODeAlquiler) psODeAlquiler.getSelectedItem();
            res.setOdalquilerID(odaAux.getId());


            if (res.getInicio().compareTo(Calendar.getInstance()) >= 0) {
                if (res.getFin().compareTo(res.getInicio()) < 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.validacion_horas_invertidas), Toast.LENGTH_LONG).show();
                } else {
                    if (!idTv.getText().toString().isEmpty()) {
                        res.setId(Long.parseLong(idTv.getText().toString()));
                    }
                    boolean aux = estaSuperpuesto(res);
                    if (aux) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.validacion_reservas_superpuestas), Toast.LENGTH_LONG).show();
                        mostrarReservas(res);
                    } else {
                        try {
                            if (RepoReserva.getInstance(getApplicationContext()).reservasContainID(res.getId())) {
                                RepoReserva.getInstance(getApplicationContext()).update(res);
                            } else {
                                RepoReserva.getInstance(getApplicationContext()).save(res);
                            }
                            onBackPressed();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_al_guardar_reserva), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            } else {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.validacion_fecha_pasada), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void mostrarReservas(Reserva res) {
        try {

            List<Reserva> quitados = new ArrayList<>();
            listRes = RepoReserva.getInstance(getApplicationContext())
                    .getAllReservasOrdenado();
            for (Reserva item : listRes) {
                if (item.compareDayTo(res) != 0) {
                    quitados.add(item);
                } else {
                    if (!item.isFijo() && res.isFijo()) {
                        if (UtilCalendar.compareDateTo(item.getInicio(), res.getInicio()) < 0) {
                            quitados.add(item);
                        }
                    }
                }
            }
            for (Reserva item : quitados) {
                listRes.remove(item);
            }
            resAdapter.setReservas(listRes);
            resAdapter.notifyDataSetChanged();

            llReservados.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_carga_vistas_reserva), Toast.LENGTH_SHORT);
        }
    }

    private boolean estaSuperpuesto(Reserva res) {
        //Recupero lista de reservas para controlar que no exista una en el mismo dia y periodo.
        boolean resultado = false;
        try {
            List<Reserva> listRes = RepoReserva.getInstance(getApplicationContext())
                    .getAllReservas();
            for (Reserva item : listRes) {
                if (res.compareTo(item) == 0) {
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
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Error_de_Carga_agenda), Toast.LENGTH_SHORT).show();
        }
        return resultado;

    }

    private boolean reservaCompleta() {
        boolean respuesta = true;
        ODeAlquiler oda = (ODeAlquiler) psODeAlquiler.getSelectedItem();
        if (nombreEt.getText().toString().isEmpty()
                || fechaEt.getText().toString().isEmpty()
                || horaEt.getText().toString().isEmpty()
                || horaEtFin.getText().toString().isEmpty()
                || oda.getId() == 0l
                ){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.validacion_campos_vacios), Toast.LENGTH_LONG).show();
            respuesta = false;
        }
        return respuesta;
    }


//    private void resumeAlquileres(Context con) throws Exception {
//        List<ODeAlquiler> lOdeAlquiler = null;
//        lOdeAlquiler = RepoODAlquiler.getInstance(con).getAllODeAlquilers();
//        lOdeAlquiler.add(new ODeAlquiler("Seleccionar Objeto de Alquiler"));
//        AlquilerAdapter adapter = new AlquilerAdapter(lOdeAlquiler);
//        // Apply the adapter to the spinner
//        psODeAlquiler.setAdapter(adapter);
//        psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
//    }

    private void resumeAlquileres(Context con) throws Exception {
        lOdeAlquiler = RepoODAlquiler.getInstance(con).getAllODeAlquilers();
        if (lOdeAlquiler.size()==0){
            callAgregarDialog(-1l);
        }
        if (lOdeAlquiler.size() != 1) {
            lOdeAlquiler.add(new ODeAlquiler(getApplicationContext().getResources().getString(R.string.seleccionar_objeto_de_alquiler)));
        }


        odaAdapter.setAlquileres(lOdeAlquiler);
        psODeAlquiler.setPopupBackgroundResource(R.color.fondo_aplicacion);
        // Apply the adapter to the spinner
        //psODeAlquiler.setAdapter(odaAdapter);
        odaAdapter.notifyDataSetChanged();
        psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
    }

    private void callAgregarDialog( long id){
        ODeAlquiler oda = null;
        try {
            oda = RepoODAlquiler.getInstance(getApplication()).getODeAlquilerById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnAceptar = (Button) dialogoAgregar.findViewById(R.id.btnAceptar);
        final EditText etOdAlquiler = (EditText) dialogoAgregar.findViewById(R.id.etAlquiler);
        if (oda != null){
            etOdAlquiler.setText(oda.getName());
        }else{
            etOdAlquiler.setText("");
        }
        final ODeAlquiler finalOda = oda;
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!etOdAlquiler.getText().toString().isEmpty()) {
                        if(finalOda !=null) {
                            finalOda.setName(etOdAlquiler.getText().toString());
                            RepoODAlquiler.getInstance(AgregarReserva.this).update(finalOda);

                        }else{
                            RepoODAlquiler.getInstance(AgregarReserva.this).save(new ODeAlquiler(etOdAlquiler.getText().toString()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    resumeAlquileres(AgregarReserva.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogoAgregar.dismiss();
            }
        });
        dialogoAgregar.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

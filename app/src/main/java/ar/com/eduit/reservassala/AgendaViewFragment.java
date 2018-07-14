package ar.com.eduit.reservassala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.AlquilerAdapter;
import ar.com.eduit.utils.ReservaAdapter;


public class AgendaViewFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    private TextView tvEmptyMesage;

    //Elementos de lista de reservas
    private ListView lwReservas;
    private ReservaAdapter resAdapter;
    private List<Reserva> listRes;
    //-----------------------------
    //Elementos de lista de Objetos de Alquiler
    private List<ODeAlquiler> lOdeAlquiler;
    private AlquilerAdapter odaAdapter;
    private Spinner psODeAlquiler;
    //-----------------------------

    private Dialog dialogoAgregar = null;
    private Dialog customDialog = null;
    
    // TODO: Rename and change types and number of parameters
    public static AgendaViewFragment newInstance() {
        AgendaViewFragment fragment = new AgendaViewFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lwReservas = (ListView) getView().findViewById(R.id.lvListaReservas);
        tvEmptyMesage = (TextView) getView().findViewById(R.id.tvEmptyMesage);
        psODeAlquiler = (Spinner) getView().findViewById(R.id.sp_odalquiler);

        psODeAlquiler.setOnItemSelectedListener(this);

        // crear dialogo para crear Objeto de alquiler
        dialogoAgregar = new Dialog(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        //deshabilitamos el título por defecto
        dialogoAgregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        //dialogoAgregar.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        dialogoAgregar.setContentView(R.layout.init_alquiler_dialog);

        //Inicializo la lista de Reservas
        listRes = new ArrayList<>();
        resAdapter = new ReservaAdapter(listRes, getContext());
        lwReservas.setAdapter(resAdapter);

        //Inicializo la lista de objetos de alquiler
        lOdeAlquiler = new ArrayList<>();
        odaAdapter = new AlquilerAdapter(lOdeAlquiler);
        psODeAlquiler.setAdapter(odaAdapter);


    }

    @Override
    public void onResume() {
        try {
            resumeList(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            lwReservas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final long itemID = lwReservas.getAdapter().getItemId(position);

                    // creo el dialog cargo el tema al dialog
                    customDialog = new Dialog(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    //deshabilitamos el título por defecto
                    customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //obligamos al usuario a pulsar los botones para cerrarlo
                    //customDialog.setCancelable(false);
                    //establecemos el contenido de nuestro dialog
                    customDialog.setContentView(R.layout.options_dialog);

                    Button eliminar = (Button) customDialog.findViewById(R.id.btnEliminar);
                    eliminar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        try {
                                                            RepoReserva.getInstance(getContext()).deleteReservaById(itemID);
                                                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.la_reserva_ha_sido_eliminada), Toast.LENGTH_SHORT);
                                                            customDialog.dismiss();
                                                            resumeList(getContext());


                                                        } catch (Exception e) {
                                                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_al_borrar_el_registro), Toast.LENGTH_LONG);
                                                        }
                                                    }
                                                }
                    );


                    Button editar = (Button) customDialog.findViewById(R.id.btnEditar);
                    editar.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(getContext(), AgregarReserva.class);
                                                      intent.putExtra("itemID" , itemID);
                                                      startActivity(intent);
                                                      customDialog.dismiss();
                                                      try {
                                                          resumeList(getContext());
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                              }
                    );
                    customDialog.show();
                }
            });

        } catch (Exception e) {

        }

        try {
            resumeAlquileres(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }


    private void resumeList(Context context) throws Exception {

        ODeAlquiler oda = (ODeAlquiler) psODeAlquiler.getSelectedItem();
        listRes=null;
        if (oda.getId()>0){
            listRes = RepoReserva.getInstance(context)
                    .getReservasOrdenadoPosterioresFiltrado(oda.getId());
        }
        else {
            listRes = RepoReserva.getInstance(context)
                    .getReservasOrdenadoPosteriores();
        }

        resAdapter.setReservas(listRes);
        resAdapter.notifyDataSetChanged();
        if (lwReservas.getAdapter().getCount() > 0) {
            tvEmptyMesage.setVisibility(View.GONE);
        }else {
            tvEmptyMesage.setVisibility(View.VISIBLE);
        }
    }

    private void resumeAlquileres(Context con) throws Exception {
        lOdeAlquiler = RepoODAlquiler.getInstance(con).getAllODeAlquilers();
        if (lOdeAlquiler.size() !=1) {
            lOdeAlquiler.add(new ODeAlquiler(getContext().getResources().getString(R.string.todos)));
        }

        odaAdapter.setAlquileres(lOdeAlquiler);
        psODeAlquiler.setPopupBackgroundResource(R.color.fondo_aplicacion);
        // Apply the adapter to the spinner
        //psODeAlquiler.setAdapter(odaAdapter);
        odaAdapter.notifyDataSetChanged();
        psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            resumeList(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

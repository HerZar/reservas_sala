package ar.com.eduit.reservassala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.AlquilerAdapter;
import ar.com.eduit.utils.ReservaAdapter;

public class AgendaResHist extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ListView lwReservas;
    private Spinner psODeAlquiler;

    private Dialog customDialog = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_agenda_historica);
        lwReservas = (ListView) findViewById(R.id.lvListaReservas);
        psODeAlquiler = (Spinner) findViewById(R.id.sp_odalquiler);
        psODeAlquiler.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        try {
            resumeList(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lwReservas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final long itemID = lwReservas.getAdapter().getItemId(position);
                    // con este tema personalizado evitamos los bordes por defecto
                    customDialog = new Dialog(AgendaResHist.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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
                                                            RepoReserva.getInstance(AgendaResHist.this).deleteReservaById(itemID);
                                                            Toast.makeText(AgendaResHist.this, AgendaResHist.this.getResources().getString(R.string.la_reserva_ha_sido_eliminada), Toast.LENGTH_SHORT);
                                                            customDialog.dismiss();
                                                            resumeList(AgendaResHist.this);


                                                        } catch (Exception e) {
                                                            Toast.makeText(AgendaResHist.this, AgendaResHist.this.getResources().getString(R.string.error_al_borrar_el_registro), Toast.LENGTH_LONG);
                                                        }
                                                    }
                                                }
                    );
                    Button editar = (Button) customDialog.findViewById(R.id.btnEditar);
                    editar.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(AgendaResHist.this, AgregarReserva.class);
                                                      intent.putExtra("itemID" , itemID);
                                                      startActivity(intent);
                                                      customDialog.dismiss();
                                                      try {
                                                          resumeList(AgendaResHist.this);
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                              }
                    );
                    customDialog.show();
                    return false;
                }
            });
            // cargo el spinner con la lista de objectos a alquilar.
            // Create an ArrayAdapter using the string array and a default spinner layout
            List<ODeAlquiler> lOdeAlquiler = RepoODAlquiler.getInstance(getApplicationContext()).getAllODeAlquilers();
            lOdeAlquiler.add(new ODeAlquiler(getApplicationContext().getResources().getString(R.string.todos)));
            AlquilerAdapter adapter = new AlquilerAdapter(lOdeAlquiler);
            // Apply the adapter to the spinner
            psODeAlquiler.setAdapter(adapter);
            psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Error_de_Carga_agenda), Toast.LENGTH_SHORT).show();
            List<ODeAlquiler> lOdeAlquiler = new ArrayList<>();
            lOdeAlquiler.add(new ODeAlquiler(getApplicationContext().getResources().getString(R.string.todos)));
            AlquilerAdapter adapter = new AlquilerAdapter(lOdeAlquiler);
            // Apply the adapter to the spinner
            psODeAlquiler.setAdapter(adapter);
            psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
        }

        super.onResume();
    }

    public void borrarTodos(View view) {
        try {
            RepoReserva.getInstance(getApplicationContext()).deleteReservasViejas();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Historicos_borrados), Toast.LENGTH_LONG);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.error_borrar_historicos), Toast.LENGTH_LONG);
        }
        try {
            List<Reserva> listRes = RepoReserva.getInstance(getApplicationContext())
                    .getAllReservasOrdenado();
            final ReservaAdapter resAdapter = new ReservaAdapter(listRes, getApplicationContext());
            lwReservas.setAdapter(resAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.Error_de_Carga_agenda), Toast.LENGTH_SHORT).show();
        }
    }

    private void resumeList(Context context) throws Exception {

        ODeAlquiler oda = (ODeAlquiler) psODeAlquiler.getSelectedItem();
        List<Reserva> listRes=null;
        if (oda.getId()>0){
            listRes = RepoReserva.getInstance(context)
                    .getAllReservasOrdenadoFiltrado(oda.getId());
        }
        else {
            listRes = RepoReserva.getInstance(context)
                    .getAllReservasOrdenado();
        }
        //     listRes = getPosteriores(listRes);
        final ReservaAdapter resAdapter = new ReservaAdapter(listRes, getApplicationContext());
        lwReservas.setAdapter(resAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            resumeList(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


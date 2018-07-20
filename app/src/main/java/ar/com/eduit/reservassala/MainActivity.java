package ar.com.eduit.reservassala;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.utils.AlquilerAdapter;
import ar.com.eduit.utils.ReservaAdapter;

public class MainActivity extends AppCompatActivity{
        //implements AdapterView.OnItemSelectedListener{

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

    private  AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarReserva.class);
                startActivity(intent);
            }
        });

        //initialization on adMob
        MobileAds.initialize(this, "ca-app-pub-2504725253688326~4113229007");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //-----------------------------------------------

//        lwReservas = (ListView) findViewById(R.id.lvListaReservas);
//        tvEmptyMesage = (TextView) findViewById(R.id.tvEmptyMesage);
//        psODeAlquiler = (Spinner) findViewById(R.id.sp_odalquiler);
//        psODeAlquiler.setOnItemSelectedListener(this);
//
//        // crear dialogo para crear Objeto de alquiler
//        dialogoAgregar = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
//        //deshabilitamos el título por defecto
//        dialogoAgregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //obligamos al usuario a pulsar los botones para cerrarlo
//        //dialogoAgregar.setCancelable(false);
//        //establecemos el contenido de nuestro dialog
//        dialogoAgregar.setContentView(R.layout.init_alquiler_dialog);
//
//        //Inicializo la lista de Reservas
//        listRes = new ArrayList<>();
//        resAdapter = new ReservaAdapter(listRes, getApplicationContext());
//        lwReservas.setAdapter(resAdapter);
//
//        //Inicializo la lista de objetos de alquiler
//        lOdeAlquiler = new ArrayList<>();
//        odaAdapter = new AlquilerAdapter(lOdeAlquiler);
//        psODeAlquiler.setAdapter(odaAdapter);
    }

    @Override
    protected void onResume() {
//        try {
//            resumeList(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//
//            lwReservas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    final long itemID = lwReservas.getAdapter().getItemId(position);
//                    /*
//                    Intent intent = new Intent(getApplicationContext(), options.class);
//                    intent.putExtra("itemID" , itemID);
//                    startActivity(intent);
//                    */
//                    // creo el dialog cargo el tema al dialog
//                    customDialog = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
//                    //deshabilitamos el título por defecto
//                    customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    //obligamos al usuario a pulsar los botones para cerrarlo
//                    //customDialog.setCancelable(false);
//                    //establecemos el contenido de nuestro dialog
//                    customDialog.setContentView(R.layout.options_dialog);
//
//                    Button eliminar = (Button) customDialog.findViewById(R.id.btnEliminar);
//                    eliminar.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    RepoReserva.getInstance(MainActivity.this).deleteReservaById(itemID);
//                                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.la_reserva_ha_sido_eliminada), Toast.LENGTH_SHORT);
//                                    customDialog.dismiss();
//                                    resumeList(MainActivity.this);
//
//
//                                } catch (Exception e) {
//                                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.error_al_borrar_el_registro), Toast.LENGTH_LONG);
//                                }
//                            }
//                        }
//                    );
//
//
//                    Button editar = (Button) customDialog.findViewById(R.id.btnEditar);
//                    editar.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(MainActivity.this, AgregarReserva.class);
//                                intent.putExtra("itemID" , itemID);
//                                startActivity(intent);
//                                customDialog.dismiss();
//                                try {
//                                    resumeList(MainActivity.this);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    );
//                    customDialog.show();
//                }
//            });
//
//        } catch (Exception e) {
//
//        }
//
//        try {
//            resumeAlquileres(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /*try{
            // cargo el spinner con la lista de objectos a alquilar.
            // Create an ArrayAdapter using the string array and a default spinner layout
            lOdeAlquiler = RepoODAlquiler.getInstance(getApplicationContext()).getAllODeAlquilers();
            if (lOdeAlquiler.size()==0){
                callAgregarDialog(-1l);

            }
            if (lOdeAlquiler.size()!=1) {
                lOdeAlquiler.add(new ODeAlquiler(getApplicationContext().getResources().getString(R.string.todos)));
            }
            AlquilerAdapter adapter = new AlquilerAdapter(lOdeAlquiler);
            // Apply the adapter to the spinner
            psODeAlquiler.setAdapter(adapter);
            psODeAlquiler.setSelection(lOdeAlquiler.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        getSupportFragmentManager().beginTransaction().replace(R.id.flshowreservas,AgendaViewFragment.newInstance()).commit();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_historico) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void irHistorico(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), AgendaResHist.class);
        startActivity(intent);

    }

    public void irSetings(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), SetingsActivity.class);
        startActivity(intent);
    }

    public void showSemana(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flshowreservas,WeekAgendaViewFragment.newInstance()).commit();
    }

    public void showDia(View view) {

        getSupportFragmentManager().beginTransaction().replace(R.id.flshowreservas,DayAgendaViewFragment.newInstance()).commit();

    }

    public void showAgenda(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flshowreservas,
               AgendaViewFragment.newInstance() ).commit();
    }

}

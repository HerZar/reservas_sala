package ar.com.eduit.reservassala;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import ar.com.eduit.Exceptions.ContainReservasException;
import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.repository.RepoODAlquiler;
import ar.com.eduit.utils.AlquilerAdapter;

public class SetingsActivity extends AppCompatActivity {

    private ListView lvAlquileres;
    private Dialog dialogoAgregar = null;
    private Dialog dialogoOptions = null;
    private TextView tvEmptyMesage;

    private List<ODeAlquiler> lOdeAlquiler;
    private AlquilerAdapter odaAdapter;

    private  AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lvAlquileres = (ListView) findViewById(R.id.lvListaAlquileres);
        tvEmptyMesage = (TextView) findViewById(R.id.tvEmptyMesage);
        try {
            resumeList(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        /** creo el dialog cargo el tema al dialog para crear y actualizar Alquileres*/
        dialogoAgregar = new Dialog(SetingsActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        //deshabilitamos el título por defecto
        dialogoAgregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        //dialogoAgregar.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        dialogoAgregar.setContentView(R.layout.alquiler_dialog);

        /**creo el dialogo para editar o eliminar alquileres*/
        dialogoOptions = new Dialog(SetingsActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        //deshabilitamos el título por defecto
        dialogoOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        //dialogoAgregar.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        dialogoOptions.setContentView(R.layout.options_dialog);

        // creo las funciones del dialogo options
        lvAlquileres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final long itemID = lvAlquileres.getAdapter().getItemId(position);
                    /*
                    Intent intent = new Intent(getApplicationContext(), options.class);
                    intent.putExtra("itemID" , itemID);
                    startActivity(intent);
                    */
                Button eliminar = (Button) dialogoOptions.findViewById(R.id.btnEliminar);
                eliminar.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        RepoODAlquiler.getInstance(SetingsActivity.this).deleteODeAlquilerById(itemID);
                                                        Toast.makeText(SetingsActivity.this, SetingsActivity.this.getResources().getString(R.string.objeto_de_alquiler_borrado), Toast.LENGTH_SHORT).show();
                                                        dialogoOptions.dismiss();
                                                        resumeList(SetingsActivity.this);


                                                    } catch(ContainReservasException cre){
                                                        Toast.makeText(SetingsActivity.this, SetingsActivity.this.getResources().getString(R.string.objeto_de_alquiler_con_reservas), Toast.LENGTH_LONG).show();
                                                    }
                                                    catch (Exception e) {
                                                        Toast.makeText(SetingsActivity.this, SetingsActivity.this.getResources().getString(R.string.error_al_borrar_el_registro), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                );


                Button editar = (Button) dialogoOptions.findViewById(R.id.btnEditar);
                editar.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  callAgregarDialog(itemID);
                                                  dialogoOptions.dismiss();
                                                  try {
                                                      resumeList(SetingsActivity.this);
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                          }
                );
                dialogoOptions.show();
            }
        });
        lvAlquileres.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });


        //Inicializar lista de alquileres
        lOdeAlquiler = new ArrayList<>();
        odaAdapter = new AlquilerAdapter(lOdeAlquiler);
        lvAlquileres.setAdapter(odaAdapter);
        try {
            resumeList(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarAlquiler(View view) {
          callAgregarDialog(-1l);
    }

    private void resumeList(Context context) throws Exception {

        lOdeAlquiler = RepoODAlquiler.getInstance(context).getAllODeAlquilers();
        odaAdapter.setAlquileres(lOdeAlquiler);
        odaAdapter.notifyDataSetChanged();
        if (lvAlquileres.getAdapter().getCount() > 0) {
            tvEmptyMesage.setVisibility(View.GONE);
        }else {
            tvEmptyMesage.setVisibility(View.VISIBLE);
        }
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
                            RepoODAlquiler.getInstance(SetingsActivity.this).update(finalOda);

                        }else{
                            RepoODAlquiler.getInstance(SetingsActivity.this).save(new ODeAlquiler(etOdAlquiler.getText().toString()));
                        }
                        resumeList(SetingsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogoAgregar.dismiss();
            }
        });
        dialogoAgregar.show();

    }
}

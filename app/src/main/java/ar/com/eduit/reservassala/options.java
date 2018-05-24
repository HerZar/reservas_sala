package ar.com.eduit.reservassala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ar.com.eduit.repository.RepoReserva;

public class options extends AppCompatActivity {

    private long itemID;
    private Button btnEliminar;
    private Button btnEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().hide();
        itemID = getIntent().getExtras().getLong("itemID");
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEditar = (Button) findViewById(R.id.btnEditar);
      //  btnEditar.setVisibility(View.VISIBLE);
    }

    public void eliminarReserva(View view) {
        try{
            RepoReserva.getInstance(getApplicationContext()).deleteReservaById(itemID);
            Toast.makeText(this.getApplicationContext(),this.getApplicationContext().getResources().getString(R.string.la_reserva_ha_sido_eliminada),Toast.LENGTH_SHORT);
        }catch (Exception e){
            Toast.makeText(this.getApplicationContext(),this.getApplicationContext().getResources().getString(R.string.error_al_borrar_el_registro),Toast.LENGTH_LONG);
        }

        onBackPressed();
    }

    public void editarReserva(View view) {

        Intent intent = new Intent(getApplicationContext(), AgregarReserva.class);
        intent.putExtra("itemID" , itemID);
        startActivity(intent);
        finish();

    }
}

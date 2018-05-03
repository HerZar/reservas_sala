package ar.com.eduit.reservassala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.ReservaAdapter;

public class AgendaResHist extends AppCompatActivity {
    private ListView lwReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_agenda_historica);
        lwReservas = (ListView) findViewById(R.id.lvListaReservas);

    }

    @Override
    protected void onResume() {
        try {
            List<Reserva> listRes = RepoReserva.getInstance(getApplicationContext())
                    .getAllReservasOrdenado();
            final ReservaAdapter resAdapter = new ReservaAdapter(listRes);
            lwReservas.setAdapter(resAdapter);
            lwReservas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    long itemID = resAdapter.getItemId(position);
                    Intent intent = new Intent(getApplicationContext(), options.class);
                    intent.putExtra("itemID", itemID);
                    startActivity(intent);
                    return false;
                }
            });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Al cargar Agenda.", Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }

    public void borrarTodos(View view) {
        try {
            RepoReserva.getInstance(getApplicationContext()).deleteReservasViejas();
            Toast.makeText(getApplicationContext(), "Registros historicos borrados.", Toast.LENGTH_LONG);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al borrar los registros historicos.", Toast.LENGTH_LONG);
        }
        try {
            List<Reserva> listRes = RepoReserva.getInstance(getApplicationContext())
                    .getAllReservasOrdenado();
            final ReservaAdapter resAdapter = new ReservaAdapter(listRes);
            lwReservas.setAdapter(resAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Al cargar Agenda.", Toast.LENGTH_SHORT).show();
        }
    }
}


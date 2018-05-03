package ar.com.eduit.reservassala;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.com.eduit.entities.Reserva;
import ar.com.eduit.repository.RepoReserva;
import ar.com.eduit.utils.ReservaAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmptyMesage;
    private ListView lwReservas;
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

        lwReservas = (ListView) findViewById(R.id.lvListaReservas);
        tvEmptyMesage = (TextView) findViewById(R.id.tvEmptyMesage);
    }

    @Override
    protected void onResume() {
        try {
            List<Reserva> listRes = RepoReserva.getInstance(getApplicationContext())
                    .getReservasOrdenadoPosteriores();
       //     listRes = getPosteriores(listRes);
            final ReservaAdapter resAdapter = new ReservaAdapter(listRes);
            lwReservas.setAdapter(resAdapter);
            lwReservas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    long itemID = resAdapter.getItemId(position);
                    Intent intent = new Intent(getApplicationContext(), options.class);
                    intent.putExtra("itemID" , itemID);
                    startActivity(intent);
                    return false;
                }
            });
            if (resAdapter.getCount()>0){
                tvEmptyMesage.setVisibility(View.GONE);
            }




        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error Al cargar Agenda.", Toast.LENGTH_SHORT).show();
        }

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


}

package ar.com.eduit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import ar.com.eduit.entities.ODeAlquiler;
import ar.com.eduit.entities.Reserva;
import ar.com.eduit.entities.Seting;

/**
 * Created by a211589 on 14/07/2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String NOMBRE_DB= "ReservasDB";
    private static final int VERSION = 2;

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_DB,null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Seting.class);
            TableUtils.createTable(connectionSource, ODeAlquiler.class);
            TableUtils.createTable(connectionSource, Reserva.class);

            //inicializacion
            Dao<Seting, Long> dao =  getDao(Seting.class);
            Seting set = new Seting("first", "0");
            dao.create(set);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}

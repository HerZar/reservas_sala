package ar.com.eduit.repository;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.com.eduit.entities.Reserva;

public class RepoReserva {

    private static  RepoReserva instance;

    private Dao<Reserva,Long> dao;

    public RepoReserva(Context contexto) {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(contexto, DataBaseHelper.class);
        try{
            dao = helper.getDao(Reserva.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RepoReserva getInstance(Context contexto){
        if(instance ==null){
            instance = new RepoReserva(contexto);
        }
        return instance;
    }

    public void save(Reserva c) throws Exception{
        dao.create(c);
    }
    public void update (Reserva c) throws Exception{
        dao.update(c);
    }

    public Reserva getReservaById(long id) throws Exception{
        return dao.queryForId(id);
    }

    public List<Reserva> getAllReservas()throws Exception{
        return dao.queryForAll();

        //return null;
    }

    public void deleteReservaById(long id) throws Exception{
        dao.deleteById(id);
    }

    public List<Reserva> getAllReservasOrdenadoFiltrado(long oDaId) throws Exception {
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        qb.where().eq("odalquilerID", oDaId);
        qb.orderBy("inicio",true);
        qb.orderBy("fijo", false);
        return qb.query();

    }
    public List<Reserva> getAllReservasOrdenado() throws Exception {
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        qb.orderBy("inicio",true);
        qb.orderBy("fijo", false);
        return qb.query();

    }

    public List<Reserva> getReservasOrdenadoPosteriores() throws Exception {
        Calendar aux = Calendar.getInstance();
        aux.set(Calendar.HOUR_OF_DAY, 0);
        aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.SECOND, 0);
        aux.set(Calendar.MILLISECOND, 0);
        Date inicioAux = aux.getTime();
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        qb.orderBy("inicio",true);
        qb.orderBy("fijo", false);
        qb.where().ge("inicio",inicioAux).
                   or().eq("fijo",true);

        return qb.query();

    }

    public List<Reserva> getReservasOrdenadoPosterioresFiltrado(long oDaId) throws Exception {
        Calendar aux = Calendar.getInstance();
        aux.set(Calendar.HOUR_OF_DAY, 0);
        aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.SECOND, 0);
        aux.set(Calendar.MILLISECOND, 0);
        Date inicioAux = aux.getTime();
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        qb.orderBy("inicio",true);
        qb.orderBy("fijo", false);
        Where<Reserva, Long> w = qb.where();
        w.and(w.eq("odalquilerID", oDaId),
                w.or(w.gt("inicio",inicioAux),w.eq("fijo",true))
        );



        return qb.query();

    }

    public List<Reserva> getReservasOrdenadoAnteriores() throws Exception {
        Calendar aux = Calendar.getInstance();
        aux.set(Calendar.HOUR_OF_DAY, 0);
        aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.SECOND, 0);
        aux.set(Calendar.MILLISECOND, 0);
        Date inicioAux = aux.getTime();
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        qb.orderBy("inicio",true);
        qb.orderBy("fijo", false);
        qb.where().lt("inicio",inicioAux).
                   and().
                   eq("fijo",false);

        return qb.query();

    }

    public void deleteReservasViejas() throws Exception{
        Calendar aux = Calendar.getInstance();
        aux.set(Calendar.HOUR_OF_DAY, 0);
        aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.SECOND, 0);
        aux.set(Calendar.MILLISECOND, 0);
        Date inicioAux = aux.getTime();
        DeleteBuilder<Reserva,Long> db= dao.deleteBuilder();
        db.where().lt("inicio",inicioAux).
                and().
                eq("fijo",false);
        db.delete();

    }

    public boolean reservasContainID(long id) throws SQLException {
        return dao.idExists(id);
    }

    public boolean reservasContainIDOdAlquiler(long id) throws SQLException {
        QueryBuilder<Reserva,Long> qb= dao.queryBuilder();
        List<Reserva> aux = qb.where().
                eq("odalquilerID",id).query();

        boolean respuesta = false;
        if (aux.size()>0){respuesta=true;}

        return respuesta;
    }
}

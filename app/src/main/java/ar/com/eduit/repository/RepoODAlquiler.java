package ar.com.eduit.repository;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import ar.com.eduit.Exceptions.ContainReservasException;
import ar.com.eduit.entities.ODeAlquiler;

public class RepoODAlquiler {

    private static  RepoODAlquiler instance;
    private Context context;

    private Dao<ODeAlquiler,Long> dao;

    public RepoODAlquiler(Context contexto) {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(contexto, DataBaseHelper.class);
        context = contexto;
        try{
            dao = helper.getDao(ODeAlquiler.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RepoODAlquiler getInstance(Context contexto){
        if(instance ==null){
            instance = new RepoODAlquiler(contexto);
        }
        return instance;
    }

    public void save(ODeAlquiler c) throws Exception{
        dao.create(c);
    }
    public void update (ODeAlquiler c) throws Exception{
        dao.update(c);
    }

    public ODeAlquiler getODeAlquilerById(long id) throws Exception{
        return dao.queryForId(id);
    }

    public List<ODeAlquiler> getAllODeAlquilers()throws Exception{
        return dao.queryForAll();

        //return null;
    }

    public List<String> getListODeAlquilers()throws Exception{
        List<ODeAlquiler> lista = dao.queryForAll();
        List<String> listaString = new ArrayList<>();
        for (ODeAlquiler a : lista) {
            listaString.add(a.getName());
        }
        return listaString;

    }

    public void deleteODeAlquilerById(long id) throws Exception{
        if (RepoReserva.getInstance(context).reservasContainIDOdAlquiler(id)){
                throw new ContainReservasException();
        }
        dao.deleteById(id);
    }

}

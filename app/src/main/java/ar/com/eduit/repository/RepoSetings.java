package ar.com.eduit.repository;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import ar.com.eduit.entities.Seting;

public class RepoSetings {


    private static  RepoSetings instance;

    private Dao<Seting,Long> dao;

    public RepoSetings(Context contexto) {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(contexto, DataBaseHelper.class);
        try{
            dao = helper.getDao(Seting.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RepoSetings getInstance(Context contexto){
        if(instance ==null){
            instance = new RepoSetings(contexto);
        }
        return instance;
    }

    public void save(Seting c) throws Exception{
        dao.create(c);
    }
    public void update (Seting c) throws Exception{
        dao.update(c);
    }

    public Seting getSetingsById(long id) throws Exception{
        return dao.queryForId(id);
    }

    public Seting getSetingsByKey(String key) throws SQLException {
        //return dao.queryForEq("key", key);
        QueryBuilder<Seting,Long> qb = dao.queryBuilder();
        qb.where().eq("key", key);

        return qb.queryForFirst();

    }

    public List<Seting> getAllSetingss()throws Exception{
        return dao.queryForAll();

        //return null;
    }

    public void deleteSetingsById(long id) throws Exception{
        dao.deleteById(id);
    }


}

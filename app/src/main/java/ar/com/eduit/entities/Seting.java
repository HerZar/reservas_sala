package ar.com.eduit.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Seting")
public class Seting {

    /**
        Esta tabla guarda configuraciones del sistema.
        Documentar en este cuadro de comentario cada configuracion ingresada

        Setings (Configuraciones)
        key = first ; valor de perimera ejecucion, si esta en 0 la aplicacion es usada por primera vez.
    */
    @DatabaseField(generatedId = true)    // For Autoincrement)
    private long id;
    @DatabaseField
    private String key;
    @DatabaseField
    private String seting;

    public Seting() {
    }

    public Seting(String key, String seting) {
        this.key = key;
        this.seting = seting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSeting() {
        return seting;
    }

    public void setSeting(String seting) {
        this.seting = seting;
    }
}

package ar.com.eduit.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Alquileres")
public class ODeAlquiler  {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String name;

    public ODeAlquiler() {
    }

    public ODeAlquiler(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

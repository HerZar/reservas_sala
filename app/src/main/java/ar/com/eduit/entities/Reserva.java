package ar.com.eduit.entities;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

import ar.com.eduit.utils.UtilCalendar;

@DatabaseTable(tableName = "reservas")
public class Reserva implements Comparable<Reserva> {

    @DatabaseField(generatedId = true)    // For Autoincrement)
    private long id;
    @DatabaseField
    private String Nombre;
    @DatabaseField
    private Date inicio;
    @DatabaseField
    private Date fin;
    @DatabaseField
    private boolean fijo = false;

    public Reserva() {

    }

    public Reserva(String nombre, Calendar inicio, Calendar fin, boolean fijo) {
        Nombre = nombre;
        this.inicio = inicio.getTime();
        this.fin = fin.getTime();
        this.fijo = fijo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Calendar getInicio() {
        Calendar inicioAux = Calendar.getInstance();
        inicioAux.setTime(this.inicio);
        return inicioAux;
    }

    public void setInicio(Calendar inicio) {
        this.inicio = inicio.getTime();
    }

    public Calendar getFin() {
        Calendar finAux = Calendar.getInstance();
        finAux.setTime(this.fin);
        return finAux;
    }

    public void setFin(Calendar fin) {
        this.fin = fin.getTime();
    }

    public boolean isFijo() {
        return fijo;
    }

    public void setFijo(boolean fijo) {
        this.fijo = fijo;
    }

    /**
     *  este metodo devuelve -1 si es menor, 0 si es superpuesto, 1 si es mayor, y 2 si es el mismo objeto.
     * */
    @Override
    public int compareTo(@NonNull Reserva o) {
        int retorno = 0;
        if (this.getId() == o.getId()){
            retorno = 2;
        }
        else {
            if (!this.isFijo() && !o.isFijo()) {
                if (this.inicio.compareTo(o.fin) >= 0) {

                    retorno = 1;
                } else {
                    if (this.fin.compareTo(o.inicio) <= 0) {
                        retorno = -1;
                    } else {
                        retorno = 0;
                    }
                }
            } else {
                //Compruebo dia
                if (this.getInicio().get(Calendar.DAY_OF_WEEK) == o.getInicio().get(Calendar.DAY_OF_WEEK)) {
                    //is el dia es el mismo comparo horas.
                    if (UtilCalendar.compareHourTo(this.getInicio(), o.getFin()) >= 0) {
                        retorno = 1;
                    } else {
                        //if (this.fin.compareTo(o.inicio) <= 0) {
                        if (UtilCalendar.compareHourTo(this.getFin(), o.getInicio()) <= 0) {
                            retorno = -1;
                        } else {
                            retorno = 0;
                        }
                    }
                } else {
                    if (this.getInicio().get(Calendar.DAY_OF_WEEK) < o.getInicio().get(Calendar.DAY_OF_WEEK)) {
                        retorno = -1;
                    } else {
                        retorno = 1;
                    }
                }
            }
        }
        return retorno;
    }

    /**
     *  este metodo devuelve -1 si es menor, 0 si es superpuesto, 1 si es mayor, y 2 si es el mismo objeto.
     * */
    @SuppressLint("WrongConstant")
    public int compareDayTo(Reserva o) {
        int respuesta = 0;
        if (this.getId() == o.getId()){
            respuesta = 2;
        }
        else {
    /*  CODIGO REMOVIDO Y PUESTO EN UTILCALENDAR
        Calendar esteDia = Calendar.getInstance();
        Calendar elOtro = Calendar.getInstance();
        esteDia.set(this.getInicio().get(Calendar.YEAR),
                this.getInicio().get(Calendar.MONTH),
                this.getInicio().get(Calendar.DAY_OF_MONTH),
                this.getInicio().get(0),
                this.getInicio().get(0));
        esteDia.set(Calendar.SECOND,0);
        esteDia.set(Calendar.MILLISECOND,0);

        elOtro.set(o.getInicio().get(Calendar.YEAR),
                o.getInicio().get(Calendar.MONTH),
                o.getInicio().get(Calendar.DAY_OF_MONTH),
                o.getInicio().get(0),
                o.getInicio().get(0));
        elOtro.set(Calendar.SECOND,0);
        elOtro.set(Calendar.MILLISECOND,0);*/

            if (!this.isFijo() && !o.isFijo()) {
                respuesta = UtilCalendar.compareDateTo(this.getInicio(), o.getInicio());
            } else {
                //Compruebo dia
                if (this.getInicio().get(Calendar.DAY_OF_WEEK) == o.getInicio().get(Calendar.DAY_OF_WEEK)) {
                    //is el dia es el mismo comparo horas.
                    respuesta = 0;
                } else {
                    if (this.getInicio().get(Calendar.DAY_OF_WEEK) < o.getInicio().get(Calendar.DAY_OF_WEEK)) {
                        respuesta = -1;
                    } else {
                        respuesta = 1;
                    }
                }
            }
        }

        return respuesta;
    }


    public boolean isForToday(){

        boolean isToday = false;
        Calendar today = Calendar.getInstance();

        if (this.isFijo()){
            if ((this.getInicio().get(Calendar.DAY_OF_WEEK) ==
                    today.get(Calendar.DAY_OF_WEEK)) &&
                    (UtilCalendar.compareDateTo(this.getInicio(),today) <= 0)){
                isToday=true;
            }
        }else{
            if (UtilCalendar.compareDateTo(this.getInicio(),today) == 0){
                isToday = true;
            }
        }

        return isToday;
    }

}

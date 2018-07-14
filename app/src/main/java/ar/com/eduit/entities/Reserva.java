package ar.com.eduit.entities;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    //Forein key ODeAlquiler
    @DatabaseField
    private long odalquilerID;

    public Reserva() {

    }

    public Reserva(String nombre, Date inicio, Date fin, boolean fijo, long odalquilerID) {
        Nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.fijo = fijo;
        this.odalquilerID = odalquilerID;
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

    public long getOdalquilerID() {
        return odalquilerID;
    }

    public void setOdalquilerID(long odalquilerID) {
        this.odalquilerID = odalquilerID;
    }

    /**
     * este metodo devuelve -1 si es menor, 0 si es superpuesto, 1 si es mayor,
     * 2 si es el mismo objeto, 3 si no pertenese al mismo objeto de alquiler.
     */
    @Override
    public int compareTo(@NonNull Reserva o) {
        int retorno = 0;

        // compruebo si pertenece al mismo objeto de alquiler.
        if (this.getOdalquilerID() != o.getOdalquilerID()) {
            retorno = 3;
        } else {
            //Compruebo si el objeto es si mismo.
            if (this.getId() == o.getId()) {
                retorno = 2;
            } else {
                //Compruebo si ninguna de las reservas es fija coparo unicamente las fechas
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
                    //si alguna de las dos respervas es fija, comparo dia de la semana y hora.
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
        }
        return retorno;
    }

    /**
     * este metodo devuelve -1 si es menor, 0 si es superpuesto, 1 si es mayor,
     * 2 si es el mismo objeto, 3 si no pertenese al mismo objeto de alquiler.
     */

    public int compareToIgnoringODalquiler(@NonNull Reserva o) {
        int retorno = 0;

        //Compruebo si el objeto es si mismo.
        if (this.getId() == o.getId()) {
            retorno = 2;
        } else {
            //Compruebo si ninguna de las reservas es fija coparo unicamente las fechas
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
                //si alguna de las dos respervas es fija, comparo dia de la semana y hora.
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
     * este metodo devuelve -1 si es menor, 0 si es superpuesto, 1 si es mayor,
     * 2 si es el mismo objeto, 3 si no pertenece al mismo objeto de alquiler.
     */
    @SuppressLint("WrongConstant")
    public int compareDayTo(Reserva o) {
        int respuesta = 0;
        // compruebo si pertenece al mismo objeto de alquiler.
        if (this.getOdalquilerID() != o.getOdalquilerID()) {
            respuesta = 3;
        } else {
            //Compruebo si el objeto es si mismo.
            if (this.getId() == o.getId()) {
                respuesta = 2;
            } else {
                //Compruebo si ninguna de las reservas es fija coparo unicamente las fechas
                if (!this.isFijo() && !o.isFijo()) {
                    respuesta = UtilCalendar.compareDateTo(this.getInicio(), o.getInicio());
                } else {
                    //si alguna de las dos respervas es fija, comparo dia de la semana y hora.
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
        }

        return respuesta;
    }


    public boolean isForToday() {

        boolean isToday = false;
        Calendar today = Calendar.getInstance();

        if (this.isFijo()) {
            if ((this.getInicio().get(Calendar.DAY_OF_WEEK) ==
                    today.get(Calendar.DAY_OF_WEEK)) &&
                    (UtilCalendar.compareDateTo(this.getInicio(), today) <= 0)) {
                isToday = true;
            }
        } else {
            if (UtilCalendar.compareDateTo(this.getInicio(), today) == 0) {
                isToday = true;
            }
        }

        return isToday;
    }


    public boolean estaSuperpuesta(List<Reserva> listRes) {
        for (Reserva item : listRes) {
            //si es si mismo no esta superpuesto
            if (this.getId() == item.getId()) {
                return false;
            } else {
                if (this.getInicio().compareTo(item.getFin()) >= 0) {
                    return false;
                } else {
                    if (this.getFin().compareTo(item.getInicio()) <= 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

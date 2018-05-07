package ar.com.eduit.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.com.eduit.reservassala.R;

public class UtilCalendar {
/*
    private static final String[] strDays = new String[]{
            "Domingo",
            "Lunes",
            "Martes",
            "Miercoles",
            "Jueves",
            "Viernes",
            "Sabado"};
*/
    /**
     *  Este metodo recibe un int de 0 a 6 y devuelve el dia de la semana para ese int
     *  0 = Domingo en adelante.
     * */
    public static String getDiaSemana (Context context , int dia){

        List<String> strDays = new ArrayList<>();
        strDays.add(context.getString(R.string.Domingo));
        strDays.add(context.getString(R.string.Lunes));
        strDays.add(context.getString(R.string.Martes));
        strDays.add(context.getString(R.string.Miercoles));
        strDays.add(context.getString(R.string.Jueves));
        strDays.add(context.getString(R.string.Viernes));
        strDays.add(context.getString(R.string.Sabado));


        return strDays.get(dia);
    }

    /**
     *  Este metodo recibe una fecha y una hora en string y
     *  devuelve un calendar cargado
     *  @fecha debe recibirse en formato DD/MM/YYYY
     *  @hora debe recibirse en formato hh:mi
     */
    public static Calendar getCalendarFromString(String fecha, String hora){

        int day = Integer.parseInt(fecha.substring(0,2));
        int month = Integer.parseInt(fecha.substring(3,5));
        int year = Integer.parseInt(fecha.substring(6,10));
        int hour = Integer.parseInt(hora.substring(0,2));
        int min = Integer.parseInt(hora.substring(3,5));
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, year);
        calendario.set(Calendar.MONTH, month-1);
        calendario.set(Calendar.DAY_OF_MONTH, day);
        calendario.set(Calendar.HOUR_OF_DAY, hour);
        calendario.set(Calendar.MINUTE, min);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND,0);
        return calendario;
    }

    /**
     * Este metodo compara dos calendar unicamente por la fecha sin importar
     * la hora que tengan cargada.
     **/
    @SuppressLint("WrongConstant")
    public static int compareDateTo (Calendar a, Calendar b){
        Calendar esteDia = Calendar.getInstance();
        Calendar elOtro = Calendar.getInstance();
        esteDia.set(a.get(Calendar.YEAR),
                a.get(Calendar.MONTH),
                a.get(Calendar.DAY_OF_MONTH),
                a.get(0),
                a.get(0));
        esteDia.set(Calendar.SECOND,0);
        esteDia.set(Calendar.MILLISECOND,0);

        elOtro.set(b.get(Calendar.YEAR),
                b.get(Calendar.MONTH),
                b.get(Calendar.DAY_OF_MONTH),
                b.get(0),
                b.get(0));
        elOtro.set(Calendar.SECOND,0);
        elOtro.set(Calendar.MILLISECOND,0);

        return esteDia.compareTo(elOtro);
    }

    /**
     * Este metodo compara dos calendar por fecha hora, sin contar
     * los segundos y milisegundos
     *
     **/
    public static int compareHourTo(Calendar a, Calendar b) {
        Calendar esteDia = Calendar.getInstance();
        Calendar elOtro = Calendar.getInstance();
        //cargo segundos y milisegundos que no interesan en este compare.
        esteDia.set(Calendar.YEAR, a.get(Calendar.YEAR));
        esteDia.set(Calendar.MONTH, a.get(Calendar.MONTH));
        esteDia.set(Calendar.DAY_OF_MONTH,a.get(Calendar.DAY_OF_MONTH));
        esteDia.set(Calendar.HOUR_OF_DAY, a.get(Calendar.HOUR_OF_DAY));
        esteDia.set(Calendar.MINUTE, a.get(Calendar.MINUTE));
        esteDia.set(Calendar.SECOND, 0);
        esteDia.set(Calendar.MILLISECOND,0);

        elOtro.set(Calendar.YEAR, a.get(Calendar.YEAR));
        elOtro.set(Calendar.MONTH, a.get(Calendar.MONTH));
        elOtro.set(Calendar.DAY_OF_MONTH,a.get(Calendar.DAY_OF_MONTH));
        elOtro.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
        elOtro.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
        elOtro.set(Calendar.SECOND, 0);
        elOtro.set(Calendar.MILLISECOND,0);

        return esteDia.compareTo(elOtro);
    }
}

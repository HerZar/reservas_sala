package ar.com.eduit.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Calendar;

public class TimePickerFragment  extends DialogFragment {


    private static int hour;
    private static int minute;
    //private DatePickerDialog.OnDateSetListener listener;
    private TimePickerDialog.OnTimeSetListener listener;
    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener, int hora, int minuto) {
        TimePickerFragment fragment = new TimePickerFragment();
        hour = hora;
        minute = minuto;
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    @SuppressLint("WrongConstant")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default time in the picker
        // Create a new instance of DatePickerDialog and return it
        //return new DatePickerDialog(getActivity(), listener, year, month, day);
        if (hour ==-1) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = 0;
        }
        TimePickerDialog tpAux =new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, listener, hour, minute, true);
        return  tpAux;
    }


}

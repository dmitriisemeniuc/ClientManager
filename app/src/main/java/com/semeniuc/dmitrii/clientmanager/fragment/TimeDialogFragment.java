package com.semeniuc.dmitrii.clientmanager.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeDialogFragment extends DialogFragment {

    static Context context;
    static Calendar time;
    static TimeDialogFragmentListener listener;

    public static TimeDialogFragment newInstance(Context ctx, Calendar calendar){
        TimeDialogFragment dialog  = new TimeDialogFragment();

        context = ctx;
        time = calendar;

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return new TimePickerDialog(context, timeSetListener, time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), true);
    }

    public void setTimeDialogFragmentListener(TimeDialogFragmentListener lis){
        listener = lis;
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hour, int minute) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    //call back to the DateDialogFragment listener
                    listener.onTimeDialogFragmentTimeSet(calendar);
                }
            };

    //DateDialogFragment listener interface
    public interface TimeDialogFragmentListener{
        void onTimeDialogFragmentTimeSet(Calendar date);
    }
}

package com.semeniuc.dmitrii.clientmanager.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateDialogFragment extends DialogFragment {

    static Context context;
    static Calendar date;
    static DateDialogFragmentListener listener;

    public static DateDialogFragment newInstance(Context ctx, Calendar calendar){
        DateDialogFragment dialog  = new DateDialogFragment();

        context = ctx;
        date = calendar;

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return new DatePickerDialog(
                context,
                dateSetListener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
    }

    public void setDateDialogFragmentListener(DateDialogFragmentListener lis){
        listener = lis;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    //call back to the DateDialogFragment listener
                    listener.onDateDialogFragmentDateSet(newDate);
                }
            };

    //DateDialogFragment listener interface
    public interface DateDialogFragmentListener{
        void onDateDialogFragmentDateSet(Calendar date);
    }
}

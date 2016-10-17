package com.semeniuc.dmitrii.clientmanager.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;

import com.semeniuc.dmitrii.clientmanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Utils {

    private AppCompatActivity mActivity;

    public Utils(AppCompatActivity activity) {
        mActivity = activity;
    }

    public boolean isAppointmentFormValid() {

        boolean valid = true;
        // Fields of Appointment form
        List<AppCompatEditText> fields = new LinkedList<>();
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_name));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_client_name));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_client_phone));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_service));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_calendar_date));
        // Check if fields are not empty
        for (AppCompatEditText field : fields) {
            if (field.getText().toString().isEmpty()) {
                field.setError(mActivity.getResources().getString(R.string.field_is_required));
                valid = false;
            }
        }
        return valid;
    }

    public String getCurrentDate() {
        Locale locale = mActivity.getResources().getConfiguration().locale;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy/", locale);
        return df.format(Calendar.getInstance().getTime());
    }
}

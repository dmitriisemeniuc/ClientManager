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

    public AppCompatActivity mActivity;

    public Utils(AppCompatActivity activity) {
        mActivity = activity;
    }

    public boolean isAppointmentFormValid() {

        boolean valid = true;
        // Fields of Appointment form
        List<AppCompatEditText> fields = new LinkedList<>();
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_title));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_client_name));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_client_phone));
        fields.add((AppCompatEditText) mActivity.findViewById(R.id.appointment_service));
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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", getLocale());
        return df.format(Calendar.getInstance().getTime());
    }

    public Locale getLocale() {
        return mActivity.getResources().getConfiguration().locale;
    }
}

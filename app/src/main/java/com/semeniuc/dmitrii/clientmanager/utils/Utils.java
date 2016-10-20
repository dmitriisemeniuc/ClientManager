package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext = MyApplication.getInstance().getApplicationContext();

    public Utils() {
    }

    public Utils(AppCompatActivity activity) {
        mActivity = activity;
    }

    public boolean isAppointmentFormValid() {

        boolean valid = true;
        // Fields of Appointment form
        AppCompatEditText title = (AppCompatEditText) mActivity.findViewById(R.id.appointment_title);
        if (title.getText().toString().isEmpty()) {
            title.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        AppCompatEditText clientName = (AppCompatEditText) mActivity.findViewById(R.id.appointment_client_name);
        if (clientName.getText().toString().isEmpty()) {
            clientName.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        AppCompatEditText clientPhone = (AppCompatEditText) mActivity.findViewById(R.id.appointment_client_phone);
        if (clientPhone.getText().toString().isEmpty()) {
            clientPhone.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        AppCompatEditText service = (AppCompatEditText) mActivity.findViewById(R.id.appointment_service);
        if (service.getText().toString().isEmpty()) {
            service.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        AppCompatTextView date = (AppCompatTextView) mActivity.findViewById(R.id.appointment_calendar_date);
        if (date.getText().toString().isEmpty()) {
            date.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        return valid;
    }

    public String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        return dateFormat.format(date);
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void showSaveResultMessage(int saved, AppCompatActivity activity) {
        if (saved == Constants.CREATED) {
            Toast.makeText(mContext, activity.getResources()
                            .getString(R.string.appointment_saved),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, activity.getResources().getString(R.string.saving_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void showUpdateResultMessage(int updated, AppCompatActivity activity) {
        if (updated == Constants.UPDATED) {
            Toast.makeText(mContext, activity.getResources()
                            .getString(R.string.appointment_updated),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, activity.getResources().getString(R.string.updating_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void showDeleteResultMessage(int deleted, AppCompatActivity activity) {
        if (deleted == Constants.DELETED) {
            Toast.makeText(mContext, activity.getResources()
                            .getString(R.string.appointment_deleted),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, activity.getResources().getString(R.string.deleting_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Appointment copyAppointmentData(Appointment fromAppointment, Appointment toAppointment) {
        toAppointment.setTitle(fromAppointment.getTitle());
        toAppointment.setClientName(fromAppointment.getClientName());
        toAppointment.setClientPhone(fromAppointment.getClientPhone());
        toAppointment.setService(fromAppointment.getService());
        toAppointment.setInfo(fromAppointment.getInfo());
        toAppointment.setDate(fromAppointment.getDate());
        return toAppointment;
    }

    public String concat(int year, int month, int day) {
        String monthStr = String.valueOf(++month);
        String dayStr = String.valueOf(day);
        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        if (day < 10) {
            dayStr = "0" + dayStr;
        }
        return dayStr + "/" + monthStr + "/" + year;
    }
}

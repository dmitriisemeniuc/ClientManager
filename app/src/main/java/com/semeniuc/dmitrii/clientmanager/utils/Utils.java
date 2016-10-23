package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
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

        AppCompatTextView time = (AppCompatTextView) mActivity.findViewById(R.id.appointment_time);
        if (time.getText().toString().isEmpty()) {
            time.setError(mActivity.getResources().getString(R.string.field_is_required));
            valid = false;
        }
        return valid;
    }

    public boolean isEditTextEmpty(AppCompatEditText et){
        if (et.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public String convertDateToString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public Date convertStringToDate(String dateString, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateString);
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

    public Appointment updateAppointmentData(Appointment fromAppointment, Appointment toAppointment) {
        toAppointment.setClientName(fromAppointment.getClientName());
        toAppointment.setClientPhone(fromAppointment.getClientPhone());
        toAppointment.setService(fromAppointment.getService());
        toAppointment.setInfo(fromAppointment.getInfo());
        toAppointment.setDate(fromAppointment.getDate());
        return toAppointment;
    }

    public String getCorrectDateFormat(int year, int month, int day) {
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

    public String getCorrectTimeFormat(int hour, int minute){
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        if(hour < 10){
            hourStr = "0" + hourStr;
        }
        if(minute < 10){
            minuteStr = "0" + minuteStr;
        }
        return hourStr + ":" + minuteStr;
    }

    public String getUserFromPrefs(){
        SharedPreferences settings = mContext.getSharedPreferences(
                Constants.LOGIN_PREFS, mContext.MODE_PRIVATE);
        // setting.getString will return NEW_USER value in case if USER value won't be found
        return settings.getString(Constants.USER, Constants.NEW_USER);
    }

    public void setUserInPrefs(String user) {
        SharedPreferences.Editor editor = getEditor(Constants.LOGIN_PREFS);
        if(user.equals(Constants.NEW_USER)){
            editor.putString(Constants.USER, Constants.NEW_USER);
            editor.putString(Constants.EMAIL, Constants.EMPTY);
            editor.putBoolean(Constants.LOGGED_IN, false);
        } else {
            editor.putString(Constants.USER, user);
            editor.putString(Constants.EMAIL, MyApplication.getInstance().getUser().getEmail());
            editor.putBoolean(Constants.LOGGED_IN, true);
        }
        // Commit the edits!
        editor.commit();
    }

    public SharedPreferences.Editor getEditor(String prefs) {
        SharedPreferences settings = mContext.getSharedPreferences(prefs, mContext.MODE_PRIVATE);
        return settings.edit();
    }
}

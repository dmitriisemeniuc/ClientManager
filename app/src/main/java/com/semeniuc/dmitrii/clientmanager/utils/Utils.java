package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public boolean isEditTextEmpty(AppCompatEditText et) {
        return et.getText().toString().isEmpty();
    }

    public boolean isTextViewEmpty(AppCompatTextView tv) {
        return tv.getText().toString().isEmpty();
    }

    public String convertDateToString(Date date, String pattern, Context context) {
        Locale locale = getLocale(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        return dateFormat.format(date);
    }

    public Date convertStringToDate(String dateString, String pattern, Context context) {
        Locale locale = getLocale(context);
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Locale getLocale(Context context){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

    public Appointment updateAppointmentData(Appointment fromAppointment, Appointment toAppointment) {
        toAppointment.setClientName(fromAppointment.getClientName());
        toAppointment.setClientPhone(fromAppointment.getClientPhone());
        toAppointment.setService(fromAppointment.getService());
        toAppointment.setTools(fromAppointment.getTools());
        toAppointment.setSum(fromAppointment.getSum());
        toAppointment.setDone(fromAppointment.isDone());
        toAppointment.setPaid(fromAppointment.isPaid());
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

    public String getCorrectTimeFormat(int hour, int minute) {
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        if (minute < 10) {
            minuteStr = "0" + minuteStr;
        }
        return hourStr + ":" + minuteStr;
    }

    public String getUserFromPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.LOGIN_PREFS, Context.MODE_PRIVATE);
        // setting.getString will return NEW_USER value in case if USER value won't be found
        return settings.getString(Constants.USER, Constants.NEW_USER);
    }

    public void setUserInPrefs(String user, Context context) {
        SharedPreferences.Editor editor = getEditor(Constants.LOGIN_PREFS, context);
        if (user.equals(Constants.NEW_USER)) {
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

    public SharedPreferences.Editor getEditor(String prefs, Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        return settings.edit();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}

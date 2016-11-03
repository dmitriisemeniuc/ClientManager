package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isValidEditText(AppCompatEditText editText, Context context){
        boolean empty = isEditTextEmpty(editText);
        if (empty) editText.setError(context.getResources().getString(R.string.field_is_required));
        return !empty;
    }

    public static boolean isValidTextView(AppCompatTextView textView, Context context){
        boolean empty = isTextViewEmpty(textView);
        if (empty) textView.setError(context.getResources().getString(R.string.field_is_required));
        return !empty;
    }

    public static boolean isEqualPasswords(AppCompatEditText editText1,
                                           AppCompatEditText editText2, Context context){
        if (editText1.getText().toString().equals(editText2.getText().toString())) {
            return true;
        }
        editText2.setError(context.getResources().getString(R.string.passwords_not_match));
        return false;
    }

    public static boolean isEditTextEmpty(AppCompatEditText et) {
        return et.getText().toString().isEmpty();
    }

    public static boolean isTextViewEmpty(AppCompatTextView tv) {
        return tv.getText().toString().isEmpty();
    }

    public static String convertDateToString(Date date, String pattern, Context context) {
        Locale locale = getLocale(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        return dateFormat.format(date);
    }

    public static Date convertStringToDate(String dateString, String pattern, Context context) {
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

    public static Calendar getDateForDialog(String date, Context context) {
        final Calendar calendar = Calendar.getInstance();
        Date dateForDialog = convertStringToDate(date, Constants.DATE_FORMAT, context);
        calendar.setTime(dateForDialog);
        return calendar;
    }

    public static Calendar getTimeForDialog(String time, Context context) {
        final Calendar calendar = Calendar.getInstance();
        Date dateForDialog = convertStringToDate(time, Constants.TIME_FORMAT, context);
        calendar.setTime(dateForDialog);
        return calendar;
    }

    public static Locale getLocale(Context context){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

    public static Appointment updateAppointmentData(Appointment fromAppointment, Appointment toAppointment) {
        toAppointment.setClient(fromAppointment.getClient());
        toAppointment.getClient().setContact(fromAppointment.getClient().getContact());
        toAppointment.setService(fromAppointment.getService());
        toAppointment.setTools(fromAppointment.getTools());
        toAppointment.setSum(fromAppointment.getSum());
        toAppointment.setDone(fromAppointment.isDone());
        toAppointment.setPaid(fromAppointment.isPaid());
        toAppointment.setInfo(fromAppointment.getInfo());
        toAppointment.setDate(fromAppointment.getDate());
        return toAppointment;
    }

    public static String getCorrectDateFormat(int year, int month, int day) {
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

    public static String getCorrectTimeFormat(int hour, int minute) {
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

    public static String getUserFromPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.LOGIN_PREFS, Context.MODE_PRIVATE);
        // setting.getString will return NEW_USER value in case if USER value won't be found
        return settings.getString(Constants.USER, Constants.NEW_USER);
    }

    public static void setUserInPrefs(String user, Context context) {
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

    public static SharedPreferences.Editor getEditor(String prefs, Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        return settings.edit();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void hideKeyboard(ViewGroup mainLayout, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }
}

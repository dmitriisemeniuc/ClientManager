package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TimePicker;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_appointment;
    public static final String LOG_TAG = AppointmentActivity.class.getSimpleName();
    public static final int DATE_PICKER_DIALOG_ID = 1;
    public static final int TIME_PICKER_DIALOG_ID = 2;

    protected Utils mUtils = new Utils(AppointmentActivity.this);
    protected Appointment mAppointment;
    protected String mClientName;
    protected String mClientPhone;
    protected String mService;
    protected String mInfo;
    protected String mDate;
    protected String mTime;
    protected Date mDateTime;

    @BindView(R.id.appointment_client_name)
    AppCompatEditText clientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText clientPhone;
    @BindView(R.id.appointment_service)
    AppCompatEditText service;
    @BindView(R.id.appointment_info)
    AppCompatEditText info;
    @BindView(R.id.appointment_calendar_date)
    AppCompatTextView date;
    @BindView(R.id.appointment_time)
    AppCompatTextView time;
    @BindView(R.id.appointment_layout)
    ScrollView mainLayout;

    @OnClick(R.id.appointment_calendar_icon)
    void onCalendarIconClicked() {
        showPickerDialog(DATE_PICKER_DIALOG_ID);
    }

    @OnClick(R.id.appointment_calendar_date)
    void onCalendarDateClicked() {
        showPickerDialog(DATE_PICKER_DIALOG_ID);
    }

    @OnClick(R.id.appointment_time_icon)
    void onClockIconClicked() {
        showPickerDialog(TIME_PICKER_DIALOG_ID);
    }

    @OnClick(R.id.appointment_time)
    void onClockClicked() {
        showPickerDialog(TIME_PICKER_DIALOG_ID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appointment_toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_appointment:
                if (mUtils.isAppointmentFormValid()) {
                    setDataFromFields();
                    new SaveAppointment().execute();
                    return true;
                }
                hideKeyboard();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setDataFromFields() {
        mClientName = clientName.getText().toString();
        mClientPhone = clientPhone.getText().toString();
        mService = service.getText().toString();
        mInfo = info.getText().toString();
        mDate = date.getText().toString();
        mTime = time.getText().toString();
        String dateTime = mDate + " " + mTime;
        mDateTime = mUtils.convertStringToDate(dateTime, Constants.DATE_TIME_FORMAT);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_PICKER_DIALOG_ID) {
            return getDatePickerDialog();
        }
        if (id == TIME_PICKER_DIALOG_ID) {
            return getTimePickerDialog();
        }
        return null;
    }

    /**
     * Open picker dialog with date/time dialog id
     */
    protected void showPickerDialog(int dialogId) {
        hideKeyboard();
        showDialog(dialogId);
    }

    /*
    * Open date picker dialog with current date
    * */
    private DatePickerDialog getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
                this, datePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /*
    * Set chosen Date to date edit text
    * */
    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String formattedDate = mUtils.getCorrectDateFormat(year, month, day);
            date.setText(formattedDate);
            date.setError(null);
        }
    };

    /*
    * Open date picker dialog with current time
    * */
    private TimePickerDialog getTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
    }

    /*
    * Set chosen Time to time edit text
    * */
    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String formattedTime = mUtils.getCorrectTimeFormat(hourOfDay, minute);
            time.setText(formattedTime);
            time.setError(null);
        }
    };

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    /*
    * Save Appointment to DB (Create new one)
    * */
    private class SaveAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mAppointment = new Appointment(MyApplication.getInstance().getUser(), mClientName,
                    mClientPhone, mService, mInfo, mDateTime);
            AppointmentRepository appointmentRepo = new AppointmentRepository(
                    MyApplication.getInstance().getApplicationContext());
            return appointmentRepo.create(mAppointment);
        }

        @Override
        protected void onPostExecute(Integer created) {
            mUtils.showSaveResultMessage(created, AppointmentActivity.this);
            if (created == Constants.CREATED) finish();
            super.onPostExecute(created);
        }
    }

    protected String getDateFromDateAndTime() {
        return date.getText().toString() + " " + time.getText().toString();
    }
}

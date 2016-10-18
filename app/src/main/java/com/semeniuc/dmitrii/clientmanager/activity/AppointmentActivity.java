package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.AppointmentDbHelperImpl;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.IAppointmentDbHelper;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_appointment;
    public static final String LOG_TAG = AppointmentActivity.class.getSimpleName();
    public static final int DATEPICKER_DIALOG_ID = 1;

    private Utils mUtils = new Utils(AppointmentActivity.this);
    private Appointment mAppointment;

    @BindView(R.id.appointment_title)
    AppCompatEditText mAppointmentTitle;
    @BindView(R.id.appointment_client_name)
    AppCompatEditText mClientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText mClientPhone;
    @BindView(R.id.appointment_service)
    AppCompatEditText mService;
    @BindView(R.id.appointment_info)
    AppCompatEditText mInfo;
    @BindView(R.id.appointment_calendar_date)
    AppCompatTextView mDate;

    @OnClick(R.id.appointment_calendar_icon)
    void onCalendarIconClicked() {
        showDatePickerDialog(DATEPICKER_DIALOG_ID);
    }

    @OnClick(R.id.appointment_calendar_date)
    void onCalendarDateClicked() {
        showDatePickerDialog(DATEPICKER_DIALOG_ID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);

        setCurrentDateToCalendarDate();
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
                    new SaveAppointment().execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATEPICKER_DIALOG_ID) {
            return getDatePickerDialog();
        } else {
            return null;
        }
    }

    private void setCurrentDateToCalendarDate() {
        String date = mUtils.getCurrentDate();
        mDate.setText(date);
    }

    private DatePickerDialog getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
                this, datePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String date = day + "/" + ++month + "/" + year;
            mDate.setText(date);
        }
    };

    private void showDatePickerDialog(int dialogId) {
        showDialog(dialogId);
    }

    public Appointment createAppointment() {
        Appointment appointment = new Appointment();
        appointment.setTitle(mAppointmentTitle.getText().toString());
        appointment.setUserGoogleId(MyApplication.getInstance().getUser().getGoogleId());
        appointment.setClientName(mClientName.getText().toString());
        appointment.setClientPhone(mClientPhone.getText().toString());
        appointment.setService(mService.getText().toString());
        appointment.setInfo(mInfo.getText().toString());
        appointment.setDate(new Date(mDate.getText().toString()));
        return appointment;
    }

    private void showUserMessage(int saved, String title, String clientName) {
        switch (saved) {
            case Constants.APPOINTMENT_CREATED:
                Toast.makeText(this, getResources()
                                .getString(R.string.appointment_saved),
                        Toast.LENGTH_SHORT).show();
                break;
            case Constants.APPOINTMENT_NOT_CREATED:
                Toast.makeText(this, getResources().getString(R.string.saving_failed),
                        Toast.LENGTH_SHORT).show();
                break;
            case Constants.APPOINTMENT_EXISTS:
                Toast.makeText(this, getResources().getString(R.string.appointment_with_title) +
                                " '" + title + "' " + getResources().getString(R.string.and_with_client) +
                                " '" + clientName + "' - " + getResources().getString(R.string.already_exists),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class SaveAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mAppointment = createAppointment();
            IAppointmentDbHelper appointmentHelper = new AppointmentDbHelperImpl();
            return appointmentHelper.saveAppointmentToDb(mAppointment);
        }

        @Override
        protected void onPostExecute(Integer saved) {
            showUserMessage(saved, mAppointment.getTitle(), mAppointment.getClientName());
            if (saved == Constants.APPOINTMENT_CREATED) finish();
            super.onPostExecute(saved);
        }
    }
}

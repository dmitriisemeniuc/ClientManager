package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ServiceRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AppointmentReviewActivity extends AppointmentActivity {

    public static final String LOG_TAG = AppointmentReviewActivity.class.getSimpleName();

    private Context mContext = MyApplication.getInstance().getApplicationContext();
    private Appointment mAppointment;

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
    @BindView(R.id.appointment_cash)
    AppCompatEditText sum;
    @BindView(R.id.appointment_paid_icon)
    AppCompatImageView paidIcon;
    @BindView(R.id.appointment_done_icon)
    AppCompatImageView doneIcon;
    @BindView(R.id.appointment_service_hair_coloring_icon)
    AppCompatImageView hairColoringIcon;
    @BindView(R.id.appointment_service_hairdo_icon)
    AppCompatImageView hairdoIcon;
    @BindView(R.id.appointment_service_haircut_icon)
    AppCompatImageView haircutIcon;
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

    @OnClick(R.id.appointment_paid)
    void onPaidClicked() {
        changePaidImage();
    }

    @OnClick(R.id.appointment_paid_icon)
    void onPaidIconClicked() {
        changePaidImage();
    }

    @OnClick(R.id.appointment_done_icon)
    void onDoneIconClicked() {
        changeDoneImage();
    }

    @OnClick(R.id.appointment_service_hair_coloring_icon)
    void onHairColoringIconClicked() {
        changeHairColoringImage();
    }

    @OnClick(R.id.appointment_service_hairdo_icon)
    void onHairdoIconClicked() {
        changeHairdoImage();
    }

    @OnClick(R.id.appointment_service_haircut_icon)
    void onHaircutIconClicked() {
        changeHaircutImage();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        mAppointment = bundle.getParcelable(Constants.APPOINTMENT_PATH);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateAppointmentFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Toolbar menu with Delete|Update options
        inflater.inflate(R.menu.appointment_review_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_appointment:
                if (mUtils.isAppointmentFormValid()) {
                    setDataFromFields();
                    new UpdateAppointment().execute();
                } else {
                    hideKeyboard();
                }
                break;
            case R.id.action_delete_appointment:
                new DeleteAppointment().execute();
                break;
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_PICKER_DIALOG_ID) {
            return getDatePickerDialog();
        } else if (id == TIME_PICKER_DIALOG_ID) {
            return getTimePickerDialog();
        } else {
            return null;
        }
    }

    /**
     * Fill appointment form fields with coming data
     */
    public void populateAppointmentFields() {
        // text fields
        clientName.setText(mAppointment.getClientName());
        clientPhone.setText(mAppointment.getClientPhone());
        service.setText(mAppointment.getService().getName());
        sum.setText(mAppointment.getSum());
        info.setText(mAppointment.getInfo());
        date.setText(mUtils.convertDateToString(mAppointment.getDate(), Constants.DATE_FORMAT));
        time.setText(mUtils.convertDateToString(mAppointment.getDate(), Constants.TIME_FORMAT));
        // booleans
        boolean hairColoring = mAppointment.getService().isHairColoring();
        if (hairColoring) hairColoringIcon.setImageResource(R.mipmap.ic_paint_yes);
        boolean hairdo = mAppointment.getService().isHairdo();
        if (hairdo) hairdoIcon.setImageResource(R.mipmap.ic_womans_hair_yes);
        boolean haircut = mAppointment.getService().isHaircut();
        if (haircut) haircutIcon.setImageResource(R.mipmap.ic_scissors_yes);
        boolean paid = mAppointment.isPaid();
        if (paid) paidIcon.setImageResource(R.mipmap.ic_money_paid_yes);
        boolean done = mAppointment.isDone();
        if(done) doneIcon.setImageResource(R.mipmap.ic_ok_yes);
    }

    private void setDataFromFields() {
        mAppointment.setClientName(clientName.getText().toString());
        mAppointment.setClientPhone(clientPhone.getText().toString());
        mAppointment.getService().setName(service.getText().toString());
        mAppointment.setInfo(info.getText().toString());
        mAppointment.setSum(sum.getText().toString());
        mDate = date.getText().toString();
        mTime = time.getText().toString();
        String dateTime = mDate + " " + mTime;
        mDateTime = mUtils.convertStringToDate(dateTime, Constants.DATE_TIME_FORMAT);
        mAppointment.setDate(mDateTime);
    }

    /**
     * Open date picker dialog with date coming from Appointment
     */
    private DatePickerDialog getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        // Set date for dialog coming from appointment
        Date dateForDialog = mUtils.convertStringToDate(date.getText().toString(), Constants.DATE_FORMAT);
        calendar.setTime(dateForDialog);
        return new DatePickerDialog(
                this, datePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Open time picker dialog with time coming from Appointment
     */
    private TimePickerDialog getTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        // Set time for dialog coming from appointment
        Date dateForDialog = mUtils.convertStringToDate(time.getText().toString(), Constants.TIME_FORMAT);
        calendar.setTime(dateForDialog);
        return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
    }

    private class UpdateAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            ServiceRepository serviceRepo = new ServiceRepository(mContext);
            serviceRepo.update(mAppointment.getService());
            mAppointment.setUser(MyApplication.getInstance().getUser());
            AppointmentRepository appointmentRepo = new AppointmentRepository(mContext);
            return appointmentRepo.update(mAppointment);
        }

        @Override
        protected void onPostExecute(Integer updated) {
            mUtils.showUpdateResultMessage(updated, AppointmentReviewActivity.this);
            if (updated == Constants.UPDATED) finish();
        }
    }

    private class DeleteAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            ServiceRepository serviceRepo = new ServiceRepository(mContext);
            serviceRepo.delete(mService);
            AppointmentRepository appointmentRepo = new AppointmentRepository(mContext);
            return appointmentRepo.delete(mAppointment);
        }

        @Override
        protected void onPostExecute(Integer deleted) {
            mUtils.showDeleteResultMessage(deleted, AppointmentReviewActivity.this);
            if (deleted == Constants.DELETED) finish();
        }
    }

    // ********** Methods of onClick Image changing
    private void changePaidImage() {
        boolean paid = !mAppointment.isPaid();
        mAppointment.setPaid(paid);
        if (paid) {
            paidIcon.setImageResource(R.mipmap.ic_money_paid_yes);
            return;
        }
        paidIcon.setImageResource(R.mipmap.ic_money_paid_no);
    }

    private void changeDoneImage() {
        boolean done = !mAppointment.isDone();
        mAppointment.setDone(done);
        if (done) {
            doneIcon.setImageResource(R.mipmap.ic_ok_yes);
            return;
        }
        doneIcon.setImageResource(R.mipmap.ic_ok_no);
    }

    private void changeHairColoringImage() {
        boolean hairColoring = !mAppointment.getService().isHairColoring();
        mAppointment.getService().setHairColoring(hairColoring);
        if (hairColoring) {
            hairColoringIcon.setImageResource(R.mipmap.ic_paint_yes);
            return;
        }
        hairColoringIcon.setImageResource(R.mipmap.ic_paint_no);
    }

    private void changeHairdoImage() {
        boolean hairdo = !mAppointment.getService().isHairdo();
        mAppointment.getService().setHairdo(hairdo);
        if (hairdo) {
            hairdoIcon.setImageResource(R.mipmap.ic_womans_hair_yes);
            return;
        }
        hairdoIcon.setImageResource(R.mipmap.ic_womans_hair_no);
    }

    private void changeHaircutImage() {
        boolean haircut = !mAppointment.getService().isHaircut();
        mAppointment.getService().setHaircut(haircut);
        if (haircut) {
            haircutIcon.setImageResource(R.mipmap.ic_scissors_yes);
            return;
        }
        haircutIcon.setImageResource(R.mipmap.ic_scissors_no);
    }
}

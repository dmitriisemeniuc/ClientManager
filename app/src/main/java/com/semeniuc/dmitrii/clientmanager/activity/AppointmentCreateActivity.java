package com.semeniuc.dmitrii.clientmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.fragment.DateDialogFragment;
import com.semeniuc.dmitrii.clientmanager.fragment.TimeDialogFragment;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.Client;
import com.semeniuc.dmitrii.clientmanager.model.Contact;
import com.semeniuc.dmitrii.clientmanager.model.Service;
import com.semeniuc.dmitrii.clientmanager.model.Tools;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointmentCreateActivity extends BaseActivity {

    @BindView(R.id.appointment_client_name)
    AppCompatEditText editTextClientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText editTextClientPhone;
    @BindView(R.id.appointment_client_address)
    AppCompatEditText editTextAddress;
    @BindView(R.id.appointment_service)
    AppCompatEditText editTextService;
    @BindView(R.id.appointment_info)
    AppCompatEditText editTextInfo;
    @BindView(R.id.appointment_calendar_date)
    AppCompatTextView textViewDate;
    @BindView(R.id.appointment_time)
    AppCompatTextView textViewTime;
    @BindView(R.id.appointment_cash)
    AppCompatEditText editTextSum;
    @BindView(R.id.appointment_paid_icon)
    AppCompatImageView imageViewPaid;
    @BindView(R.id.appointment_done_icon)
    AppCompatImageView imageViewDone;
    @BindView(R.id.appointment_service_hair_coloring_icon)
    AppCompatImageView imageViewHairColoring;
    @BindView(R.id.appointment_service_hairdo_icon)
    AppCompatImageView imageViewHairdo;
    @BindView(R.id.appointment_service_haircut_icon)
    AppCompatImageView imageViewHaircut;
    @BindView(R.id.appointment_tools_brush_icon)
    AppCompatImageView imageViewBrush;
    @BindView(R.id.appointment_tools_hair_brush_icon)
    AppCompatImageView imageViewHairBrush;
    @BindView(R.id.appointment_tools_hair_dryer_icon)
    AppCompatImageView imageViewHairDryer;
    @BindView(R.id.appointment_tools_oxy_icon)
    AppCompatImageView imageViewOxy;
    @BindView(R.id.appointment_tools_cut_set_icon)
    AppCompatImageView imageViewCutSet;
    @BindView(R.id.appointment_tools_hair_band_icon)
    AppCompatImageView imageViewHairBand;
    @BindView(R.id.appointment_tools_spray_icon)
    AppCompatImageView imageViewSpray;
    @BindView(R.id.appointment_tools_tube_icon)
    AppCompatImageView imageViewTube;
    @BindView(R.id.appointment_tools_trimmer_icon)
    AppCompatImageView imageViewTrimmer;
    @BindView(R.id.appointment_layout)
    ScrollView mainLayout;

    @OnClick(R.id.appointment_calendar_icon)
    void onCalendarIconClicked() {
        Utils.hideKeyboard(mainLayout, this);
        showDatePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_calendar_date)
    void onCalendarDateClicked() {
        Utils.hideKeyboard(mainLayout, this);
        showDatePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_time_icon)
    void onClockIconClicked() {
        Utils.hideKeyboard(mainLayout, this);
        showTimePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_time)
    void onClockClicked() {
        Utils.hideKeyboard(mainLayout, this);
        showTimePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_paid_icon)
    void onPaidIconClicked() {
        appointment.setPaid(!appointment.isPaid());
        changeImage(Constants.PAID, appointment, imageViewPaid);
    }

    @OnClick(R.id.appointment_done_icon)
    void onDoneIconClicked() {
        appointment.setDone(!appointment.isDone());
        changeImage(Constants.DONE, appointment, imageViewDone);
    }

    @OnClick(R.id.appointment_service_hair_coloring_icon)
    void onHairColoringIconClicked() {
        appointment.getService().setHairColoring(!appointment.getService().isHairColoring());
        changeImage(Constants.HAIR_COLORING, appointment, imageViewHairColoring);
    }

    @OnClick(R.id.appointment_service_hairdo_icon)
    void onHairdoIconClicked() {
        appointment.getService().setHairdo(!appointment.getService().isHairdo());
        changeImage(Constants.HAIRDO, appointment, imageViewHairdo);
    }

    @OnClick(R.id.appointment_service_haircut_icon)
    void onHaircutIconClicked() {
        appointment.getService().setHaircut(!appointment.getService().isHaircut());
        changeImage(Constants.HAIR_CUT, appointment, imageViewHaircut);
    }

    @OnClick(R.id.appointment_tools_brush_icon)
    void onBrushIconClicked() {
        appointment.getTools().setBrush(!appointment.getTools().isBrush());
        changeImage(Constants.BRUSH, appointment, imageViewBrush);
    }

    @OnClick(R.id.appointment_tools_hair_brush_icon)
    void onHairBrushIconClicked() {
        appointment.getTools().setHairBrush(!appointment.getTools().isHairBrush());
        changeImage(Constants.HAIR_BRUSH, appointment, imageViewHairBrush);
    }

    @OnClick(R.id.appointment_tools_hair_dryer_icon)
    void onHairDryerIconClicked() {
        appointment.getTools().setHairDryer(!appointment.getTools().isHairDryer());
        changeImage(Constants.HAIR_DRAYER, appointment, imageViewHairDryer);
    }

    @OnClick(R.id.appointment_tools_oxy_icon)
    void onOxyIconClicked() {
        appointment.getTools().setOxy(!appointment.getTools().isOxy());
        changeImage(Constants.OXY, appointment, imageViewOxy);
    }

    @OnClick(R.id.appointment_tools_cut_set_icon)
    void onCutSetIconClicked() {
        appointment.getTools().setCutSet(!appointment.getTools().isCutSet());
        changeImage(Constants.CUT_SET, appointment, imageViewCutSet);
    }

    @OnClick(R.id.appointment_tools_hair_band_icon)
    void onHairBandIconClicked() {
        appointment.getTools().setHairBand(!appointment.getTools().isHairBand());
        changeImage(Constants.HAIR_BAND, appointment, imageViewHairBand);
    }

    @OnClick(R.id.appointment_tools_spray_icon)
    void onSprayIconClicked() {
        appointment.getTools().setSpray(!appointment.getTools().isSpray());
        changeImage(Constants.SPRAY, appointment, imageViewSpray);
    }

    @OnClick(R.id.appointment_tools_tube_icon)
    void onTubeClicked() {
        appointment.getTools().setTube(!appointment.getTools().isTube());
        changeImage(Constants.TUBE, appointment, imageViewTube);
    }

    @OnClick(R.id.appointment_tools_trimmer_icon)
    void onTrimmerIconClicked() {
        appointment.getTools().setTrimmer(!appointment.getTools().isTrimmer());
        changeImage(Constants.TRIMMER, appointment, imageViewTrimmer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        ButterKnife.bind(this);
        initInstances();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.hideKeyboard(mainLayout, this);
        if (item.getItemId() == R.id.action_save_appointment) {
            if (!isAppointmentFormValid()) return false;
            setDataFromFields();
            saveAppointment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInstances() {
        appointment = new Appointment();
        appointment.setClient(new Client(MyApplication.getInstance().getUser()));
        appointment.getClient().setContact(new Contact());
        appointment.setService(new Service());
        appointment.setTools(new Tools());
        dbHelper = new DatabaseTaskHelper();
        listener = this;
    }

    /**
     * Save Appointment to DB (Create new one) using RXJava
     */
    private void saveAppointment() {

        observable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.CREATED) {
                    listener.showMessage(getResources().getString(R.string.appointment_saved));
                    finish();
                } else listener.showMessage(getResources().getString(R.string.saving_failed));
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.getMessage();
            }
        });
    }

    final Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {

        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            Integer result = dbHelper.saveAppointment(appointment);
            subscriber.onNext(result);
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    /**
     * Open time picker
     */
    protected void showDatePickerDialog(Calendar calendar) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(this, calendar);
        dateDialogFragment.setDateDialogFragmentListener(date1 -> {
            String formattedDate = Utils.getCorrectDateFormat(
                    date1.get(Calendar.YEAR),
                    date1.get(Calendar.MONTH),
                    date1.get(Calendar.DAY_OF_MONTH));
            textViewDate.setText(formattedDate);
            textViewDate.setError(null);
        });
        dateDialogFragment.show(getSupportFragmentManager(), "date picker dialog fragment");
    }

    /**
     * Open time picker dialog
     */
    protected void showTimePickerDialog(Calendar calendar) {
        TimeDialogFragment timeDialogFragment = TimeDialogFragment.newInstance(this, calendar);
        timeDialogFragment.setTimeDialogFragmentListener(time1 -> {
            String formattedTime = Utils.getCorrectTimeFormat(
                    time1.get(Calendar.HOUR_OF_DAY),
                    time1.get(Calendar.MINUTE));
            textViewTime.setText(formattedTime);
            textViewTime.setError(null);
        });
        timeDialogFragment.show(getSupportFragmentManager(), "time picker dialog fragment");
    }

    protected boolean isAppointmentFormValid() {
        boolean valid = true;
        if (!Utils.isValidEditText(editTextClientName, this)) valid = false;
        if (!Utils.isValidEditText(editTextService, this)) valid = false;
        if (!Utils.isValidTextView(textViewTime, this)) valid = false;
        if (!Utils.isValidTextView(textViewDate, this)) valid = false;
        return valid;
    }

    protected void setDataFromFields() {
        appointment = setDataFromFields(
                appointment,
                editTextClientName.getText().toString(),
                editTextClientPhone.getText().toString(),
                editTextAddress.getText().toString(),
                editTextService.getText().toString(),
                editTextSum.getText().toString(),
                editTextInfo.getText().toString(),
                textViewDate.getText().toString() + " " +
                        textViewTime.getText().toString(),
                this
        );
    }
}

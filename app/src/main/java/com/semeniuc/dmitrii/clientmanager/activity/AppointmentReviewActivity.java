package com.semeniuc.dmitrii.clientmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointmentReviewActivity extends AppointmentCreateActivity implements OnTaskCompleted {

    public static final String LOG_TAG = AppointmentReviewActivity.class.getSimpleName();

    private Appointment appointment;
    private DatabaseTaskHelper dbHelper;
    private OnTaskCompleted listener;
    private Utils utils;

    @BindView(R.id.appointment_client_name)
    AppCompatEditText etClientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText etClientPhone;
    @BindView(R.id.appointment_client_address)
    AppCompatEditText etAddress;
    @BindView(R.id.appointment_service)
    AppCompatEditText etService;
    @BindView(R.id.appointment_info)
    AppCompatEditText etInfo;
    @BindView(R.id.appointment_calendar_date)
    AppCompatTextView tvDate;
    @BindView(R.id.appointment_time)
    AppCompatTextView tvTime;
    @BindView(R.id.appointment_cash)
    AppCompatEditText etSum;
    @BindView(R.id.appointment_paid_icon)
    AppCompatImageView ivPaid;
    @BindView(R.id.appointment_done_icon)
    AppCompatImageView ivDone;
    @BindView(R.id.appointment_service_hair_coloring_icon)
    AppCompatImageView ivHairColoring;
    @BindView(R.id.appointment_service_hairdo_icon)
    AppCompatImageView ivHairdo;
    @BindView(R.id.appointment_service_haircut_icon)
    AppCompatImageView ivHaircut;
    @BindView(R.id.appointment_tools_brush_icon)
    AppCompatImageView ivBrush;
    @BindView(R.id.appointment_tools_hair_brush_icon)
    AppCompatImageView ivHairBrush;
    @BindView(R.id.appointment_tools_hair_dryer_icon)
    AppCompatImageView ivHairDryer;
    @BindView(R.id.appointment_tools_oxy_icon)
    AppCompatImageView ivOxy;
    @BindView(R.id.appointment_tools_tube_icon)
    AppCompatImageView ivTube;
    @BindView(R.id.appointment_tools_cut_set_icon)
    AppCompatImageView ivCutSet;
    @BindView(R.id.appointment_tools_hair_band_icon)
    AppCompatImageView ivHairBand;
    @BindView(R.id.appointment_tools_spray_icon)
    AppCompatImageView ivSpray;
    @BindView(R.id.appointment_tools_trimmer_icon)
    AppCompatImageView ivTrimmer;
    @BindView(R.id.appointment_layout)
    ScrollView mainLayout;

    @OnClick(R.id.appointment_calendar_icon)
    void onCalendarIconClicked() {
        hideKeyboard();
        Calendar calendar = getDateForDialog();
        showDatePickerDialog(calendar);
    }

    @OnClick(R.id.appointment_calendar_date)
    void onCalendarDateClicked() {
        hideKeyboard();
        Calendar calendar = getDateForDialog();
        showDatePickerDialog(calendar);
    }

    @OnClick(R.id.appointment_time_icon)
    void onClockIconClicked() {
        hideKeyboard();
        Calendar calendar = getTimeForDialog();
        showTimePickerDialog(calendar);
    }

    @OnClick(R.id.appointment_time)
    void onClockClicked() {
        hideKeyboard();
        Calendar calendar = getTimeForDialog();
        showTimePickerDialog(calendar);
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

    @OnClick(R.id.appointment_tools_brush_icon)
    void onBrushIconClicked() {
        changeBrushImage();
    }

    @OnClick(R.id.appointment_tools_hair_brush_icon)
    void onHairBrushIconClicked() {
        changeHairBrushImage();
    }

    @OnClick(R.id.appointment_tools_hair_dryer_icon)
    void onHairDryerIconClicked() {
        changeHairDryerImage();
    }

    @OnClick(R.id.appointment_tools_oxy_icon)
    void onOxyIconClicked() {
        changeOxyImage();
    }

    @OnClick(R.id.appointment_tools_cut_set_icon)
    void onCutSetIconClicked() {
        changeCutSetImage();
    }

    @OnClick(R.id.appointment_tools_hair_band_icon)
    void onHairBandIconClicked() {
        changeHairBandImage();
    }

    @OnClick(R.id.appointment_tools_spray_icon)
    void onSprayIconClicked() {
        changeSprayImage();
    }

    @OnClick(R.id.appointment_tools_tube_icon)
    void onTubeClicked() {
        changeTubeImage();
    }

    @OnClick(R.id.appointment_tools_trimmer_icon)
    void onTrimmerIconClicked() {
        changeTrimmerImage();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getIntent().getExtras().getParcelable(Constants.APPOINTMENT_PATH);
        dbHelper = new DatabaseTaskHelper();
        utils = new Utils();
        listener = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateAppointmentFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Toolbar menu with Delete|Update options
        getMenuInflater().inflate(R.menu.appointment_review_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_appointment:
                boolean valid = super.isAppointmentFormValid();
                if (valid) {
                    setDataFromFields();
                    updateAppointment();
                } else {
                    hideKeyboard();
                }
                break;
            case R.id.action_delete_appointment:
                deleteAppointment();
                break;
        }
        return true;
    }

    private Calendar getDateForDialog() {
        final Calendar calendar = Calendar.getInstance();
        Date dateForDialog = utils.convertStringToDate(
                tvDate.getText().toString(), Constants.DATE_FORMAT, this);
        calendar.setTime(dateForDialog);
        return calendar;
    }

    private Calendar getTimeForDialog() {
        final Calendar calendar = Calendar.getInstance();
        Date dateForDialog = utils.convertStringToDate(
                tvTime.getText().toString(), Constants.TIME_FORMAT, this);
        calendar.setTime(dateForDialog);
        return calendar;
    }

    /**
     * Fill appointment form fields with incoming data
     */
    public void populateAppointmentFields() {
        // text fields
        etClientName.setText(appointment.getClient().getName());
        etClientPhone.setText(appointment.getClient().getContact().getPhone());
        etAddress.setText(appointment.getClient().getContact().getAddress());
        etService.setText(appointment.getService().getName());
        etSum.setText(appointment.getSum());
        etInfo.setText(appointment.getInfo());
        tvDate.setText(utils.convertDateToString(appointment.getDate(), Constants.DATE_FORMAT, this));
        tvTime.setText(utils.convertDateToString(appointment.getDate(), Constants.TIME_FORMAT, this));
        // Services
        boolean hairColoring = appointment.getService().isHairColoring();
        if (hairColoring) ivHairColoring.setImageResource(R.mipmap.ic_paint_yes);
        boolean hairdo = appointment.getService().isHairdo();
        if (hairdo) ivHairdo.setImageResource(R.mipmap.ic_womans_hair_yes);
        boolean haircut = appointment.getService().isHaircut();
        if (haircut) ivHaircut.setImageResource(R.mipmap.ic_scissors_yes);
        boolean paid = appointment.isPaid();
        if (paid) ivPaid.setImageResource(R.mipmap.ic_money_paid_yes);
        boolean done = appointment.isDone();
        if (done) ivDone.setImageResource(R.mipmap.ic_ok_yes);
        // Tools
        boolean brush = appointment.getTools().isBrush();
        if (brush) ivBrush.setImageResource(R.mipmap.ic_brush_yes);
        boolean hairBrush = appointment.getTools().isHairBrush();
        if (hairBrush) ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_yes);
        boolean hairDryer = appointment.getTools().isHairDryer();
        if (hairDryer) ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_yes);
        boolean hairBand = appointment.getTools().isHairBand();
        if (hairBand) ivHairBand.setImageResource(R.mipmap.ic_hair_band_yes);
        boolean cutSet = appointment.getTools().isCutSet();
        if (cutSet) ivCutSet.setImageResource(R.mipmap.ic_cutset_yes);
        boolean spray = appointment.getTools().isSpray();
        if (spray) ivSpray.setImageResource(R.mipmap.ic_spray_yes);
        boolean oxy = appointment.getTools().isOxy();
        if (oxy) ivOxy.setImageResource(R.mipmap.ic_soap_yes);
        boolean tube = appointment.getTools().isTube();
        if (tube) ivTube.setImageResource(R.mipmap.ic_tube_yes);
        boolean trimmer = appointment.getTools().isTrimmer();
        if (trimmer) ivTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
    }

    private void setDataFromFields() {
        appointment.getClient().setName(etClientName.getText().toString());
        appointment.getClient().getContact().setPhone(etClientPhone.getText().toString());
        appointment.getClient().getContact().setAddress(etAddress.getText().toString());
        appointment.getService().setName(etService.getText().toString());
        appointment.setInfo(etInfo.getText().toString());
        appointment.setSum(etSum.getText().toString());
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();
        String dateTimeStr = date + " " + time;
        Date dateTime = utils.convertStringToDate(dateTimeStr, Constants.DATE_TIME_FORMAT, this);
        appointment.setDate(dateTime);
    }

    /**
     * Update Appointment in DB using RXJava
     */
    private void updateAppointment() {

        updateObservable.subscribe(new Subscriber() {

            @Override
            public void onNext(Object o) {
                Integer result = (Integer) o;
                if (result == Constants.UPDATED) {
                    String message = getResources().getString(R.string.appointment_updated);
                    listener.showMessage(message);
                    finish();
                    return;
                }
                String message = getResources().getString(R.string.updating_failed);
                listener.showMessage(message);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "Error: " + e.getMessage());
            }
        });
    }

    // OBSERVABLE
    final Observable updateObservable = Observable.create(new Observable.OnSubscribe() {
        @Override
        public void call(Object o) {
            Subscriber subscriber = (Subscriber) o;
            subscriber.onNext(dbHelper.updateAppointment(appointment));
            subscriber.onCompleted();
        }
    })
            .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
            .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread

    /**
     * Delete Appointment from DB using RXJava
     */
    private void deleteAppointment() {

        deleteObservable.subscribe(new Subscriber() {

            @Override
            public void onNext(Object o) {
                Integer result = (Integer) o;
                if (result == Constants.DELETED) {
                    String message = getResources().getString(R.string.appointment_deleted);
                    listener.showMessage(message);
                    finish();
                    return;
                }
                String message = getResources().getString(R.string.deleting_failed);
                listener.showMessage(message);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "Error: " + e.getMessage());
            }
        });
    }

    // DELETE OBSERVABLE
    final Observable deleteObservable = Observable.create(new Observable.OnSubscribe() {
        @Override
        public void call(Object o) {
            Subscriber subscriber = (Subscriber) o;
            subscriber.onNext(dbHelper.deleteAppointment(appointment));
            subscriber.onCompleted();
        }
    })
            .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
            .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread

    // ********** Methods of onClick Image changing **********
    private void changePaidImage() {
        boolean paid = !appointment.isPaid();
        appointment.setPaid(paid);
        if (paid) {
            ivPaid.setImageResource(R.mipmap.ic_money_paid_yes);
            return;
        }
        ivPaid.setImageResource(R.mipmap.ic_money_paid_no);
    }

    private void changeDoneImage() {
        boolean done = !appointment.isDone();
        appointment.setDone(done);
        if (done) {
            ivDone.setImageResource(R.mipmap.ic_ok_yes);
            return;
        }
        ivDone.setImageResource(R.mipmap.ic_ok_no);
    }

    private void changeHairColoringImage() {
        boolean hairColoring = !appointment.getService().isHairColoring();
        appointment.getService().setHairColoring(hairColoring);
        if (hairColoring) {
            ivHairColoring.setImageResource(R.mipmap.ic_paint_yes);
            return;
        }
        ivHairColoring.setImageResource(R.mipmap.ic_paint_no);
    }

    private void changeHairdoImage() {
        boolean hairdo = !appointment.getService().isHairdo();
        appointment.getService().setHairdo(hairdo);
        if (hairdo) {
            ivHairdo.setImageResource(R.mipmap.ic_womans_hair_yes);
            return;
        }
        ivHairdo.setImageResource(R.mipmap.ic_womans_hair_no);
    }

    private void changeHaircutImage() {
        boolean haircut = !appointment.getService().isHaircut();
        appointment.getService().setHaircut(haircut);
        if (haircut) {
            ivHaircut.setImageResource(R.mipmap.ic_scissors_yes);
            return;
        }
        ivHaircut.setImageResource(R.mipmap.ic_scissors_no);
    }

    private void changeBrushImage() {
        boolean brush = !appointment.getTools().isBrush();
        appointment.getTools().setBrush(brush);
        if (brush) {
            ivBrush.setImageResource(R.mipmap.ic_brush_yes);
            return;
        }
        ivBrush.setImageResource(R.mipmap.ic_brush_no);
    }

    private void changeHairBrushImage() {
        boolean hairBrush = !appointment.getTools().isHairBrush();
        appointment.getTools().setHairBrush(hairBrush);
        if (hairBrush) {
            ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_yes);
            return;
        }
        ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_no);
    }

    private void changeHairDryerImage() {
        boolean hairDryer = !appointment.getTools().isHairDryer();
        appointment.getTools().setHairDryer(hairDryer);
        if (hairDryer) {
            ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_yes);
            return;
        }
        ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_no);
    }

    private void changeOxyImage() {
        boolean oxy = !appointment.getTools().isOxy();
        appointment.getTools().setOxy(oxy);
        if (oxy) {
            ivOxy.setImageResource(R.mipmap.ic_soap_yes);
            return;
        }
        ivOxy.setImageResource(R.mipmap.ic_soap_no);
    }

    private void changeCutSetImage() {
        boolean cutSet = !appointment.getTools().isCutSet();
        appointment.getTools().setCutSet(cutSet);
        if (cutSet) {
            ivCutSet.setImageResource(R.mipmap.ic_cutset_yes);
            return;
        }
        ivCutSet.setImageResource(R.mipmap.ic_cutset_no);
    }

    private void changeHairBandImage() {
        boolean hairBand = !appointment.getTools().isHairBand();
        appointment.getTools().setHairBand(hairBand);
        if (hairBand) {
            ivHairBand.setImageResource(R.mipmap.ic_hair_band_yes);
            return;
        }
        ivHairBand.setImageResource(R.mipmap.ic_hair_band_no);
    }

    private void changeSprayImage() {
        boolean spray = !appointment.getTools().isSpray();
        appointment.getTools().setSpray(spray);
        if (spray) {
            ivSpray.setImageResource(R.mipmap.ic_spray_yes);
            return;
        }
        ivSpray.setImageResource(R.mipmap.ic_spray_no);
    }

    private void changeTubeImage() {
        boolean tube = !appointment.getTools().isTube();
        appointment.getTools().setTube(tube);
        if (tube) {
            ivTube.setImageResource(R.mipmap.ic_tube_yes);
            return;
        }
        ivTube.setImageResource(R.mipmap.ic_tube_no);
    }

    private void changeTrimmerImage() {
        boolean trimmer = !appointment.getTools().isTrimmer();
        appointment.getTools().setTrimmer(trimmer);
        if (trimmer) {
            ivTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
            return;
        }
        ivTrimmer.setImageResource(R.mipmap.ic_trimmer_no);
    }
}

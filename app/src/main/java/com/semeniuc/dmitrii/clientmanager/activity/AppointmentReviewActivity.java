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
import com.semeniuc.dmitrii.clientmanager.repository.ToolsRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AppointmentReviewActivity extends AppointmentActivity {

    public static final String LOG_TAG = AppointmentReviewActivity.class.getSimpleName();

    private Context context = MyApplication.getInstance().getApplicationContext();
    private Appointment appointment;

    @BindView(R.id.appointment_client_name)
    AppCompatEditText etClientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText etClientPhone;
    @BindView(R.id.appointment_service)
    AppCompatEditText etService;
    @BindView(R.id.appointment_info)
    AppCompatEditText etInfo;
    @BindView(R.id.appointment_calendar_date)
    AppCompatTextView etDate;
    @BindView(R.id.appointment_time)
    AppCompatTextView etTime;
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

        Bundle bundle = getIntent().getExtras();
        appointment = bundle.getParcelable(Constants.APPOINTMENT_PATH);
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
                if (utils.isAppointmentFormValid()) {
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
        etClientName.setText(appointment.getClientName());
        etClientPhone.setText(appointment.getClientPhone());
        etService.setText(appointment.getService().getName());
        etSum.setText(appointment.getSum());
        etInfo.setText(appointment.getInfo());
        etDate.setText(utils.convertDateToString(appointment.getDate(), Constants.DATE_FORMAT));
        etTime.setText(utils.convertDateToString(appointment.getDate(), Constants.TIME_FORMAT));
        // booleans
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
        if(done) ivDone.setImageResource(R.mipmap.ic_ok_yes);
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
        if (oxy) ivOxy.setImageResource(R.mipmap.ic_ok_yes);
        boolean tube = appointment.getTools().isTube();
        if (tube) ivTube.setImageResource(R.mipmap.ic_tube_yes);
        boolean trimmer = appointment.getTools().isTrimmer();
        if (trimmer) ivTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
    }

    private void setDataFromFields() {
        appointment.setClientName(etClientName.getText().toString());
        appointment.setClientPhone(etClientPhone.getText().toString());
        appointment.getService().setName(etService.getText().toString());
        appointment.setInfo(etInfo.getText().toString());
        appointment.setSum(etSum.getText().toString());
        date = etDate.getText().toString();
        time = etTime.getText().toString();
        String dateTime = date + " " + time;
        this.dateTime = utils.convertStringToDate(dateTime, Constants.DATE_TIME_FORMAT);
        appointment.setDate(this.dateTime);
    }

    /**
     * Open etDate picker dialog with etDate coming from Appointment
     */
    private DatePickerDialog getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        // Set date for dialog coming from appointment
        Date dateForDialog = utils.convertStringToDate(etDate.getText().toString(), Constants.DATE_FORMAT);
        calendar.setTime(dateForDialog);
        return new DatePickerDialog(
                this, datePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Open etTime picker dialog with etTime coming from Appointment
     */
    private TimePickerDialog getTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        // Set time for dialog coming from appointment
        Date dateForDialog = utils.convertStringToDate(etTime.getText().toString(), Constants.TIME_FORMAT);
        calendar.setTime(dateForDialog);
        return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
    }

    private class UpdateAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            ServiceRepository serviceRepo = new ServiceRepository(context);
            serviceRepo.update(appointment.getService());
            ToolsRepository toolsRepo = new ToolsRepository(context);
            toolsRepo.update(appointment.getTools());
            appointment.setUser(MyApplication.getInstance().getUser());
            AppointmentRepository appointmentRepo = new AppointmentRepository(context);
            return appointmentRepo.update(appointment);
        }

        @Override
        protected void onPostExecute(Integer updated) {
            utils.showUpdateResultMessage(updated, AppointmentReviewActivity.this);
            if (updated == Constants.UPDATED) finish();
        }
    }

    private class DeleteAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            ServiceRepository serviceRepo = new ServiceRepository(context);
            serviceRepo.delete(service);
            ToolsRepository toolsRepo = new ToolsRepository(context);
            toolsRepo.delete(tools);
            AppointmentRepository appointmentRepo = new AppointmentRepository(context);
            return appointmentRepo.delete(appointment);
        }

        @Override
        protected void onPostExecute(Integer deleted) {
            utils.showDeleteResultMessage(deleted, AppointmentReviewActivity.this);
            if (deleted == Constants.DELETED) finish();
        }
    }

    // ********** Methods of onClick Image changing
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

    private void changeBrushImage(){
        boolean brush = !appointment.getTools().isBrush();
        appointment.getTools().setBrush(brush);
        if (brush) {
            ivBrush.setImageResource(R.mipmap.ic_brush_yes);
            return;
        }
        ivBrush.setImageResource(R.mipmap.ic_brush_no);
    }

    private void changeHairBrushImage(){
        boolean hairBrush = !appointment.getTools().isHairBrush();
        appointment.getTools().setHairBrush(hairBrush);
        if (hairBrush) {
            ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_yes);
            return;
        }
        ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_no);
    }

    private void changeHairDryerImage(){
        boolean hairDryer = !appointment.getTools().isHairDryer();
        appointment.getTools().setHairDryer(hairDryer);
        if (hairDryer) {
            ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_yes);
            return;
        }
        ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_no);
    }

    private void changeOxyImage(){
        boolean oxy = !appointment.getTools().isOxy();
        appointment.getTools().setOxy(oxy);
        if (oxy) {
            ivOxy.setImageResource(R.mipmap.ic_soap_yes);
            return;
        }
        ivOxy.setImageResource(R.mipmap.ic_soap_no);
    }

    private void changeCutSetImage(){
        boolean cutSet = !appointment.getTools().isCutSet();
        appointment.getTools().setCutSet(cutSet);
        if (cutSet) {
            ivCutSet.setImageResource(R.mipmap.ic_cutset_yes);
            return;
        }
        ivCutSet.setImageResource(R.mipmap.ic_cutset_no);
    }

    private void changeHairBandImage(){
        boolean hairBand = !appointment.getTools().isHairBand();
        appointment.getTools().setHairBand(hairBand);
        if (hairBand) {
            ivHairBand.setImageResource(R.mipmap.ic_hair_band_yes);
            return;
        }
        ivHairBand.setImageResource(R.mipmap.ic_hair_band_no);
    }

    private void changeSprayImage(){
        boolean spray = !appointment.getTools().isSpray();
        appointment.getTools().setSpray(spray);
        if (spray) {
            ivSpray.setImageResource(R.mipmap.ic_spray_yes);
            return;
        }
        ivSpray.setImageResource(R.mipmap.ic_spray_no);
    }

    private void  changeTubeImage(){
        boolean tube = !appointment.getTools().isTube();
        appointment.getTools().setTube(tube);
        if (tube) {
            ivTube.setImageResource(R.mipmap.ic_tube_yes);
            return;
        }
        ivTube.setImageResource(R.mipmap.ic_tube_no);
    }

    private void  changeTrimmerImage(){
        boolean trimmer = !appointment.getTools().isTrimmer();
        appointment.getTools().setTrimmer(trimmer);
        if (trimmer) {
            ivTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
            return;
        }
        ivTrimmer.setImageResource(R.mipmap.ic_trimmer_no);
    }
}

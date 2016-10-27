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
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TimePicker;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.Service;
import com.semeniuc.dmitrii.clientmanager.model.Tools;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ServiceRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ToolsRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends AppCompatActivity {

    public static final int DATE_PICKER_DIALOG_ID = 1;
    public static final int TIME_PICKER_DIALOG_ID = 2;

    protected Utils utils;
    private Appointment appointment;
    protected String clientName;
    protected String clientPhone;
    protected Service service;
    protected Tools tools;
    protected String info;
    protected String date;
    protected String time;
    protected Date dateTime;
    private String sum;
    private boolean paid;
    private boolean done;

    @BindView(R.id.appointment_client_name)
    AppCompatEditText etClientName;
    @BindView(R.id.appointment_client_phone)
    AppCompatEditText etClientPhone;
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
    @BindView(R.id.appointment_tools_cut_set_icon)
    AppCompatImageView ivSutSet;
    @BindView(R.id.appointment_tools_hair_band_icon)
    AppCompatImageView ivHairBand;
    @BindView(R.id.appointment_tools_spray_icon)
    AppCompatImageView ivSpray;
    @BindView(R.id.appointment_tools_tube_icon)
    AppCompatImageView ivTube;
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
    void onTubeIconClicked() {
        changeTubeImage();
    }

    @OnClick(R.id.appointment_tools_trimmer_icon)
    void onTrimmerIconClicked() {
        changeTrimmerImage();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        ButterKnife.bind(this);
        utils = new Utils();
        service = new Service();
        tools = new Tools();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_appointment:
                boolean formValid = isAppointmentFormValid();
                if (formValid) {
                    setDataFromFields();
                    new SaveAppointment().execute();
                    return true;
                }
                hideKeyboard();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean isAppointmentFormValid() {
        boolean valid = true;
        boolean empty = utils.isEditTextEmpty(etClientName);
        if (empty) {
            etClientName.setError(getResources().getString(R.string.field_is_required));
            valid = false;
        }
        empty = utils.isEditTextEmpty(etClientPhone);
        if (empty) {
            etClientPhone.setError(getResources().getString(R.string.field_is_required));
            valid = false;
        }
        empty = utils.isEditTextEmpty(etService);
        if (empty) {
            etService.setError(getResources().getString(R.string.field_is_required));
            valid = false;
        }
        empty = utils.isTextViewEmpty(tvDate);
        if (empty) {
            tvDate.setError(getResources().getString(R.string.field_is_required));
            valid = false;
        }
        empty = utils.isTextViewEmpty(tvTime);
        if (empty) {
            tvTime.setError(getResources().getString(R.string.field_is_required));
            valid = false;
        }
        return valid;
    }

    private void setDataFromFields() {
        clientName = etClientName.getText().toString();
        clientPhone = etClientPhone.getText().toString();
        service.setName(etService.getText().toString());
        sum = etSum.getText().toString();
        info = etInfo.getText().toString();
        date = tvDate.getText().toString();
        time = tvTime.getText().toString();
        String dateTimeStr = date + " " + time;

        dateTime = utils.convertStringToDate(dateTimeStr, Constants.DATE_TIME_FORMAT, this);
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

    /**
     * Open date picker dialog with current date
     */
    private DatePickerDialog getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
                this, datePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Set chosen Date to date edit text
     */
    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String formattedDate = utils.getCorrectDateFormat(year, month, day);
            tvDate.setText(formattedDate);
            tvDate.setError(null);
        }
    };

    /**
     * Open date picker dialog with current time
     */
    private TimePickerDialog getTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
    }

    /**
     * Set chosen Time to time edit text
     */
    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String formattedTime = utils.getCorrectTimeFormat(hourOfDay, minute);
            tvTime.setText(formattedTime);
            tvTime.setError(null);
        }
    };

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    /**
     * Save Appointment to DB (Create new one)
     */
    private class SaveAppointment extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {

            ServiceRepository serviceRepo = new ServiceRepository(getApplicationContext());
            serviceRepo.create(service);
            ToolsRepository toolsRepo = new ToolsRepository(getApplicationContext());
            toolsRepo.create(tools);
            appointment = new Appointment(MyApplication.getInstance().getUser(), clientName,
                    clientPhone, service, tools, info, dateTime, sum, paid, done);
            AppointmentRepository appointmentRepo = new AppointmentRepository(getApplicationContext());
            int created = appointmentRepo.create(appointment);
            return created;
        }

        @Override
        protected void onPostExecute(Integer created) {
            utils.showSaveResultMessage(created, AppointmentActivity.this);
            if (created == Constants.CREATED) finish();
            super.onPostExecute(created);
        }
    }

    // ********** Methods of onClick Image changing
    private void changePaidImage() {
        paid = !paid;
        if (paid) {
            ivPaid.setImageResource(R.mipmap.ic_money_paid_yes);
            return;
        }
        ivPaid.setImageResource(R.mipmap.ic_money_paid_no);
    }

    private void changeDoneImage() {
        done = !done;
        if (!done) {
            ivDone.setImageResource(R.mipmap.ic_ok_yes);
            return;
        }
        ivDone.setImageResource(R.mipmap.ic_ok_no);
    }

    private void changeHairColoringImage() {
        boolean hairColoring = !service.isHairColoring();
        service.setHairColoring(hairColoring);
        if (hairColoring) {
            ivHairColoring.setImageResource(R.mipmap.ic_paint_yes);
            return;
        }
        ivHairColoring.setImageResource(R.mipmap.ic_paint_no);
    }

    private void changeHairdoImage() {
        boolean hairdo = !service.isHairdo();
        service.setHairdo(hairdo);
        if (hairdo) {
            ivHairdo.setImageResource(R.mipmap.ic_womans_hair_yes);
            return;
        }
        ivHairdo.setImageResource(R.mipmap.ic_womans_hair_no);
    }

    private void changeHaircutImage() {
        boolean haircut = !service.isHaircut();
        service.setHaircut(haircut);
        if (haircut) {
            ivHaircut.setImageResource(R.mipmap.ic_scissors_yes);
            return;
        }
        ivHaircut.setImageResource(R.mipmap.ic_scissors_no);
    }

    private void changeBrushImage() {
        boolean brush = !tools.isBrush();
        tools.setBrush(brush);
        if (brush) {
            ivBrush.setImageResource(R.mipmap.ic_brush_yes);
            return;
        }
        ivBrush.setImageResource(R.mipmap.ic_brush_no);
    }

    private void changeHairBrushImage() {
        boolean hairBrush = !tools.isHairBrush();
        tools.setHairBrush(hairBrush);
        if (hairBrush) {
            ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_yes);
            return;
        }
        ivHairBrush.setImageResource(R.mipmap.ic_hair_brush_no);
    }

    private void changeHairDryerImage() {
        boolean hairDryer = !tools.isHairDryer();
        tools.setHairDryer(hairDryer);
        if (hairDryer) {
            ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_yes);
            return;
        }
        ivHairDryer.setImageResource(R.mipmap.ic_hair_dryer_no);
    }

    private void changeOxyImage() {
        boolean oxy = !tools.isOxy();
        tools.setOxy(oxy);
        if (oxy) {
            ivOxy.setImageResource(R.mipmap.ic_soap_yes);
            return;
        }
        ivOxy.setImageResource(R.mipmap.ic_soap_no);
    }

    private void changeCutSetImage() {
        boolean cutSet = !tools.isCutSet();
        tools.setCutSet(cutSet);
        if (cutSet) {
            ivSutSet.setImageResource(R.mipmap.ic_cutset_yes);
            return;
        }
        ivSutSet.setImageResource(R.mipmap.ic_cutset_no);
    }

    private void changeHairBandImage() {
        boolean hairBand = !tools.isHairBand();
        tools.setHairBand(hairBand);
        if (hairBand) {
            ivHairBand.setImageResource(R.mipmap.ic_hair_band_yes);
            return;
        }
        ivHairBand.setImageResource(R.mipmap.ic_hair_band_no);
    }

    private void changeSprayImage() {
        boolean spray = !tools.isSpray();
        tools.setSpray(spray);
        if (spray) {
            ivSpray.setImageResource(R.mipmap.ic_spray_yes);
            return;
        }
        ivSpray.setImageResource(R.mipmap.ic_spray_no);
    }

    private void changeTubeImage() {
        boolean tube = !tools.isTube();
        tools.setTube(tube);
        if (tube) {
            ivTube.setImageResource(R.mipmap.ic_tube_yes);
            return;
        }
        ivTube.setImageResource(R.mipmap.ic_tube_no);
    }

    private void changeTrimmerImage() {
        boolean trimmer = !tools.isTrimmer();
        tools.setTrimmer(trimmer);
        if (trimmer) {
            ivTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
            return;
        }
        ivTrimmer.setImageResource(R.mipmap.ic_trimmer_no);
    }
}

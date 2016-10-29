package com.semeniuc.dmitrii.clientmanager.activity;

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
import android.widget.ScrollView;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.fragment.DateDialogFragment;
import com.semeniuc.dmitrii.clientmanager.fragment.TimeDialogFragment;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.Service;
import com.semeniuc.dmitrii.clientmanager.model.Tools;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends AppCompatActivity implements OnTaskCompleted {

    protected Utils utils;
    private DatabaseTaskHelper dbHelper;
    private Appointment appointment;
    protected String clientName, clientPhone, info, date, time, sum;
    protected Service service;
    protected Tools tools;
    protected Date dateTime;
    private boolean paid, done;

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
        hideKeyboard();
        showDatePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_calendar_date)
    void onCalendarDateClicked() {
        hideKeyboard();
        showDatePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_time_icon)
    void onClockIconClicked() {
        hideKeyboard();
        showTimePickerDialog(Calendar.getInstance());
    }

    @OnClick(R.id.appointment_time)
    void onClockClicked() {
        hideKeyboard();
        showTimePickerDialog(Calendar.getInstance());
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
        dbHelper = new DatabaseTaskHelper();
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
                    appointment = new Appointment(MyApplication.getInstance().getUser(), clientName,
                            clientPhone, service, tools, info, dateTime, sum, paid, done);
                    new SaveAppointment(this).execute(appointment);
                    return true;
                }
                hideKeyboard();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Open time picker
     */
    protected void showDatePickerDialog(Calendar calendar) {
        DateDialogFragment ddf = DateDialogFragment.newInstance(this, calendar);
        ddf.setDateDialogFragmentListener(new DateDialogFragment.DateDialogFragmentListener() {

            @Override
            public void onDateDialogFragmentDateSet(Calendar date) {
                String formattedDate = utils.getCorrectDateFormat(
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));
                tvDate.setText(formattedDate);
                tvDate.setError(null);
            }
        });
        ddf.show(getSupportFragmentManager(), "date picker dialog fragment");
    }

    /**
     * Open time picker dialog
     */
    protected void showTimePickerDialog(Calendar calendar) {
        TimeDialogFragment tdf = TimeDialogFragment.newInstance(this, calendar);
        tdf.setTimeDialogFragmentListener(new TimeDialogFragment.TimeDialogFragmentListener() {

            @Override
            public void onTimeDialogFragmentTimeSet(Calendar time) {
                String formattedTime = utils.getCorrectTimeFormat(
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE));
                tvTime.setText(formattedTime);
                tvTime.setError(null);
            }
        });
        tdf.show(getSupportFragmentManager(), "time picker dialog fragment");
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

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Save Appointment to DB (Create new one)
     */
    private class SaveAppointment extends AsyncTask<Appointment, Void, Integer> {

        private OnTaskCompleted listener;

        public SaveAppointment(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(Appointment... array) {
            return dbHelper.saveAppointment(array[0]);
        }

        @Override
        protected void onPostExecute(Integer created) {
            super.onPostExecute(created);
            if (created == Constants.CREATED) {
                String message = getResources().getString(R.string.appointment_saved);
                listener.showMessage(message);
                finish();
            } else {
                String message = getResources().getString(R.string.saving_failed);
                listener.showMessage(message);
            }
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
        if (done) {
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

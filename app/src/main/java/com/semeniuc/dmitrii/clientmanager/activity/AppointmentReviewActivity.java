package com.semeniuc.dmitrii.clientmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointmentReviewActivity extends AppointmentCreateActivity implements OnTaskCompleted {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getIntent().getExtras().getParcelable(Constants.APPOINTMENT_PATH);
        listener = this;
        dbHelper = new DatabaseTaskHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateAppointmentFields();
        setImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_review_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_appointment:
                if (super.isAppointmentFormValid()) {
                    setDataFromFields();
                    updateAppointment();
                } else Utils.hideKeyboard(mainLayout, this);
                break;
            case R.id.action_delete_appointment:
                deleteAppointment();
                break;
        }
        return true;
    }

    /**
     * Fill appointment form fields with incoming data
     */
    public void populateAppointmentFields() {
        editTextClientName.setText(appointment.getClient().getName());
        editTextClientPhone.setText(appointment.getClient().getContact().getPhone());
        editTextAddress.setText(appointment.getClient().getContact().getAddress());
        editTextService.setText(appointment.getService().getName());
        editTextSum.setText(appointment.getSum());
        editTextInfo.setText(appointment.getInfo());
        textViewDate.setText(Utils.convertDateToString(
                appointment.getDate(), Constants.DATE_FORMAT, this));
        textViewTime.setText(Utils.convertDateToString(
                appointment.getDate(), Constants.TIME_FORMAT, this));
    }

    private void setImages() {
        if (appointment.getService().isHairColoring())
            imageViewHairColoring.setImageResource(R.mipmap.ic_paint_yes);
        if (appointment.getService().isHairdo())
            imageViewHairdo.setImageResource(R.mipmap.ic_womans_hair_yes);
        if (appointment.getService().isHaircut())
            imageViewHaircut.setImageResource(R.mipmap.ic_scissors_yes);
        if (appointment.isPaid()) imageViewPaid.setImageResource(R.mipmap.ic_money_paid_yes);
        if (appointment.isDone()) imageViewDone.setImageResource(R.mipmap.ic_ok_yes);
        if (appointment.getTools().isBrush())
            imageViewBrush.setImageResource(R.mipmap.ic_brush_yes);
        if (appointment.getTools().isHairBrush())
            imageViewHairBrush.setImageResource(R.mipmap.ic_hair_brush_yes);
        if (appointment.getTools().isHairDryer())
            imageViewHairDryer.setImageResource(R.mipmap.ic_hair_dryer_yes);
        if (appointment.getTools().isHairBand())
            imageViewHairBand.setImageResource(R.mipmap.ic_hair_band_yes);
        if (appointment.getTools().isCutSet())
            imageViewCutSet.setImageResource(R.mipmap.ic_cutset_yes);
        if (appointment.getTools().isSpray())
            imageViewSpray.setImageResource(R.mipmap.ic_spray_yes);
        if (appointment.getTools().isOxy()) imageViewOxy.setImageResource(R.mipmap.ic_soap_yes);
        if (appointment.getTools().isTube()) imageViewTube.setImageResource(R.mipmap.ic_tube_yes);
        if (appointment.getTools().isTrimmer())
            imageViewTrimmer.setImageResource(R.mipmap.ic_trimmer_yes);
    }

    /**
     * Update Appointment in DB using RXJava
     */
    private void updateAppointment() {

        updateObservable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.UPDATED) {
                    listener.showMessage(getResources().getString(R.string.appointment_updated));
                    finish();
                } else listener.showMessage(getResources().getString(R.string.updating_failed));
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

    final Observable<Integer> updateObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(dbHelper.updateAppointment(appointment));
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    /**
     * Delete Appointment from DB using RXJava
     */
    private void deleteAppointment() {

        deleteObservable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.DELETED) {
                    listener.showMessage(getResources().getString(R.string.appointment_deleted));
                    finish();
                } else listener.showMessage(getResources().getString(R.string.deleting_failed));
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

    final Observable<Integer> deleteObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            Integer result = dbHelper.deleteAppointment(appointment);
            subscriber.onNext(result);
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
}

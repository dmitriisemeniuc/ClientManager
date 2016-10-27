package com.semeniuc.dmitrii.clientmanager;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;

public class MyApplication extends Application {

    private static MyApplication myApp;
    private static AppointmentRepository appointmentRepo;
    private GoogleApiClient googleApiClient;
    private User user;



    public static MyApplication getInstance() {
        return myApp;
    }

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;

    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

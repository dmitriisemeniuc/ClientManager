package com.semeniuc.dmitrii.clientmanager;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.semeniuc.dmitrii.clientmanager.model.User;

public class MyApplication extends Application {

    private GoogleApiClient googleApiClient;
    private User user;

    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
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

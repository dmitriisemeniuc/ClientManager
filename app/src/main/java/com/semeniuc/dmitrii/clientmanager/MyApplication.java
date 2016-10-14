package com.semeniuc.dmitrii.clientmanager;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.semeniuc.dmitrii.clientmanager.model.User;

public class MyApplication extends Application {

    private GoogleApiClient mGoogleApiClient;
    private User mUser;

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
        return mGoogleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}

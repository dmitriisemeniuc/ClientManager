package com.semeniuc.dmitrii.clientmanager;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

public class MyApplication extends Application {

    private GoogleApiClient mGoogleApiClient;

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
}

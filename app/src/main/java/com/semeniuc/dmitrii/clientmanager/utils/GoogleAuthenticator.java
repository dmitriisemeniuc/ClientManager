package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.semeniuc.dmitrii.clientmanager.MyApplication;

public class GoogleAuthenticator implements GoogleApiClient.OnConnectionFailedListener {

    public static final String LOG_TAG = GoogleAuthenticator.class.getSimpleName();

    private GoogleSignInOptions mGso;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google sign in is unavailable");
    }

    public void createGoogleSignInOptions() {
        // Configure sign-in to request the user profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    public void setGoogleApiClient(Context context, FragmentActivity fragmentActivity) {
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();
        // Set googleAPiClient object visible for the app
        MyApplication.getInstance().setGoogleApiClient(googleApiClient);
    }

    public OptionalPendingResult<GoogleSignInResult> getOptionalPendingResult() {
        return Auth.GoogleSignInApi.silentSignIn(MyApplication.getInstance().getGoogleApiClient());
    }
}

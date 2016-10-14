package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int LAYOUT = R.layout.activity_signin;
    public static final String LOG_TAG = SignInActivity.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    public static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    private GoogleAuthenticator mGoogleAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(LOG_TAG, "onCreate()");
        setContentView(LAYOUT);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mGoogleAuthenticator = new GoogleAuthenticator();
        mGoogleAuthenticator.createGoogleSignInOptions();
        mGoogleAuthenticator.setGoogleApiClient(this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) Log.i(LOG_TAG, "onStart()");
        silentSignIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DEBUG) Log.i(LOG_TAG, "onActivityResult()");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void silentSignIn() {
        if (DEBUG) Log.i(LOG_TAG, "silentSignIn()");
        OptionalPendingResult<GoogleSignInResult> opr = mGoogleAuthenticator.getOptionalPendingResult();
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            if (DEBUG) Log.i(LOG_TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.
            // Cross-device single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (DEBUG) Log.i(LOG_TAG, "handleSignInResult()");
        if (result.isSuccess()) {
            if (DEBUG) Log.i(LOG_TAG, "Sign in is success!");
            // Store user id, user name and email.
            // TODO: Set user details
            // Show authenticated UI
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(MyApplication.getInstance().getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void signOut() {
        Auth.GoogleSignInApi.signOut(MyApplication.getInstance().getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    protected void updateUI(boolean update) {
        if (DEBUG) Log.i(LOG_TAG, "updateUI()");
        if (update) {
            if (DEBUG) Log.i(LOG_TAG, "signedIn==true");
            startMainActivity();
            finish();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
        if (DEBUG) Log.i(LOG_TAG, "show Dialog");
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            if (DEBUG) Log.i(LOG_TAG, "hide Dialog");
        }
    }
}

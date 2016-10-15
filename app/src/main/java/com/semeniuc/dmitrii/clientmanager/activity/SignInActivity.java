package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_signin;
    public static final String LOG_TAG = SignInActivity.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    public static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    private GoogleAuthenticator mGoogleAuthenticator;

    @OnClick(R.id.sign_in_button) void submitSignIn() {
        signIn();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(LOG_TAG, "onCreate()");
        setContentView(LAYOUT);

        ButterKnife.bind(this);

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

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(
                MyApplication.getInstance().getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
    * Checking if the user is signed in previously
    * */
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
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    /**
     * Handling of sign in result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (DEBUG) Log.i(LOG_TAG, "handleSignInResult()");
        if (result.isSuccess()) {
            if (DEBUG) Log.i(LOG_TAG, "Sign in is success!");
            // Store user id, user name and email to the global User object
            setUserDetails(result);
            // Show authenticated UI
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /*
    * Setting of signed user to global User object
    * */
    private void setUserDetails(@NonNull GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        if(null != account){
            mGoogleAuthenticator.setUserDetails(account);
            if (DEBUG)
                Toast.makeText(this, "Signed in as: " + MyApplication.getInstance().getUser().getName(),
                        Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Sign out from google account
    * */
    protected void signOut() {
        Auth.GoogleSignInApi.signOut(
                MyApplication.getInstance().getGoogleApiClient())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(false);
                    }
                });
    }

    /*
    * Updating of UI. If true => goes to MainActivity
    * */
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

    /*
    * Showing of dialog during of sign in process
    * */
    private void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
        if (DEBUG) Log.i(LOG_TAG, "show Dialog");
    }

    /*
    * Hiding of dialog after the sign in process is done
    * */
    private void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            if (DEBUG) Log.i(LOG_TAG, "hide Dialog");
        }
    }
}

package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseSignInActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_signin;
    public static final String LOG_TAG = BaseSignInActivity.class.getSimpleName();
    public static final String LOGIN_PREFS = "loginPrefs";
    public static final boolean DEBUG = Constants.DEBUG;
    public static final int RC_SIGN_IN = 9001;
    public static String USER_SAVING_MSG = "";
    public static String USER_SAVING_ERROR = "";

    private ProgressDialog mProgressDialog;
    private GoogleAuthenticator mGoogleAuthenticator;
    private Context mCtx = MyApplication.getInstance().getApplicationContext();

    @OnClick(R.id.sign_in_with_google_button)
    void submitSignIn() {
        signInWithGoogle();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);
        checkUserSignInType();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            newSignInWithGoogle(result);
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(
                MyApplication.getInstance().getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Identify what type of user should be used
     * {It can be: google user, facebook user or user registered with e-mail}
     * */
    private void checkUserSignInType() {
        SharedPreferences settings = mCtx.getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        String user = settings.getString(Constants.USER, "");
        if(user.equals(Constants.USER_GOOGLE)){
            mGoogleAuthenticator = new GoogleAuthenticator();
            mGoogleAuthenticator.createGoogleSignInOptions();
            mGoogleAuthenticator.setGoogleApiClient(this, this);
            silentSignInWithGoogle();
        }
    }

    /*
    * Checking if the user is signed in previously
    * */
    private void silentSignInWithGoogle() {
        OptionalPendingResult<GoogleSignInResult> opr = mGoogleAuthenticator.getOptionalPendingResult();
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleGoogleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.
            // Cross-device single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleGoogleSignInResult(googleSignInResult);
                }
            });
        }
    }

    /**
     * Handling of sign in result
     */
    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            User user = getUserByEmail(account.getEmail());
            MyApplication.getInstance().setUser(user);
            // Show authenticated UI
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void newSignInWithGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Store user id, user name and email to the global User object
            setGoogleUserDetails(result);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /*
    * Setting of signed user to global User object
    * */
    private void setGoogleUserDetails(@NonNull GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        if (null != account) {
          User user = mGoogleAuthenticator.setUserDetails(account);
            // Save Global user to DB
            new SaveGoogleUser().execute(user);
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
        if (update) {
            startMainActivity();
            finish();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(BaseSignInActivity.this, MainActivity.class);
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
    }

    /*
    * Hiding of dialog after the sign in process is done
    * */
    private void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private User getUserByEmail(String email){
        UserRepository userRepo = new UserRepository(mCtx);
        List<User> users = userRepo.findByEmail(email);
        if(users != null){
            return users.get(0);
        } else {
            return null;
        }
    }

    private class SaveGoogleUser extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... array) {
            User user = array[0];
            UserRepository userRepo = new UserRepository(mCtx);
            // Check if user with this e-mail already exists.
            // It makes sense if there are another authentication options apart from google
            // sign in, like facebook login or login with user e-mail.
            // If the app will use only google sign in option, this check can be removed
            List<User> users = userRepo.findByEmail(user.getEmail());
            if (null != users) {
                if (users.size() == 0) {
                    int index = userRepo.create(user);
                    if (index == 1) {
                        users = userRepo.findByEmail(user.getEmail());
                        user = users.get(0);
                        // Set global user
                        MyApplication.getInstance().setUser(user);
                        USER_SAVING_MSG = "Signed in as: " + user.getName();
                        USER_SAVING_ERROR = "";
                    } else {
                        USER_SAVING_ERROR = getResources().getString(R.string.user_saving_failed);
                    }
                } else {
                    user = users.get(0);
                    MyApplication.getInstance().setUser(user);
                    USER_SAVING_ERROR = "";
                }
            }
            return USER_SAVING_MSG;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            if (!USER_SAVING_ERROR.isEmpty()) {
                Toast.makeText(mCtx, USER_SAVING_ERROR, Toast.LENGTH_SHORT).show();
                return;
            }
            // Show authenticated UI
            updateUI(true);
            SharedPreferences settings = mCtx.getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.USER, Constants.USER_GOOGLE);
            // Commit the edits!
            editor.commit();
            Toast.makeText(mCtx, "Signed in as: " + MyApplication.getInstance().getUser().getName(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

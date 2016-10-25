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
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_signin;
    public static final String LOG_TAG = SignInActivity.class.getSimpleName();
    public static String USER_SAVING_MSG = Constants.EMPTY;
    public static String USER_SAVING_ERROR = Constants.EMPTY;

    private ProgressDialog mProgressDialog;
    private GoogleAuthenticator mGoogleAuthenticator;
    private Context mCtx = MyApplication.getInstance().getApplicationContext();
    private Utils mUtils;
    private String mFieldRequired;

    @BindView(R.id.sign_in_email_et)
    AppCompatEditText email;
    @BindView(R.id.sign_in_password_et)
    AppCompatEditText password;
    @BindView(R.id.main_sign_in_layout)
    RelativeLayout mainLayout;

    @OnClick(R.id.sign_in_with_google_button)
    void submitGoogleSignIn() {
        signInWithGoogle();
    }

    @OnClick(R.id.sign_in_btn)
    void submitSignIn() {
        signIn();
    }

    @OnClick(R.id.sign_up_link)
    void submitSignUp() {
        goToSignUpActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);
        checkUserSignInType();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            newSignInWithGoogle(result);
        }
    }

    /**
     * Sign in with Google account
     */
    private void signInWithGoogle() {
        GoogleApiClient apiClient = MyApplication.getInstance().getGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    /**
     * Sign in with email
     */
    private void signIn() {
        boolean formValid = validateForm();
        if (!formValid) return;
        boolean userRegistered = isEmailAndPasswordRegistered();
        if (userRegistered) {
            doLogin();
            return;
        }
        hideKeyboard();
        Toast.makeText(this, getResources().getString(R.string.invalid_credentials),
                Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        boolean empty = isSignInFieldsEmpty();
        return !empty; // if empty true => return false (form not valid)
    }

    private boolean isSignInFieldsEmpty() {
        mFieldRequired = this.getResources().getString(R.string.field_is_required);
        // email validation
        if (mUtils.isEditTextEmpty(email)) {
            email.setError(mFieldRequired);
            return true;
        }
        // password validation
        if (mUtils.isEditTextEmpty(password)) {
            password.setError(mFieldRequired);
            return true;
        }
        return false;
    }

    private boolean isEmailAndPasswordRegistered() {
        UserRepository userRepo = new UserRepository(mCtx);
        List<User> users = userRepo.findByEmailAndPassword(
                email.getText().toString(), password.getText().toString());
        if (users != null) {
            if (users.size() > 0) {
                return true; // User registered
            }
        }
        return false;
    }

    private void doLogin() {
        User user = new User(email.getText().toString(), password.getText().toString());
        MyApplication.getInstance().setUser(user);
        mUtils.setUserInPrefs(Constants.REGISTERED_USER);
        updateUI(true);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    /**
     * Go to Sign Up Activity to sign up with email
     */
    private void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Identify what type of user should be used
     * {It can be: user signed in with google or registered with e-mail}
     */
    private void checkUserSignInType() {
        mUtils = new Utils(this);
        String userType = mUtils.getUserFromPrefs();
        if (userType.equals(Constants.GOOGLE_USER)) {
            // USER REGISTERED WITH GOOGLE ACCOUNT
            initGoogleAuthenticator();
            SharedPreferences settings = mCtx.getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            // If previous google sign is cached
            // (user does not sign out) - google silent sign in will be made
            if (loggedIn) silentSignInWithGoogle();
            return;
        } // USER REGISTERED WITH EMAIL
        if (userType.equals(Constants.REGISTERED_USER)) {

            SharedPreferences settings = mCtx.getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            if (loggedIn) {
                String email = settings.getString(Constants.EMAIL, Constants.EMPTY);
                if (!email.isEmpty()) {
                    new SetGlobalUser().execute(email);
                }
            }
            return;
        } // NEW USER
        if (userType.equals(Constants.NEW_USER)) {
            // GoogleAuthenticator should be instantiated only once for this Activity,
            // so it's called from onCreate method
            initGoogleAuthenticator();
            return;
        }
        Log.e(LOG_TAG, "Unknown user type");
        Log.e(LOG_TAG, "UserType == " + userType);
    }

    /**
     * Instantiates Google authenticator object
     */
    private void initGoogleAuthenticator() {
        mGoogleAuthenticator = new GoogleAuthenticator();
        mGoogleAuthenticator.createGoogleSignInOptions();
        mGoogleAuthenticator.setGoogleApiClient(this, this);
    }

    /**
     * Checking if the user is signed in with Google account previously
     */
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
     * Handle Sign In result.
     * If result success -> go to Sign In Activity and finish this
     */
    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (!result.isSuccess()) return;
        GoogleSignInAccount account = result.getSignInAccount();
        if (account == null) return;
        User user = getUserByEmail(account.getEmail());
        MyApplication.getInstance().setUser(user);
        // Show authenticated UI
        updateUI(true);
    }

    /**
     * Makes new sign in with Google account and stores user details
     */
    private void newSignInWithGoogle(GoogleSignInResult result) {
        if (!result.isSuccess()) return;
        // Store user id, user name and email to the global User object
        setGoogleUserDetails(result);
    }

    /**
     * Sets the signed user to global User object
     */
    private void setGoogleUserDetails(@NonNull GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        if (null != account) {
            User user = new User(account);
            // Save Global user to DB
            new SaveGoogleUser().execute(user);
        }
    }

    /**
     * Sign out from Google account
     */
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

    /**
     * Updates UI.
     * If true => goes to MainActivity and finish this
     */
    protected void updateUI(boolean update) {
        if (!update) return;
        startMainActivity();
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Showing of dialog during of sign in process
     */
    private void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Hiding of dialog after the sign in process is done
     */
    private void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private User getUserByEmail(String email) {
        UserRepository userRepo = new UserRepository(mCtx);
        List<User> users = userRepo.findByEmail(email);
        if (users != null) {
            return users.get(0);
        }
        return null;
    }

    private class SaveGoogleUser extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... array) {
            User user = array[0];
            UserRepository userRepo = new UserRepository(mCtx);
            List<User> users = userRepo.findByEmail(user.getEmail());
            if (null != users) {
                if (users.size() == Constants.SIZE_EMPTY) {
                    int index = userRepo.create(user);
                    if (index == 1) {
                        users = userRepo.findByEmail(user.getEmail());
                        user = users.get(0);
                        // Set global user
                        MyApplication.getInstance().setUser(user);
                        USER_SAVING_MSG = getResources().getString(R.string.signed_in_as)
                                + ": " + user.getEmail();
                        USER_SAVING_ERROR = Constants.EMPTY;
                    } else {
                        USER_SAVING_ERROR = getResources().getString(R.string.user_saving_failed);
                    }
                } else {
                    user = users.get(0);
                    MyApplication.getInstance().setUser(user);
                    USER_SAVING_MSG = getResources().getString(R.string.signed_in_as)
                            + ": " + user.getEmail();
                    USER_SAVING_ERROR = Constants.EMPTY;
                }
                return USER_SAVING_MSG;
            }
            USER_SAVING_ERROR = getResources().getString(R.string.user_equal_null);
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
            mUtils.setUserInPrefs(Constants.GOOGLE_USER);
            Toast.makeText(mCtx, USER_SAVING_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Set global User for silent sign in
    * */
    private class SetGlobalUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... array) {
            String email = array[0];
            UserRepository userRepo = new UserRepository(mCtx);
            List<User> users = userRepo.findByEmail(email);
            if (null != users) {
                if (users.size() > 0) {
                    User user = users.get(0);
                    // Set global user
                    MyApplication.getInstance().setUser(user);
                    USER_SAVING_MSG = getResources().getString(R.string.signed_in_as)
                            + ": " + user.getEmail();
                    USER_SAVING_ERROR = Constants.EMPTY;
                } else {
                    USER_SAVING_ERROR = getResources().getString(R.string.user_login_failed);
                }
            }
            return USER_SAVING_ERROR;
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
            Toast.makeText(mCtx, USER_SAVING_MSG, Toast.LENGTH_SHORT).show();
        }
    }
}


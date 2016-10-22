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
    public static final boolean DEBUG = Constants.DEBUG;
    public static final String LOG_TAG = SignInActivity.class.getSimpleName();
    public static final String LOGIN_PREFS = "loginPrefs";
    public static final String USER = Constants.USER;
    public static final String NEW_USER = Constants.NEW_USER;
    public static final String GOOGLE_USER = Constants.GOOGLE_USER;
    public static final String REGISTERED_USER = Constants.REGISTERED_USER;
    public static final String LOGGED_IN = Constants.LOGGED_IN;
    public static final String EMAIL = Constants.EMAIL;
    public static final String EMPTY = Constants.EMPTY;
    public static final int FIRST = Constants.FIRST;
    public static final int SIZE_EMPTY = Constants.SIZE_EMPTY;
    public static final int RC_SIGN_IN = 9001;
    public static String USER_SAVING_MSG = EMPTY;
    public static String USER_SAVING_ERROR = EMPTY;

    private ProgressDialog mProgressDialog;
    private GoogleAuthenticator mGoogleAuthenticator;
    private Context mCtx = MyApplication.getInstance().getApplicationContext();
    private Utils mUtils = new Utils(SignInActivity.this);
    private String mFieldRequired;

    @BindView(R.id.sign_in_email_et)
    AppCompatEditText email;
    @BindView(R.id.sign_in_password_et)
    AppCompatEditText password;

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
        String user = checkUserSignInType();
        if (user.equals(NEW_USER)) {
            // GoogleAuthenticator should be instantiated only once for this Activity,
            // so it is called from onCreate method
            initGoogleAuthenticator();
        }
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
        GoogleApiClient apiClient = MyApplication.getInstance().getGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn() {
        boolean formValid = validateForm();
        if (!formValid) return;
        boolean userRegistered = isEmailAndPasswordRegistered();
        if (userRegistered) {
            doLogin();
        }
    }

    private boolean validateForm() {
        boolean empty = isSignInFieldsEmpty();
        if (empty) return false;
        // Form valid
        return true;
    }

    private boolean isEmailAndPasswordRegistered() {
        UserRepository userRepo = new UserRepository(mCtx);
        List<User> users = userRepo.findByEmailAndPassword(
                email.getText().toString(), password.getText().toString());
        if (users != null) {
            if (users.size() > 0) {
                // User registered
                return true;
            }

        }
        return false;
    }

    private void doLogin() {
        User user = new User(email.getText().toString(), password.getText().toString());
        MyApplication.getInstance().setUser(user);
        updateUI(true);
    }

    private boolean isSignInFieldsEmpty() {
        mFieldRequired = this.getResources().getString(R.string.field_is_required);
        boolean valid = false;
        // user name / email validation
        if (mUtils.isEditTextEmpty(email)) {
            email.setError(mFieldRequired);
            valid = true;
        }
        // password validation
        if (mUtils.isEditTextEmpty(password)) {
            password.setError(mFieldRequired);
            valid = true;
        }
        return valid;
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Identify what type of user should be used
     * {It can be: google user, facebook user or user registered with e-mail}
     */
    private String checkUserSignInType() {
        SharedPreferences settings = mCtx.getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        // setting.getString will return NEW_USER value in case if USER value won't be found
        String userType = settings.getString(USER, NEW_USER);
        if (userType.equals(GOOGLE_USER)) {
            initGoogleAuthenticator();
            // If previous google sign is cached
            // (google does not sign out) - silent sign in will be made
            boolean loggedIn = settings.getBoolean(LOGGED_IN, false);
            if (loggedIn) silentSignInWithGoogle();
        } else if (userType.equals(REGISTERED_USER)) {
            boolean loggedIn = settings.getBoolean(LOGGED_IN, false);
            if (loggedIn) {
                String email = settings.getString(EMAIL, EMPTY);
                if (!email.isEmpty()) {
                    new SetGlobalUser().execute(email);
                }
            }
        }
        return userType;
    }

    private void initGoogleAuthenticator() {
        mGoogleAuthenticator = new GoogleAuthenticator();
        mGoogleAuthenticator.createGoogleSignInOptions();
        mGoogleAuthenticator.setGoogleApiClient(this, this);
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
            User user = new User(account);
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
    }

    /*
    * Hiding of dialog after the sign in process is done
    * */
    private void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private User getUserByEmail(String email) {
        UserRepository userRepo = new UserRepository(mCtx);
        List<User> users = userRepo.findByEmail(email);
        if (users != null) {
            return users.get(FIRST);
        } else {
            return null;
        }
    }

    private class SaveGoogleUser extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... array) {
            User user = array[FIRST];
            UserRepository userRepo = new UserRepository(mCtx);
            // Check if user with this e-mail already exists.
            // It makes sense if there are another authentication options apart from google
            // sign in, like facebook login or login with user e-mail.
            // If google sign in option is used only, then this can be removed
            List<User> users = userRepo.findByEmail(user.getEmail());
            if (null != users) {
                if (users.size() == SIZE_EMPTY) {
                    int index = userRepo.create(user);
                    if (index == 1) {
                        users = userRepo.findByEmail(user.getEmail());
                        user = users.get(FIRST);
                        // Set global user
                        MyApplication.getInstance().setUser(user);
                        USER_SAVING_MSG = getResources().getString(R.string.SignedInAs)
                                + ": " + user.getEmail();
                        USER_SAVING_ERROR = EMPTY;
                    } else {
                        USER_SAVING_ERROR = getResources().getString(R.string.userSavingFailed);
                    }
                } else {
                    user = users.get(FIRST);
                    MyApplication.getInstance().setUser(user);
                    USER_SAVING_ERROR = EMPTY;
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
            editor.putString(USER, GOOGLE_USER);
            editor.putString(Constants.EMAIL, MyApplication.getInstance().getUser().getEmail());
            editor.putBoolean(Constants.LOGGED_IN, true);
            // Commit the edits!
            editor.commit();
            Toast.makeText(mCtx, USER_SAVING_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Set global User for silent sign in
    * */
    private class SetGlobalUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... array) {
            String email = array[FIRST];
            UserRepository userRepo = new UserRepository(mCtx);
            List<User> users = userRepo.findByEmail(email);
            if (null != users) {
                if (users.size() > 0) {
                    User user = users.get(FIRST);
                    // Set global user
                    MyApplication.getInstance().setUser(user);
                    USER_SAVING_MSG = getResources().getString(R.string.SignedInAs)
                            + ": " + user.getEmail();
                    USER_SAVING_ERROR = EMPTY;
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


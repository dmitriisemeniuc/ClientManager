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
import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements OnTaskCompleted {

    private ProgressDialog progressDialog;
    private GoogleAuthenticator googleAuthenticator;
    private Utils utils;
    private DatabaseTaskHelper dbHelper;

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
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);
        checkUserSignInType();
        dbHelper = new DatabaseTaskHelper();
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
        String fieldRequired = this.getResources().getString(R.string.field_is_required);
        // email validation
        if (utils.isEditTextEmpty(email)) {
            email.setError(fieldRequired);
            return true;
        }
        // password validation
        if (utils.isEditTextEmpty(password)) {
            password.setError(fieldRequired);
            return true;
        }
        return false;
    }

    private boolean isEmailAndPasswordRegistered() {
        UserRepository userRepo = UserRepository.getInstance();
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
        utils.setUserInPrefs(Constants.REGISTERED_USER, this);
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
        utils = new Utils();
        String userType = utils.getUserFromPrefs(this);
        if (userType.equals(Constants.GOOGLE_USER)) {
            // USER REGISTERED WITH GOOGLE ACCOUNT
            initGoogleAuthenticator();
            SharedPreferences settings = getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            // If previous google sign is cached
            // (user does not sign out) - google silent sign in will be made
            if (loggedIn) silentSignInWithGoogle();
            return;
        } // USER REGISTERED WITH EMAIL
        if (userType.equals(Constants.REGISTERED_USER)) {
            SharedPreferences settings = getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            if (loggedIn) {
                String email = settings.getString(Constants.EMAIL, Constants.EMPTY);
                if (!email.isEmpty()) {
                    new SetGlobalUser(this).execute(email);
                }
            }
            return;
        } // NEW USER
        if (userType.equals(Constants.NEW_USER)) {
            // GoogleAuthenticator should be instantiated only once for this Activity,
            // so it's called from onCreate method
            initGoogleAuthenticator();
        }
    }

    /**
     * Instantiates Google authenticator object
     */
    private void initGoogleAuthenticator() {
        googleAuthenticator = new GoogleAuthenticator();
        googleAuthenticator.createGoogleSignInOptions();
        googleAuthenticator.setGoogleApiClient(this, this);
    }

    /**
     * Checking if the user is signed in with Google account previously
     */
    private void silentSignInWithGoogle() {
        OptionalPendingResult<GoogleSignInResult> opr = googleAuthenticator.getOptionalPendingResult();
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
        if (result.isSuccess()) {
            // Store user id, user name and email to the global User object
            setGoogleUserDetails(result);
        } else {
            boolean connectedToNetwork = utils.isNetworkAvailable(this);
            if (!connectedToNetwork) {
                Toast.makeText(this, getResources().getString(R.string.no_internet_access),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getResources().getString(R.string.verify_internet),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets the signed user to global User object
     */
    private void setGoogleUserDetails(@NonNull GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        if (null != account) {
            User user = new User(account);
            // Save Global user to DB
            new SaveGoogleUser(this).execute(user);
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
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    /**
     * Hiding of dialog after the sign in process is done
     */
    private void hideProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private User getUserByEmail(String email) {
        UserRepository userRepo = UserRepository.getInstance();
        List<User> users = userRepo.findByEmail(email);
        if (users != null) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private class SaveGoogleUser extends AsyncTask<User, Void, String> {

        private OnTaskCompleted listener;

        public SaveGoogleUser(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(User... array) {
            return dbHelper.saveGoogleUser(array[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message;
            if (result.equals(Constants.USER_SAVED) || result.equals(Constants.USER_EXISTS)) {
                updateUI(true);
                utils.setUserInPrefs(Constants.GOOGLE_USER, SignInActivity.this);
                message = getResources().getString(R.string.signed_in_as) + ": "
                        + MyApplication.getInstance().getUser().getEmail();
                listener.showMessage(message);
                return;
            }
            if (result.equals(Constants.USER_NOT_SAVED) || result.equals(Constants.NO_DB_RESULT)) {
                message = getResources().getString(R.string.user_saving_failed);
                listener.showMessage(message);
            }
        }
    }

    /**
    * Set global User for silent sign in
    * */
    private class SetGlobalUser extends AsyncTask<String, Void, String> {

        private OnTaskCompleted listener;

        public SetGlobalUser(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... array) {
          return dbHelper.setGlobalUser(array[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals(Constants.USER_SAVED)) {
                updateUI(true);
                String message = getResources().getString(R.string.signed_in_as) + ": "
                        + MyApplication.getInstance().getUser().getEmail();
                listener.showMessage(message);
                return;
            }
            if (result.equals(Constants.USER_NOT_SAVED) || result.equals(Constants.NO_DB_RESULT)) {
                String message = getResources().getString(R.string.user_saving_failed);
                listener.showMessage(message);
            }
        }
    }
}


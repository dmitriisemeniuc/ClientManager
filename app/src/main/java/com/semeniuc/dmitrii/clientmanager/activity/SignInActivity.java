package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity implements OnTaskCompleted {

    public static final String LOG_TAG = SignInActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private GoogleAuthenticator googleAuthenticator;
    private Utils utils;
    private DatabaseTaskHelper dbHelper;
    private User user;
    private OnTaskCompleted listener;
    private String email;

    @BindView(R.id.sign_in_email_et)
    AppCompatEditText etEmail;
    @BindView(R.id.sign_in_password_et)
    AppCompatEditText etPassword;
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
        listener = this;
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

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        if (utils.isEditTextEmpty(etEmail)) {
            etEmail.setError(fieldRequired);
            return true;
        }
        // password validation
        if (utils.isEditTextEmpty(etPassword)) {
            etPassword.setError(fieldRequired);
            return true;
        }
        return false;
    }

    private boolean isEmailAndPasswordRegistered() {
        UserRepository userRepo = UserRepository.getInstance();
        List<User> users = userRepo.findByEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString());
        if (users != null) {
            if (users.size() > 0) {
                return true; // User registered
            }
        }
        return false;
    }

    private void doLogin() {
        user = new User(etEmail.getText().toString(), etPassword.getText().toString());
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
                email = settings.getString(Constants.EMAIL, Constants.EMPTY);
                if (!email.isEmpty()) {
                    setGlobalUser();
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
     * Set global user for silent sign in
     * */
    private void setGlobalUser() {
        setGlobalUserObservable.subscribe(new Subscriber() {

            @Override
            public void onNext(Object o) {
                Integer result = (Integer) o;
                if (result == Constants.USER_SAVED) {
                    String message = getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail();
                    listener.showMessage(message);
                    updateUI(true);
                }
                if (result == Constants.USER_NOT_SAVED || result == Constants.NO_DB_RESULT) {
                    String message = getResources().getString(R.string.user_saving_failed);
                    listener.showMessage(message);
                }
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "Error: " + e.getMessage());
            }
        });
    }

    // SET GLOBAL USER OBSERVABLE
    final Observable setGlobalUserObservable = Observable.create(new Observable.OnSubscribe() {
        @Override
        public void call(Object o) {
            Subscriber subscriber = (Subscriber) o;
            subscriber.onNext(dbHelper.setGlobalUser(email));
            subscriber.onCompleted();
        }
    })
            .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
            .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread

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
            opr.setResultCallback(googleSignInResult -> {
                hideProgressDialog();
                handleGoogleSignInResult(googleSignInResult);
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
        user = getUserByEmail(account.getEmail());
        Uri photoUrl = account.getPhotoUrl();
        if (null != user) {
            if (null != photoUrl) {
                user.setPhotoUrl(photoUrl.toString());
            }
            MyApplication.getInstance().setUser(user);
            // Show authenticated UI
            updateUI(true);
        }
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
            user = new User(account);
            saveGoogleUser();
        }
    }

    /**
     * Set Google user to Global user and save it to DB
     * */
    private void saveGoogleUser() {
        saveGoogleUserObservable.subscribe(new Subscriber() {

            @Override
            public void onNext(Object o) {
                Integer result = (Integer) o;
                if (result == Constants.USER_SAVED || result == Constants.USER_EXISTS) {
                    utils.setUserInPrefs(Constants.GOOGLE_USER, SignInActivity.this);
                    updateUI(true);
                    String message = getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail();
                    listener.showMessage(message);
                }
                if (result == Constants.USER_NOT_SAVED || result == Constants.NO_DB_RESULT) {
                    String message = getResources().getString(R.string.user_saving_failed);
                    listener.showMessage(message);
                }
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "Error: " + e.getMessage());
            }
        });
    }

    // SAVE GOOGLE USER OBSERVABLE
    final Observable saveGoogleUserObservable = Observable.create(new Observable.OnSubscribe() {
        @Override
        public void call(Object o) {
            Subscriber subscriber = (Subscriber) o;
            subscriber.onNext(dbHelper.saveGoogleUser(user));
            subscriber.onCompleted();
        }
    })
            .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
            .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread

    /**
     * Sign out from Google account
     */
    protected void signOut() {
        Auth.GoogleSignInApi.signOut(
                MyApplication.getInstance().getGoogleApiClient())
                .setResultCallback(status -> updateUI(false));
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
}

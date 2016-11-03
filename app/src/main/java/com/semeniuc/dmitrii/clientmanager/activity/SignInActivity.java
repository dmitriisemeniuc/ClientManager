package com.semeniuc.dmitrii.clientmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.GoogleAuthenticator;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends BaseActivity {

    private ProgressDialog progressDialog;
    private GoogleAuthenticator googleAuthenticator;
    private String email;

    @BindView(R.id.sign_in_email_et)
    AppCompatEditText editTextEmail;
    @BindView(R.id.sign_in_password_et)
    AppCompatEditText editTextPassword;
    @BindView(R.id.main_sign_in_layout)
    RelativeLayout mainLayout;

    @OnClick(R.id.sign_in_with_google_button)
    void submitGoogleSignIn() {
        signInWithGoogle();
    }

    @OnClick(R.id.sign_in_btn)
    void submitSignIn() {
        Utils.hideKeyboard(mainLayout, this);
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
        listener = this;
        dbHelper = new DatabaseTaskHelper();
        checkUserSignInType();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
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
        if (!isSignInFieldsValid()) return;
        User user = dbHelper.getUserByEmailAndPassword(
                editTextEmail.getText().toString(), editTextPassword.getText().toString());
        if (user != null) doLogin(user);
        else Toast.makeText(this, getResources().getString(R.string.invalid_credentials),
                Toast.LENGTH_SHORT).show();
    }

    private boolean isSignInFieldsValid() {
        boolean valid = true;
        if (!Utils.isValidEditText(editTextEmail, this)) valid = false;
        if (!Utils.isValidEditText(editTextPassword, this)) valid = false;
        return valid;
    }

    private void doLogin(User user) {
        MyApplication.getInstance().setUser(user);
        Utils.setUserInPrefs(Constants.REGISTERED_USER, this);
        updateUI(true);
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
        String userType = Utils.getUserFromPrefs(this);
        if (userType.equals(Constants.GOOGLE_USER)) {
            initGoogleAuthenticator();
            SharedPreferences settings = getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            if (loggedIn) silentSignInWithGoogle();
            return;
        }
        if (userType.equals(Constants.REGISTERED_USER)) {
            SharedPreferences settings = getSharedPreferences(Constants.LOGIN_PREFS, MODE_PRIVATE);
            boolean loggedIn = settings.getBoolean(Constants.LOGGED_IN, false);
            if (loggedIn) {
                email = settings.getString(Constants.EMAIL, Constants.EMPTY);
                if (!email.isEmpty()) setGlobalUser();
            }
            return;
        }
        if (userType.equals(Constants.NEW_USER)) initGoogleAuthenticator();
    }

    /**
     * Set global user for silent sign in
     */
    private void setGlobalUser() {
        setGlobalUserObservable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.USER_SAVED) {
                    listener.showMessage(getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail());
                    updateUI(true);
                }
                if (result == Constants.USER_NOT_SAVED || result == Constants.NO_DB_RESULT)
                    listener.showMessage(getResources().getString(R.string.user_saving_failed));
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.getMessage();
            }
        });
    }

    final Observable<Integer> setGlobalUserObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(dbHelper.setGlobalUser(email));
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

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
            GoogleSignInResult result = opr.get();
            handleGoogleSignInResult(result);
        } else {
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
        user = dbHelper.getUserByEmail(account.getEmail());
        Uri photoUrl = account.getPhotoUrl();
        if (null != user) {
            if (null != photoUrl) user.setPhotoUrl(photoUrl.toString());

            MyApplication.getInstance().setUser(user);
            updateUI(true);
        }
    }

    /**
     * Makes new sign in with Google account and stores user details
     */
    private void newSignInWithGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) setGoogleUserDetails(result);
        else if (Utils.isNetworkAvailable(this))
            Toast.makeText(this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the signed user to global User object
     */
    private void setGoogleUserDetails(@NonNull GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        if (null == account) return;
        user = new User(account);
        saveGoogleUser();
    }

    /**
     * Set Google user to Global user and save it to DB
     */
    private void saveGoogleUser() {
        saveGoogleUserObservable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.USER_SAVED || result == Constants.USER_EXISTS) {
                    Utils.setUserInPrefs(Constants.GOOGLE_USER, SignInActivity.this);
                    updateUI(true);
                    listener.showMessage(getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail());
                }
                if (result == Constants.USER_NOT_SAVED || result == Constants.NO_DB_RESULT)
                    listener.showMessage(getResources().getString(R.string.user_saving_failed));
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.getMessage();
            }
        });
    }

    final Observable<Integer> saveGoogleUserObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(dbHelper.saveGoogleUser(user));
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    /**
     * Sign out from Google account
     */
    protected void signOut() {
        Auth.GoogleSignInApi.signOut(
                MyApplication.getInstance().getGoogleApiClient())
                .setResultCallback(status -> updateUI(false));
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

    protected void updateUI(boolean update) {
        if (!update) return;
        startMainActivity();
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

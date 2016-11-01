package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class SignUpActivity extends AppCompatActivity implements OnTaskCompleted {

    public static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    private Utils utils;
    private DatabaseTaskHelper dbHelper;
    private User user;
    private OnTaskCompleted listener;

    @OnClick(R.id.sign_in_btn)
    void submitSignUp() {
        onSignUpBtnPressed();
    }

    @OnClick(R.id.sign_in_link)
    void submitSignIn() {
        goToSignInActivity();
    }

    @BindView(R.id.sign_up_email_et)
    AppCompatEditText email;
    @BindView(R.id.sign_up_password_et)
    AppCompatEditText password;
    @BindView(R.id.sign_up_confirm_password_et)
    AppCompatEditText confirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        utils = new Utils();
        dbHelper = new DatabaseTaskHelper();
        listener = this;
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void onSignUpBtnPressed() {
        boolean valid = isFormValid();
        if (!valid)
            return;
        user = new User(email.getText().toString(), password.getText().toString());
        saveRegisteredUser();
    }

    /**
     * Save the registered user to DB and set it to the Global user
     * */
    private void saveRegisteredUser() {
        saveRegisteredUserObservable.subscribe(new Subscriber() {

            @Override
            public void onNext(Object o) {
                Integer result = (Integer) o;
                if (result == Constants.USER_SAVED) {
                    utils.setUserInPrefs(Constants.REGISTERED_USER, SignUpActivity.this);
                    String message = getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail();
                    listener.showMessage(message);
                    updateUI(true);
                } else {
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

    // SAVE REGISTERED USER OBSERVABLE
    final Observable saveRegisteredUserObservable = Observable.create(new Observable.OnSubscribe() {
        @Override
        public void call(Object o) {
            Subscriber subscriber = (Subscriber) o;
            subscriber.onNext(dbHelper.saveRegisteredUser(user));
            subscriber.onCompleted();
        }
    });

    private boolean isFormValid() {
        boolean empty = isSignUpFieldsEmpty();
        if (empty) return false;
        boolean passwordsMatch = isPasswordsEquals();
        if (!passwordsMatch) return false;
        boolean emailRegistered = isEmailRegistered();
        if (emailRegistered) return false;
        // Form valid
        return true;
    }

    private boolean isSignUpFieldsEmpty() {
        String fieldRequired = this.getResources().getString(R.string.field_is_required);
        boolean valid = false;
        // email address validation
        if (utils.isEditTextEmpty(email)) {
            email.setError(fieldRequired);
            valid = true;
        }
        // password validation
        if (utils.isEditTextEmpty(password)) {
            password.setError(fieldRequired);
            valid = true;
        }
        // password confirmation validation
        if (utils.isEditTextEmpty(confirmPassword)) {
            confirmPassword.setError(fieldRequired);
            valid = true;
        }
        return valid;
    }

    private boolean isPasswordsEquals() {
        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        if (pass.equals(confirmPass)) {
            return true;
        }
        confirmPassword.setError(getResources().getString(R.string.passwords_not_match));
        return false;
    }

    private boolean isEmailRegistered() {
        UserRepository userRepo = UserRepository.getInstance();
        List<User> users = userRepo.findByEmail(email.getText().toString());
        if (users != null) {
            if (users.size() > 0) {
                // Email registered
                email.setError(getResources().getString(R.string.email_registered));
                return true;
            }
        }
        return false;
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
    * Updating of UI. If true => goes to MainActivity
    * */
    private void updateUI(boolean update) {
        if (!update) return;
        startMainActivity();
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

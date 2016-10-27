package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    public static String USER_SAVING_MSG = Constants.EMPTY;
    public static String USER_SAVING_ERROR = Constants.EMPTY;

    private Utils utils;

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
    }

    private void onSignUpBtnPressed() {
        boolean valid = isFormValid();
        if (!valid)
            return;
        User user = new User(email.getText().toString(), password.getText().toString());
        new SaveUser().execute(user);
    }

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
        UserRepository userRepo = new UserRepository(getApplicationContext());
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

    /*
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

    private class SaveUser extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... array) {
            User user = array[0];
            UserRepository userRepo = new UserRepository(getApplicationContext());
            int index = userRepo.create(user);
            if (index == 1) {
                List<User> users = userRepo.findByEmail(user.getEmail());
                user = users.get(0);
                // Set global user
                MyApplication.getInstance().setUser(user);
                USER_SAVING_MSG = getResources().getString(R.string.signed_in_as)
                        + ": " + user.getEmail();
                USER_SAVING_ERROR = Constants.EMPTY;
            } else {
                USER_SAVING_ERROR = getResources().getString(R.string.user_saving_failed);
            }
            return USER_SAVING_MSG;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            if (!USER_SAVING_ERROR.isEmpty()) {
                Toast.makeText(getApplicationContext(), USER_SAVING_ERROR, Toast.LENGTH_SHORT).show();
                return;
            }
            // Show authenticated UI
            updateUI(true);
            utils.setUserInPrefs(Constants.REGISTERED_USER, SignUpActivity.this);
            Toast.makeText(getApplicationContext(), USER_SAVING_MSG, Toast.LENGTH_SHORT).show();
        }
    }
}

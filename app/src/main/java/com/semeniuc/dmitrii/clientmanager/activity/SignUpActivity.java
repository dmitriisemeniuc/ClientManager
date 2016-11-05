package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.sign_up_email_et)
    AppCompatEditText editTextEmail;
    @BindView(R.id.sign_up_password_et)
    AppCompatEditText editTextPassword;
    @BindView(R.id.sign_up_confirm_password_et)
    AppCompatEditText editTextConfirmPassword;

    @OnClick(R.id.sign_in_btn)
    void submitSignUp() {
        onSignUpBtnPressed();
    }

    @OnClick(R.id.sign_in_link)
    void submitSignIn() {
        signIn();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @Override
    public void showMessage(String msg) {
        super.showMessage(msg);
    }

    @Override
    public void initInstances() {
        listener = this;
        dbHelper = new DatabaseTaskHelper();
    }

    private boolean isFieldsValid() {
        boolean valid = true;
        if (!Utils.isValidEditText(editTextEmail, this)) valid = false;
        if (!Utils.isValidEditText(editTextPassword, this)) valid = false;
        if (!Utils.isValidEditText(editTextConfirmPassword, this)) valid = false;
        return valid;
    }

    private void onSignUpBtnPressed() {
        if (!isFormValid()) return;
        user = new User(editTextEmail.getText().toString(), editTextPassword.getText().toString());
        saveRegisteredUser();
    }

    /**
     * Save the registered user to DB and set it to the Global user
     */
    private void saveRegisteredUser() {
        saveRegisteredUserObservable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer result) {
                if (result == Constants.USER_SAVED) {
                    Utils.setUserInPrefs(Constants.REGISTERED_USER, SignUpActivity.this);
                    listener.showMessage(getResources().getString(R.string.signed_in_as) + ": "
                            + MyApplication.getInstance().getUser().getEmail());
                    updateUI(true);
                } else listener.showMessage(getResources().getString(R.string.user_saving_failed));
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

    final Observable<Integer> saveRegisteredUserObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(dbHelper.saveRegisteredUser(user));
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    private boolean isFormValid() {
        if (!isFieldsValid()) return false;
        if (!Utils.isEqualPasswords(editTextPassword, editTextConfirmPassword, this)) return false;
        User user = dbHelper.getUserByEmail(editTextEmail.getText().toString());
        if (user != null) {
            editTextEmail.setError(getResources().getString(R.string.email_registered));
            return false;
        }
        return true;
    }

    private void updateUI(boolean update) {
        if (!update) return;
        startActivity(MainActivity.class);
    }

    private void signIn(){
        startActivity(SignInActivity.class);
    }

    private void startActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
}

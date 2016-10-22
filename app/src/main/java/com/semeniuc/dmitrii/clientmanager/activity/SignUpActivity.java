package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.semeniuc.dmitrii.clientmanager.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity{

    public static final int LAYOUT = R.layout.activity_signup;
    public static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    @OnClick(R.id.sign_up_sign_in_link)
    void submitSignIn() {
        goToSignInActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}

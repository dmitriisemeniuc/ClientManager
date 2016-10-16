package com.semeniuc.dmitrii.clientmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.semeniuc.dmitrii.clientmanager.R;

public class AppointmentActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_appointment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
    }
}

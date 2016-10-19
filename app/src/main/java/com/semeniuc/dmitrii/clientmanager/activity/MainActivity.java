package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.adapter.AppointmentsAdapter;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class MainActivity extends SignInActivity implements View.OnClickListener {

    public static final int LAYOUT = R.layout.activity_main;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String USER_SAVING_ERROR_MSG = "";

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        setOnClickListeners();
        // Save Global user to DB
        new SaveUser().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!USER_SAVING_ERROR_MSG.isEmpty())
            Toast.makeText(this, USER_SAVING_ERROR_MSG, Toast.LENGTH_SHORT).show();
        displayAppointments();
    }

    private void displayAppointments() {
        AppointmentRepository appointmentRepo = new AppointmentRepository(this);
        List<Appointment> appointments = (List<Appointment>) appointmentRepo.findAll();
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        // RecyclerView will be displayed as list
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // Set adapter with itemOnClickListener
        mRecyclerView.setAdapter(new AppointmentsAdapter(appointments, new AppointmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment) {
                reviewAppointment(appointment);
            }
        }));
    }

    private void reviewAppointment(Appointment appointment) {
        Intent i = new Intent(this, AppointmentReviewActivity.class);
        i.putExtra(Constants.APPOINTMENT_PATH, appointment);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                signOut();
                return true;
            case R.id.menu_disconnect_account:
                revokeAccess();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_appointment_fab_menu:
                startAppointmentActivity();
                collapseFabMenu();
                break;
        }
    }

    private void collapseFabMenu() {
        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
        fabMenu.close(false);
    }

    private void setOnClickListeners() {
        findViewById(R.id.add_appointment_fab_menu).setOnClickListener(this);
    }

    private void startAppointmentActivity() {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    protected void signOut() {
        super.signOut();
        backToSignInActivity();
    }

    protected void updateUI(boolean signedIn) {
        if (DEBUG) Log.i(LOG_TAG, "updateUI()");
        if (!signedIn) {
            backToSignInActivity();
        }
    }

    /*
    * Returning of user back to sign in activity
    * */
    private void backToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(MyApplication.getInstance().getGoogleApiClient())
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                updateUI(false);
                            }
                        });
    }

    private class SaveUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            UserRepository userRepo = new UserRepository(MyApplication.getInstance().getApplicationContext());
            User user = MyApplication.getInstance().getUser();
            List<User> users = userRepo.findByEmail(user.getEmail());
            if (null != users) {
                if (users.size() == 0) {
                    int index = userRepo.create(user);
                    USER_SAVING_ERROR_MSG = "";
                    if (index <= 0) {
                        USER_SAVING_ERROR_MSG = getResources().getString(R.string.sign_in_failed);
                    }
                } else {
                    USER_SAVING_ERROR_MSG = getResources().getString(R.string.email_already_registered);
                }
            }
            return null;
        }
    }
}

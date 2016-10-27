package com.semeniuc.dmitrii.clientmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.semeniuc.dmitrii.clientmanager.adapter.AppointmentAdapter;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends SignInActivity implements View.OnClickListener {

    public static final int LAYOUT = R.layout.activity_main;
    public static String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        setOnClickListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        displayAppointmentsOrderedByDate();
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

    protected void signOut() {
        Utils utils = new Utils(this);
        String userType = utils.getUserFromPrefs();
        if (userType.equals(Constants.GOOGLE_USER)) {
            super.signOut();
        }
        utils.setUserInPrefs(Constants.NEW_USER);
        backToSignInActivity();
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

    private void startAppointmentActivity() {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    private void displayAppointmentsOrderedByDate() {
        AppointmentRepository appointmentRepo = new AppointmentRepository(this);
        List<Appointment> appointments = appointmentRepo.findAll();
        if (appointments != null) {
            DatabaseHelper helper = new DatabaseHelper(this);
            for (Appointment appointment : appointments) {
                try {
                    helper.getServiceDao().refresh(appointment.getService());
                    helper.getToolsDao().refresh(appointment.getTools());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        // Set adapter with itemOnClickListener
        getRecyclerView().setAdapter(new AppointmentAdapter(appointments, new AppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment) {
                reviewAppointment(appointment);
            }
        }, new AppointmentAdapter.OnPhoneClickListener() {
            @Override
            public void onPhoneClick(String phoneNumber) {
                callToNumber(phoneNumber);
            }
        }));
    }

    private void callToNumber(String phoneNumber) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                phone = phoneNumber;
                // Request permission. No explanation needed
                // The callback method gets the result of the request.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    callToNumber(phone);
                } else {
                    // permission denied
                    Toast.makeText(this, getResources().getString(R.string.grant_call_permission),
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /*
    * Get Recycler View with itemAnimation and LayoutManager setting
    * */
    private RecyclerView getRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        // RecyclerView will be displayed as list
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    /*
    * Pass an appointment to the AppointmentReview Activity using parcelable
    * */
    private void reviewAppointment(Appointment appointment) {
        Intent i = new Intent(this, AppointmentReviewActivity.class);
        i.putExtra(Constants.APPOINTMENT_PATH, appointment);
        startActivity(i);
    }

    private void collapseFabMenu() {
        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
        fabMenu.close(false);
    }

    private void setOnClickListeners() {
        findViewById(R.id.add_appointment_fab_menu).setOnClickListener(this);
    }

    protected void updateUI(boolean signedIn) {
        if (!signedIn) {
            backToSignInActivity();
        }
    }

    /*
    * Returning of the user back to sign in activity
    * */
    private void backToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}

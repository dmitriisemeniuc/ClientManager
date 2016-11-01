package com.semeniuc.dmitrii.clientmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.adapter.AppointmentAdapter;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.CircleTransform;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends SignInActivity implements View.OnClickListener {

    public static String phone;
    private DatabaseTaskHelper dbHelper;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnClickListeners();
        dbHelper = new DatabaseTaskHelper();

        initToolbar();
        initNavigationView();
    }

    @Override
    public void onStart() {
        super.onStart();
        displayAppointments(Constants.ORDER_BY_DATE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
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
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.show_ordered_by_date:
                    displayAppointments(Constants.ORDER_BY_DATE);
                    return true;
                case R.id.show_ordered_by_client:
                    displayAppointments(Constants.ORDER_BY_CLIENT);
                    return true;
                case R.id.show_archived_order_by_date:
                    displayAppointments(Constants.ARCHIVED_ORDER_BY_DATE);
                    return true;
                case R.id.show_archived_order_by_client:
                    displayAppointments(Constants.ARCHIVED_ORDER_BY_CLIENT);
                    return true;
                default:
                    return false;
            }
        });
        toolbar.inflateMenu(R.menu.main_options_menu);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        // Navigation view header
        navHeader = navigation.getHeaderView(0);
        txtEmail = (TextView) navHeader.findViewById(R.id.email);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        navigation.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.action_logout:
                    signOut();
                    break;
            }
            return true;
        });
        // load nav menu header data
        loadNavHeader();
    }

    /**
     * Load navigation menu header information
     */
    private void loadNavHeader() {
        txtEmail.setText(MyApplication.getInstance().getUser().getEmail());
        String photoUrl = MyApplication.getInstance().getUser().getPhotoUrl();
        Picasso.with(this).load(photoUrl).transform(new CircleTransform()).into(imgProfile);
    }

    private void startAppointmentActivity() {
        Intent intent = new Intent(this, AppointmentCreateActivity.class);
        startActivity(intent);
    }

    private void displayAppointments(int option) {
        List<Appointment> appointments;
        switch (option) {
            case Constants.ORDER_BY_DATE:
                appointments = dbHelper.getAppointmentsOrderedByDate();
                break;
            case Constants.ORDER_BY_CLIENT:
                appointments = dbHelper.getAppointmentsOrderedByClient();
                break;
            case Constants.ARCHIVED_ORDER_BY_DATE:
                appointments = dbHelper.getDoneAndPaidAppointmentsOrderedByDate();
                break;
            case Constants.ARCHIVED_ORDER_BY_CLIENT:
                appointments = dbHelper.getDoneAndPaidAppointmentsOrderedByClient();
                break;
            default:
                return;
        }
        getRecyclerView().setAdapter(new AppointmentAdapter(appointments,
                appointment -> reviewAppointment(appointment),
                phoneNumber -> callToNumber(phoneNumber)));
    }

    private void callToNumber(String phoneNumber) {
        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            phone = phoneNumber;
            // The callback method gets the result of the request.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    Constants.PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    protected void signOut() {
        Utils utils = new Utils();
        String userType = utils.getUserFromPrefs(this);
        if (userType.equals(Constants.GOOGLE_USER)) super.signOut();
        utils.setUserInPrefs(Constants.NEW_USER, this);
        backToSignInActivity();
    }

    /**
     * Get Recycler View with itemAnimation and LayoutManager setting
     */
    private RecyclerView getRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        // RecyclerView will be displayed as list
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Set hiding animation for fab menu
                FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
                if (dy > 0) {
                    CoordinatorLayout.LayoutParams layoutParams =
                            (CoordinatorLayout.LayoutParams) menu.getLayoutParams();
                    int fab_bottomMargin = layoutParams.bottomMargin;
                    menu.animate().translationY(menu.getHeight() +
                            fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
                } else if (dy < 0)
                    menu.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            }
        });
        return recyclerView;
    }

    /**
     * Pass an appointment to the AppointmentReview Activity using parcelable
     */
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

    /**
     * Returning of the user back to sign in activity
     */
    private void backToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}

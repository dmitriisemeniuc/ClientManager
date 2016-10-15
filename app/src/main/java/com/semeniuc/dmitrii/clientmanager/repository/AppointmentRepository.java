package com.semeniuc.dmitrii.clientmanager.repository;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class AppointmentRepository implements Crud {

    public static final String LOG_TAG = AppointmentRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;

    private DatabaseHelper helper;

    public AppointmentRepository(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }


    @Override
    public int create(Object item) {
        int index = -1;
        Appointment appointment = (Appointment) item;
        try {
            index = helper.getAppointmentDao().create(appointment);
            if (DEBUG) Log.i(LOG_TAG, "index = " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;

        Appointment appointment = (Appointment) item;

        try {
            helper.getAppointmentDao().update(appointment);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;

        Appointment appointment = (Appointment) item;

        try {
            helper.getAppointmentDao().delete(appointment);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public Object findById(int id) {
        Appointment appointment = null;
        try {
            appointment = helper.getAppointmentDao().queryForId(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    @Override
    public List<?> findAll() {
        List<Appointment> items = null;

        try {
            items = helper.getAppointmentDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}

package com.semeniuc.dmitrii.clientmanager.repository;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
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

    public List<Appointment> findByTitleAndClientName(String title, String clientName) {
        List<Appointment> appointments = null;
        try{
            QueryBuilder<Appointment, Integer> queryBuilder = helper.getAppointmentDao().queryBuilder();
            Where where = queryBuilder.where();
            // The Appointment title must be equals to title
            where.eq(Appointment.APPOINTMENT_TITLE_FIELD_NAME, title);
            where.and();
            // The appointment clientName must be equals to clientName
            where.eq(Appointment.CLIENT_NAME_FIELD_NAME, clientName);
            appointments = queryBuilder.query();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return appointments;
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

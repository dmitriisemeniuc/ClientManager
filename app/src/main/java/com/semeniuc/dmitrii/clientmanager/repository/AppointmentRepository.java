package com.semeniuc.dmitrii.clientmanager.repository;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

public class AppointmentRepository implements Repository {

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
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            queryBuilder.where().eq(Appointment.APPOINTMENT_ID_FIELD_NAME, appointment.getId());
            PreparedQuery<Appointment> preparedQuery = queryBuilder.prepare();
            Appointment appointmentEntry = appointmentDAO.queryForFirst(preparedQuery);
            appointmentEntry = new Utils().updateAppointmentData(appointment, appointmentEntry);
            index = appointmentDAO.update(appointmentEntry);
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
            index = helper.getAppointmentDao().delete(appointment);
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

    public List<?> findAllByUserIdOrderByDate(long id) {
        List<Appointment> items = null;
        try {
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            queryBuilder.where().eq(Appointment.USER_FIELD_NAME, id);
            queryBuilder.orderBy(Appointment.DATE_FIELD_NAME, true); // true for ascending
            items = appointmentDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
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

package com.semeniuc.dmitrii.clientmanager.repository;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

public final class AppointmentRepository implements Repository {

    private static final AppointmentRepository instance = new AppointmentRepository();
    public static final String LOG_TAG = AppointmentRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    private static final DatabaseHelper helper;

    static{
        DatabaseManager.init(MyApplication.getInstance().getApplicationContext());
        helper = DatabaseManager.getInstance().getHelper();
    }

    private AppointmentRepository() {
    }

    public static AppointmentRepository getInstance() {
        return instance;
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Appointment appointment = (Appointment) item;
        try {
            index = helper.getAppointmentDao().create(appointment);
            if (DEBUG) Log.i(LOG_TAG, "created: " + index);
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
            queryBuilder.where().eq(Appointment.ID_FIELD_NAME, appointment.getId());
            PreparedQuery<Appointment> preparedQuery = queryBuilder.prepare();
            Appointment appointmentEntry = appointmentDAO.queryForFirst(preparedQuery);
            appointmentEntry = new Utils().updateAppointmentData(appointment, appointmentEntry);
            index = appointmentDAO.update(appointmentEntry);
            if (DEBUG) Log.i(LOG_TAG, "updated: " + index);
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
            if (DEBUG) Log.i(LOG_TAG, "deleted: " + index);
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
    public List<Appointment> findAll() {
        List<Appointment> appointment = null;
        try {
            appointment = helper.getAppointmentDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    public List<Appointment> findAllOrderedByDate() {
        List<Appointment> items = null;
        long id = MyApplication.getInstance().getUser().getId();
        try {
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            Where<Appointment, Integer> where = queryBuilder.where();
            where.or(
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, false),
                            where.eq(Appointment.PAID_FIELD_NAME, true)),
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, true),
                            where.eq(Appointment.PAID_FIELD_NAME, false)),
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, false),
                            where.eq(Appointment.PAID_FIELD_NAME, false)));
            queryBuilder.orderBy(Appointment.DATE_FIELD_NAME, true);
            items = appointmentDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Appointment> findAllOrderedByClient() {
        List<Appointment> items = null;
        long id = MyApplication.getInstance().getUser().getId();
        try {
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            Where<Appointment, Integer> where = queryBuilder.where();
            where.or(
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, false),
                            where.eq(Appointment.PAID_FIELD_NAME, true)),
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, true),
                            where.eq(Appointment.PAID_FIELD_NAME, false)),
                    where.and(
                            where.eq(Appointment.USER_FIELD_NAME, id),
                            where.eq(Appointment.DONE_FIELD_NAME, false),
                            where.eq(Appointment.PAID_FIELD_NAME, false)));
            queryBuilder.orderBy(Appointment.CLIENT_FIELD_NAME, true);
            items = appointmentDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Appointment> findDoneAndPaidOrderedByDate() {
        List<Appointment> items = null;
        long id = MyApplication.getInstance().getUser().getId();
        try {
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            Where<Appointment, Integer> where = queryBuilder.where();
            where.and(
                    where.eq(Appointment.USER_FIELD_NAME, id),
                    where.eq(Appointment.DONE_FIELD_NAME, true),
                    where.eq(Appointment.PAID_FIELD_NAME, true));
            queryBuilder.orderBy(Appointment.DATE_FIELD_NAME, true); // true for ascending
            items = appointmentDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Appointment> findDoneAndPaidOrderedByClient() {
        List<Appointment> items = null;
        long id = MyApplication.getInstance().getUser().getId();
        try {
            Dao<Appointment, Integer> appointmentDAO = helper.getAppointmentDao();
            QueryBuilder<Appointment, Integer> queryBuilder = appointmentDAO.queryBuilder();
            Where<Appointment, Integer> where = queryBuilder.where();
            where.and(
                    where.eq(Appointment.USER_FIELD_NAME, id),
                    where.eq(Appointment.DONE_FIELD_NAME, true),
                    where.eq(Appointment.PAID_FIELD_NAME, true));
            queryBuilder.orderBy(Appointment.CLIENT_FIELD_NAME, true); // true for ascending
            items = appointmentDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

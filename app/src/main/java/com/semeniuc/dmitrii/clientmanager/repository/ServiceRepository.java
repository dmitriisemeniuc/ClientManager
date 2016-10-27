package com.semeniuc.dmitrii.clientmanager.repository;

import android.util.Log;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Service;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public final class ServiceRepository implements Repository {

    public static final String LOG_TAG = ServiceRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    private static final ServiceRepository instance = new ServiceRepository();
    private static final DatabaseHelper helper;

    static {
        DatabaseManager.init(MyApplication.getInstance().getApplicationContext());
        helper = DatabaseManager.getInstance().getHelper();
    }

    private ServiceRepository() {
    }

    public static ServiceRepository getInstance() {
        return instance;
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Service service = (Service) item;
        try {
            index = helper.getServiceDao().create(service);
            if (DEBUG) Log.i(LOG_TAG, "created: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        Service service = (Service) item;
        try {
            index = helper.getServiceDao().update(service);
            if (DEBUG) Log.i(LOG_TAG, "updated: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        Service service = (Service) item;
        try {
            index = helper.getServiceDao().delete(service);
            if (DEBUG) Log.i(LOG_TAG, "deleted: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        Service service = null;
        try {
            service = helper.getServiceDao().queryForId(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return service;
    }

    @Override
    public List<?> findAll() {
        List<Service> items = null;
        try {
            items = helper.getServiceDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

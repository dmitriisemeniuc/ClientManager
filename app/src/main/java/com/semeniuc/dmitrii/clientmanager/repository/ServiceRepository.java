package com.semeniuc.dmitrii.clientmanager.repository;

import android.content.Context;
import android.util.Log;

import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Service;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class ServiceRepository implements Repository {

    public static final String LOG_TAG = ServiceRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;

    private DatabaseHelper helper;

    public ServiceRepository(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Service service = (Service) item;
        try {
            index = helper.getServiceDao().create(service);
            if (DEBUG) Log.i(LOG_TAG, "index = " + index);
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
            helper.getServiceDao().update(service);
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
            helper.getServiceDao().delete(service);
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

package com.semeniuc.dmitrii.clientmanager.repository;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.Client;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class ClientRepository implements  Repository{

    public static final String LOG_TAG = ClientRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    private static final ClientRepository instance = new ClientRepository();

    private static DatabaseHelper helper;

    static {
        DatabaseManager.init(MyApplication.getInstance().getApplicationContext());
        helper = DatabaseManager.getInstance().getHelper();
    }

    private ClientRepository() {
    }

    public static ClientRepository getInstance() {
        return instance;
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Client client = (Client) item;
        try {
            index = helper.getClientDao().create(client);
            if (DEBUG) Log.i(LOG_TAG, "created: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        Client client = (Client) item;
        try {
            index = helper.getClientDao().update(client);
            if (DEBUG) Log.i(LOG_TAG, "updated: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        Client client = (Client) item;
        try {
            index = helper.getClientDao().delete(client);
            if (DEBUG) Log.i(LOG_TAG, "deleted: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        Client client = null;
        try {
            client = helper.getClientDao().queryForId(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<?> findAll() {
        List<Client> items = null;
        long id = MyApplication.getInstance().getUser().getId();
        try {
            Dao<Client, Integer> clientDAO = helper.getClientDao();
            QueryBuilder<Client, Integer> queryBuilder = clientDAO.queryBuilder();
            Where<Client, Integer> where = queryBuilder.where();
            where.eq(Appointment.USER_FIELD_NAME, id);

            queryBuilder.orderBy(Client.CLIENT_NAME_FIELD_NAME, true); // true for ascending
            items = clientDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

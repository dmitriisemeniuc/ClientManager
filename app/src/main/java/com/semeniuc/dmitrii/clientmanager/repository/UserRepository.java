package com.semeniuc.dmitrii.clientmanager.repository;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class UserRepository implements Repository {

    public static final String LOG_TAG = UserRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    private static final UserRepository instance = new UserRepository();

    private static DatabaseHelper helper;

    static {
        DatabaseManager.init(MyApplication.getInstance().getApplicationContext());
        helper = DatabaseManager.getInstance().getHelper();
    }

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return instance;
    }

    @Override
    public int create(Object item) {
        int index = -1;
        User user = (User) item;
        try {
            index = helper.getUserDao().create(user);
            if (DEBUG) Log.i(LOG_TAG, "created: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        User user = (User) item;
        try {
            index = helper.getUserDao().update(user);
            if (DEBUG) Log.i(LOG_TAG, "updated: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        User user = (User) item;
        try {
            index = helper.getUserDao().delete(user);
            if (DEBUG) Log.i(LOG_TAG, "deleted: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        User user = null;
        try {
            user = helper.getUserDao().queryForId(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> findByEmail(String email) {
        List<User> users = null;
        try {
            Dao<User, Integer> userDAO = helper.getUserDao();
            QueryBuilder<User, Integer> queryBuilder = userDAO.queryBuilder();
            Where where = queryBuilder.where();
            where.eq(User.USER_EMAIL_FIELD_NAME, email);
            users = userDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> findByEmailAndPassword(String email, String password) {
        List<User> users = null;
        try {
            Dao<User, Integer> userDAO = helper.getUserDao();
            QueryBuilder<User, Integer> queryBuilder = userDAO.queryBuilder();
            Where where = queryBuilder.where();
            where.eq(User.USER_EMAIL_FIELD_NAME, email);
            where.and();
            where.eq(User.USER_PASSWORD_FIELD_NAME, password);
            users = userDAO.query(queryBuilder.prepare());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<?> findAll() {
        List<User> items = null;
        try {
            items = helper.getUserDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

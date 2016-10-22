package com.semeniuc.dmitrii.clientmanager.repository;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public class UserRepository implements Repository {

    public static final String LOG_TAG = UserRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;

    private DatabaseHelper helper;

    public UserRepository(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        User user = (User) item;
        try {
            index = helper.getUserDao().create(user);
            if (DEBUG) Log.i(LOG_TAG, "index = " + index);
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
            helper.getUserDao().update(user);
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
            helper.getUserDao().delete(user);
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
            users = helper.getUserDao().queryForEq("email", email);
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

package com.semeniuc.dmitrii.clientmanager.repository;

import android.util.Log;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseHelper;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseManager;
import com.semeniuc.dmitrii.clientmanager.model.Contact;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.List;

public final class ContactRepository implements Repository {

    public static final String LOG_TAG = ContactRepository.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;
    private static final ContactRepository instance = new ContactRepository();
    private static final DatabaseHelper helper;

    static {
        DatabaseManager.init(MyApplication.getInstance().getApplicationContext());
        helper = DatabaseManager.getInstance().getHelper();
    }

    private ContactRepository() {
    }

    public static ContactRepository getInstance() {
        return instance;
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Contact contact = (Contact) item;
        try {
            index = helper.getContactDao().create(contact);
            if (DEBUG) Log.i(LOG_TAG, "created: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        Contact contact = (Contact) item;
        try {
            index = helper.getContactDao().update(contact);
            if (DEBUG) Log.i(LOG_TAG, "updated: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        Contact contact = (Contact) item;
        try {
            index = helper.getContactDao().delete(contact);
            if (DEBUG) Log.i(LOG_TAG, "deleted: " + index);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        Contact contact = null;
        try {
            contact = helper.getContactDao().queryForId(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public List<?> findAll() {
        List<Contact> items = null;
        try {
            items = helper.getContactDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

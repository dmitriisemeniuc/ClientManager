package com.semeniuc.dmitrii.clientmanager.utils;

import android.util.Log;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;

import java.util.List;

public class UserSaverImpl implements IUserSaver {

    public static final String LOG_TAG = UserSaverImpl.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;

    @Override
    public void saveGlobalUserToDb() {
        UserRepository userRepo = new UserRepository(MyApplication.getInstance().getApplicationContext());
        User user = MyApplication.getInstance().getUser();
        List<User> users = userRepo.findByEmail(user.getEmail());
        if (users.size() == 0) {
            int index = userRepo.create(user);
            if (index > 0) {
                if (DEBUG) Log.i(LOG_TAG, "User created successfully");
            } else {
                if (DEBUG) Log.e(LOG_TAG, "Unable to create an user");
            }
        } else {
            if (DEBUG) Log.e(LOG_TAG, "user already exists in DB");
        }
    }
}

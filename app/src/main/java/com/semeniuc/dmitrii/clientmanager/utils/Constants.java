package com.semeniuc.dmitrii.clientmanager.utils;

import com.semeniuc.dmitrii.clientmanager.model.Appointment;

public class Constants {

    public static final String USER = "user";
    public static final String GOOGLE_USER = "google";
    public static final String REGISTERED_USER = "registered";
    public static final String NEW_USER = "new";
    public static final String EMPTY = "";
    public static final int FIRST = 0;
    public static final int SIZE_EMPTY = 0;

    public static final boolean DEBUG = true;
    public static final String DOT = ".";

    public static final int CREATED = 1;
    public static final int UPDATED = 1;
    public static final int DELETED = 1;

    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";

    public static final String APPOINTMENT_PATH =
            Appointment.class.getPackage().getName().concat(DOT + Appointment.class.getSimpleName());
}

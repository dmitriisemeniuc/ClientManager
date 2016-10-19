package com.semeniuc.dmitrii.clientmanager.utils;

import com.semeniuc.dmitrii.clientmanager.model.Appointment;

public class Constants {

    public static final boolean DEBUG = true;
    public static final String DOT = ".";

    public static final int CREATED = 1;
    public static final int UPDATED = 1;
    public static final int DELETED = 1;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String APPOINTMENT_PATH =
            Appointment.class.getPackage().getName().concat(DOT + Appointment.class.getSimpleName());

    public static final long LONG_DEFAULT = 0L;
}

package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;

import com.semeniuc.dmitrii.clientmanager.model.Appointment;

import java.util.List;

public interface IAppointmentDbHelper {

    int saveAppointmentToDb(Appointment appointment);

    List<Appointment> getAllAppointments(Context context);

}

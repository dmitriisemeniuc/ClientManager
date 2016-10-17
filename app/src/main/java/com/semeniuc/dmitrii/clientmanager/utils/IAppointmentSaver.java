package com.semeniuc.dmitrii.clientmanager.utils;

import com.semeniuc.dmitrii.clientmanager.model.Appointment;

public interface IAppointmentSaver {

    int saveAppointmentToDb(Appointment appointment);

}

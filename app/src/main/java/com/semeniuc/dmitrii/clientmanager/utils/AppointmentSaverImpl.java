package com.semeniuc.dmitrii.clientmanager.utils;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;

import java.util.List;

public class AppointmentSaverImpl implements IAppointmentSaver {

    public static final String LOG_TAG = AppointmentSaverImpl.class.getSimpleName();
    public static final boolean DEBUG = Constants.DEBUG;

    @Override
    public int saveAppointmentToDb(Appointment appointment) {
        int saved = Constants.APPOINTMENT_NOT_CREATED;
        AppointmentRepository appointmentRepo = new AppointmentRepository(
                MyApplication.getInstance().getApplicationContext());
        List<Appointment> appointments = appointmentRepo.findByTitleAndClientName(
                appointment.getTitle(), appointment.getClientName());

        if (appointments != null && appointments.size() == 0) {
            int index = appointmentRepo.create(appointment);
            if (index > 0) {
                saved = Constants.APPOINTMENT_CREATED;
            }
        } else if (appointments != null && appointments.size() > 0) {
            saved = Constants.APPOINTMENT_EXISTS;
        } else if (appointments == null) {
            saved = Constants.APPOINTMENT_IS_NULL;
        }
        return saved;
    }
}

package com.semeniuc.dmitrii.clientmanager.utils;

import android.content.Context;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;

import java.util.List;

public class AppointmentDbHelperImpl implements IAppointmentDbHelper {

    public static final String LOG_TAG = AppointmentDbHelperImpl.class.getSimpleName();
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

    @Override
    public List<Appointment> getAllAppointments(Context context) {
        AppointmentRepository appointmentRepo = new AppointmentRepository(context);
        return (List<Appointment>) appointmentRepo.findAll();
    }
}

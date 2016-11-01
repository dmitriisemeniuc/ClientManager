package com.semeniuc.dmitrii.clientmanager.db;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.repository.AppointmentRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ClientRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ContactRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ServiceRepository;
import com.semeniuc.dmitrii.clientmanager.repository.ToolsRepository;
import com.semeniuc.dmitrii.clientmanager.repository.UserRepository;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTaskHelper {

    public Integer saveGoogleUser(User user) {
        UserRepository userRepo = UserRepository.getInstance();
        List<User> users = userRepo.findByEmail(user.getEmail());
        if (null != users) {
            if (users.size() == Constants.SIZE_EMPTY) {
                // CREATE USER
                int index = userRepo.create(user);
                if (index == 1) {
                    users = userRepo.findByEmail(user.getEmail());
                    user = users.get(0);
                    // Set global user
                    MyApplication.getInstance().setUser(user);
                    return Constants.USER_SAVED;
                } else {
                    return Constants.USER_NOT_SAVED;
                }
            } else {
                // USER EXISTS
                user = users.get(0);
                MyApplication.getInstance().setUser(user);
                return Constants.USER_EXISTS;
            }
        } else {
            return Constants.NO_DB_RESULT;
        }
    }

    public Integer saveRegisteredUser(User user) {
        UserRepository userRepo = UserRepository.getInstance();
        int index = userRepo.create(user);
        if (index == 1) {
            List<User> users = userRepo.findByEmail(user.getEmail());
            user = users.get(0);
            // Set global user
            MyApplication.getInstance().setUser(user);
            return Constants.USER_SAVED;
        }
        return Constants.USER_NOT_SAVED;
    }

    public Integer setGlobalUser(String email) {
        UserRepository userRepo = UserRepository.getInstance();
        List<User> users = userRepo.findByEmail(email);
        if (null != users) {
            if (users.size() > 0) {
                User user = users.get(0);
                // Set global user
                MyApplication.getInstance().setUser(user);
                return Constants.USER_SAVED;
            } else {
                return Constants.USER_NOT_SAVED;
            }
        }
        return Constants.NO_DB_RESULT;
    }

    public int saveAppointment(Appointment appointment){
        ServiceRepository serviceRepo = ServiceRepository.getInstance();
        serviceRepo.create(appointment.getService());
        ToolsRepository toolsRepo = ToolsRepository.getInstance();
        toolsRepo.create(appointment.getTools());
        ContactRepository contactRepo = ContactRepository.getInstance();
        contactRepo.create(appointment.getClient().getContact());
        ClientRepository clientRepo = ClientRepository.getInstance();
        clientRepo.create(appointment.getClient());
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        return appointmentRepo.create(appointment);
    }

    public int updateAppointment(Appointment appointment){
        ServiceRepository serviceRepo = ServiceRepository.getInstance();
        serviceRepo.update(appointment.getService());
        ToolsRepository toolsRepo = ToolsRepository.getInstance();
        toolsRepo.update(appointment.getTools());
        ContactRepository contactRepo = ContactRepository.getInstance();
        contactRepo.update(appointment.getClient().getContact());
        ClientRepository clientRepo = ClientRepository.getInstance();
        clientRepo.update(appointment.getClient());
        appointment.setUser(MyApplication.getInstance().getUser());
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        return appointmentRepo.update(appointment);
    }

    public int deleteAppointment(Appointment appointment){
        ServiceRepository serviceRepo = ServiceRepository.getInstance();
        serviceRepo.delete(appointment.getService());
        ToolsRepository toolsRepo = ToolsRepository.getInstance();
        toolsRepo.delete(appointment.getTools());
        ContactRepository contactRepo = ContactRepository.getInstance();
        contactRepo.delete(appointment.getClient().getContact());
        ClientRepository clientRepo = ClientRepository.getInstance();
        clientRepo.delete(appointment.getClient());
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        return appointmentRepo.delete(appointment);
    }

    public List<Appointment> getAppointmentsOrderedByDate(){
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        List<Appointment> appointments = appointmentRepo.findAllOrderedByDate();
        if (appointments != null) {
            DatabaseHelper helper = new DatabaseHelper(MyApplication.getInstance().getApplicationContext());
            for (Appointment appointment : appointments) {
                try {
                    helper.getClientDao().refresh(appointment.getClient());
                    helper.getContactDao().refresh(appointment.getClient().getContact());
                    helper.getServiceDao().refresh(appointment.getService());
                    helper.getToolsDao().refresh(appointment.getTools());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsOrderedByClient(){
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        List<Appointment> appointments = appointmentRepo.findAllOrderedByClient();
        if (appointments != null) {
            DatabaseHelper helper = new DatabaseHelper(MyApplication.getInstance().getApplicationContext());
            for (Appointment appointment : appointments) {
                try {
                    helper.getClientDao().refresh(appointment.getClient());
                    helper.getContactDao().refresh(appointment.getClient().getContact());
                    helper.getServiceDao().refresh(appointment.getService());
                    helper.getToolsDao().refresh(appointment.getTools());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getDoneAndPaidAppointmentsOrderedByDate(){
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        List<Appointment> appointments = appointmentRepo.findDoneAndPaidOrderedByDate();
        if (appointments != null) {
            DatabaseHelper helper = new DatabaseHelper(MyApplication.getInstance().getApplicationContext());
            for (Appointment appointment : appointments) {
                try {
                    helper.getClientDao().refresh(appointment.getClient());
                    helper.getContactDao().refresh(appointment.getClient().getContact());
                    helper.getServiceDao().refresh(appointment.getService());
                    helper.getToolsDao().refresh(appointment.getTools());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getDoneAndPaidAppointmentsOrderedByClient(){
        AppointmentRepository appointmentRepo = AppointmentRepository.getInstance();
        List<Appointment> appointments = appointmentRepo.findDoneAndPaidOrderedByClient();
        if (appointments != null) {
            DatabaseHelper helper = new DatabaseHelper(MyApplication.getInstance().getApplicationContext());
            for (Appointment appointment : appointments) {
                try {
                    helper.getClientDao().refresh(appointment.getClient());
                    helper.getContactDao().refresh(appointment.getClient().getContact());
                    helper.getServiceDao().refresh(appointment.getService());
                    helper.getToolsDao().refresh(appointment.getTools());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return appointments;
    }
}

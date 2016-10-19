package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;

import java.util.Date;

@DatabaseTable
public class Appointment implements Parcelable {

    public static final String APPOINTMENT_ID_FIELD_NAME = "_ID";
    public static final String USER_GOOGLE_ID_FIELD_NAME = "google_id";
    public static final String APPOINTMENT_TITLE_FIELD_NAME = "title";
    public static final String CLIENT_NAME_FIELD_NAME = "client";
    public static final String CLIENT_PHONE_FIELD_NAME = "phone";
    public static final String SERVICE_FIELD_NAME = "service";
    public static final String INFO_FIELD_NAME = "info";
    public static final String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true, columnName = APPOINTMENT_ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = false, columnName = USER_GOOGLE_ID_FIELD_NAME)
    private String userGoogleId;
    @DatabaseField(canBeNull = false, columnName = APPOINTMENT_TITLE_FIELD_NAME)
    private String title;
    @DatabaseField(canBeNull = false, columnName = CLIENT_NAME_FIELD_NAME)
    private String clientName;
    @DatabaseField(canBeNull = false, columnName = CLIENT_PHONE_FIELD_NAME)
    private String clientPhone;
    @DatabaseField(canBeNull = false, columnName = SERVICE_FIELD_NAME)
    private String service;
    @DatabaseField(canBeNull = true, columnName = INFO_FIELD_NAME)
    private String info;
    @DatabaseField(canBeNull = false, columnName = DATE_FIELD_NAME,
            dataType = DataType.DATE_STRING, format = Constants.DATE_FORMAT)
    private Date date;

    public Appointment() {
    }

    public Appointment(long id, String userGoogleId, String title, String clientName, String clientPhone,
                       String service, String info, Date date) {
        this.id = id;
        this.userGoogleId = userGoogleId;
        this.title = title;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.service = service;
        this.info = info;
        this.date = date;
    }

    public Appointment(Parcel in) {
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserGoogleId() {
        return userGoogleId;
    }

    public void setUserGoogleId(String userGoogleId) {
        this.userGoogleId = userGoogleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // Write each field into the parcel. Order is important
        parcel.writeLong(id);
        parcel.writeString(userGoogleId);
        parcel.writeString(title);
        parcel.writeString(clientName);
        parcel.writeString(clientPhone);
        parcel.writeString(service);
        parcel.writeString(info);
        parcel.writeString(date.toString());
    }

    private void readFromParcel(Parcel in) {

        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        userGoogleId = in.readString();
        title = in.readString();
        clientName = in.readString();
        clientPhone = in.readString();
        service = in.readString();
        info = in.readString();
        date = new Date(in.readString());
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Appointment createFromParcel(Parcel in) {
                    return new Appointment(in);
                }

                public Appointment[] newArray(int size) {
                    return new Appointment[size];
                }
            };
}

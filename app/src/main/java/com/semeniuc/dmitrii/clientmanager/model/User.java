package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User implements Parcelable {

    public static final String USER_GOOGLE_ID_FIELD_NAME = "google_id";
    public static final String USER_NAME_FIELD_NAME = "name";
    public static final String USER_EMAIL_FIELD_NAME = "email";
    public static final String USER_PASSWORD_FIELD_NAME = "password";

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = true, columnName = USER_GOOGLE_ID_FIELD_NAME, unique = true)
    private String googleId;
    @DatabaseField(canBeNull = false, columnName = USER_NAME_FIELD_NAME)
    private String name;
    @DatabaseField(canBeNull = false, columnName = USER_EMAIL_FIELD_NAME, unique = true)
    private String email;
    @DatabaseField(canBeNull = true, columnName = USER_PASSWORD_FIELD_NAME)
    private String password;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Appointment> appointments;

    public User() {
    }

    public User(Parcel in) {
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ForeignCollection<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ForeignCollection<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // Write each field into the parcel. Order is important
        parcel.writeLong(id);
        parcel.writeString(googleId);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
    }

    private void readFromParcel(Parcel in) {

        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        googleId = in.readString();
        name = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                public User[] newArray(int size) {
                    return new User[size];
                }
            };
}

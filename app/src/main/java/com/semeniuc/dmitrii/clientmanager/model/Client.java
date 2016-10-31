package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Client implements Parcelable {

    public static final String CLIENT_ID_FIELD_NAME = "_ID";
    public static final String CLIENT_NAME_FIELD_NAME = "name";
    public static final String USER_FIELD_NAME = "user_id";
    public static final String CONTACT_FIELD_NAME = "contact_id";

    @DatabaseField(generatedId = true, columnName = CLIENT_ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = false, columnName = CLIENT_NAME_FIELD_NAME)
    private String name;
    @DatabaseField(canBeNull = false, foreign = true, columnName = USER_FIELD_NAME)
    private User user;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true,
            columnDefinition = "integer references contact(_id) on delete cascade",
            columnName = CONTACT_FIELD_NAME)
    private Contact contact;

    public Client() {
    }

    public Client(User user) {
        this.user = user;
    }

    public Client(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // Write each field into the parcel. Order is important
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(contact, i);
    }

    private void readFromParcel(Parcel in) {
        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        name = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        contact = in.readParcelable(Contact.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Client createFromParcel(Parcel in) {
                    return new Client(in);
                }

                public Client[] newArray(int size) {
                    return new Client[size];
                }
            };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}

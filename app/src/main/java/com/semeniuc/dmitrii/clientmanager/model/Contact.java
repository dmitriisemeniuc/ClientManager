package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Contact implements Parcelable {

    public static final String ID_FIELD_NAME = "_ID";
    public static final String PHONE_FIELD_NAME = "phone";
    public static final String ADDRESS_FIELD_NAME = "address";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = true, columnName = PHONE_FIELD_NAME)
    private String phone;
    @DatabaseField(canBeNull = true, columnName = ADDRESS_FIELD_NAME)
    private String address;

    public Contact() {
    }

    public Contact(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(phone);
        parcel.writeString(address);
    }

    private void readFromParcel(Parcel in) {
        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        phone = in.readString();
        address = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Contact createFromParcel(Parcel in) {
                    return new Contact(in);
                }

                public Contact[] newArray(int size) {
                    return new Contact[size];
                }
            };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

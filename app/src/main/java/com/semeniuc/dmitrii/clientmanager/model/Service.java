package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Service implements Parcelable {

    public static final String ID_FIELD_NAME = "_ID";
    public static final String NAME_FIELD_NAME = "name";
    public static final String HAIR_COLORING_FIELD_NAME = "hair_coloring";
    public static final String HAIRDO_FIELD_NAME = "hairdo";
    public static final String HAIRCUT_FIELD_NAME = "haircut";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = false, columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(canBeNull = false, columnName = HAIR_COLORING_FIELD_NAME)
    private boolean hairColoring;
    @DatabaseField(canBeNull = false, columnName = HAIRDO_FIELD_NAME)
    private boolean hairdo;
    @DatabaseField(canBeNull = false, columnName = HAIRCUT_FIELD_NAME)
    private boolean haircut;

    public Service() {
    }

    public Service(Parcel in) {
        readFromParcel(in);
    }

    public Service(String name, boolean hairColoring, boolean hairdo, boolean haircut) {
        this.name = name;
        this.hairColoring = hairColoring;
        this.hairdo = hairdo;
        this.haircut = haircut;
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
        parcel.writeInt(hairColoring ? 1 : 0);
        parcel.writeInt(hairdo ? 1 : 0);
        parcel.writeInt(haircut ? 1 : 0);
    }

    private void readFromParcel(Parcel in) {

        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        name = in.readString();
        hairColoring = in.readInt() == 1;
        hairdo = in.readInt() == 1;
        haircut = in.readInt() == 1;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Service createFromParcel(Parcel in) {
                    return new Service(in);
                }

                public Service[] newArray(int size) {
                    return new Service[size];
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

    public boolean isHairColoring() {
        return hairColoring;
    }

    public void setHairColoring(boolean hairColoring) {
        this.hairColoring = hairColoring;
    }

    public boolean isHairdo() {
        return hairdo;
    }

    public void setHairdo(boolean hairdo) {
        this.hairdo = hairdo;
    }

    public boolean isHaircut() {
        return haircut;
    }

    public void setHaircut(boolean haircut) {
        this.haircut = haircut;
    }
}

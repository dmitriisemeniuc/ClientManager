package com.semeniuc.dmitrii.clientmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Tools implements Parcelable {

    public static final String TOOLS_ID_FIELD_NAME = "_ID";
    public static final String TOOLS_BRUSH_FIELD_NAME = "brush";
    public static final String TOOLS_HAIR_BRUSH_FIELD_NAME = "hair_brush";
    public static final String TOOLS_HAIR_DRYER_FIELD_NAME = "hair_dryer";
    public static final String TOOLS_HAIR_BAND_FIELD_NAME = "hair_band";
    public static final String TOOLS_CUT_SET_FIELD_NAME = "cut_set";
    public static final String TOOLS_SPRAY_FIELD_NAME = "spray";
    public static final String TOOLS_OXY_FIELD_NAME = "oxy";
    public static final String TOOLS_TUBE_FIELD_NAME = "tube";
    public static final String TOOLS_TRIMMER_FIELD_NAME = "trimmer";

    @DatabaseField(generatedId = true, columnName = TOOLS_ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = false, columnName = TOOLS_BRUSH_FIELD_NAME)
    private boolean brush;
    @DatabaseField(canBeNull = false, columnName = TOOLS_HAIR_BRUSH_FIELD_NAME)
    private boolean hairBrush;
    @DatabaseField(canBeNull = false, columnName = TOOLS_HAIR_DRYER_FIELD_NAME)
    private boolean hairDryer;
    @DatabaseField(canBeNull = false, columnName = TOOLS_HAIR_BAND_FIELD_NAME)
    private boolean hairBand;
    @DatabaseField(canBeNull = false, columnName = TOOLS_CUT_SET_FIELD_NAME)
    private boolean cutSet;
    @DatabaseField(canBeNull = false, columnName = TOOLS_SPRAY_FIELD_NAME)
    private boolean spray;
    @DatabaseField(canBeNull = false, columnName = TOOLS_OXY_FIELD_NAME)
    private boolean oxy;
    @DatabaseField(canBeNull = false, columnName = TOOLS_TUBE_FIELD_NAME)
    private boolean tube;
    @DatabaseField(canBeNull = false, columnName = TOOLS_TRIMMER_FIELD_NAME)
    private boolean trimmer;

    public Tools() {
    }

    public Tools(Parcel in) {
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
        parcel.writeValue(brush);
        parcel.writeValue(hairBrush);
        parcel.writeValue(hairDryer);
        parcel.writeValue(hairBand);
        parcel.writeValue(cutSet);
        parcel.writeValue(spray);
        parcel.writeValue(oxy);
        parcel.writeValue(tube);
        parcel.writeValue(trimmer);
    }

    private void readFromParcel(Parcel in) {
        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        brush = (Boolean) in.readValue(null);
        hairBrush = (Boolean) in.readValue(null);
        hairDryer = (Boolean) in.readValue(null);
        hairBand = (Boolean) in.readValue(null);
        cutSet = (Boolean) in.readValue(null);
        spray = (Boolean) in.readValue(null);
        oxy = (Boolean) in.readValue(null);
        tube = (Boolean) in.readValue(null);
        trimmer = (Boolean) in.readValue(null);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Tools createFromParcel(Parcel in) {
                    return new Tools(in);
                }

                public Tools[] newArray(int size) {
                    return new Tools[size];
                }
            };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBrush() {
        return brush;
    }

    public void setBrush(boolean brush) {
        this.brush = brush;
    }

    public boolean isHairBrush() {
        return hairBrush;
    }

    public void setHairBrush(boolean hairBrush) {
        this.hairBrush = hairBrush;
    }

    public boolean isHairDryer() {
        return hairDryer;
    }

    public void setHairDryer(boolean hairDryer) {
        this.hairDryer = hairDryer;
    }

    public boolean isHairBand() {
        return hairBand;
    }

    public void setHairBand(boolean hairBand) {
        this.hairBand = hairBand;
    }

    public boolean isCutSet() {
        return cutSet;
    }

    public void setCutSet(boolean cutSet) {
        this.cutSet = cutSet;
    }

    public boolean isSpray() {
        return spray;
    }

    public void setSpray(boolean spray) {
        this.spray = spray;
    }

    public boolean isOxy() {
        return oxy;
    }

    public void setOxy(boolean oxy) {
        this.oxy = oxy;
    }

    public boolean isTube() {
        return tube;
    }

    public void setTube(boolean tube) {
        this.tube = tube;
    }

    public boolean isTrimmer() {
        return trimmer;
    }

    public void setTrimmer(boolean trimmer) {
        this.trimmer = trimmer;
    }
}

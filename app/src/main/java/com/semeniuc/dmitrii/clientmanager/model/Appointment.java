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

    public static final String ID_FIELD_NAME = "_ID";
    public static final String USER_FIELD_NAME = "user_id";
    public static final String CLIENT_FIELD_NAME = "client";
    public static final String SERVICE_FIELD_NAME = "service";
    public static final String TOOLS_FIELD_NAME = "tools";
    public static final String INFO_FIELD_NAME = "info";
    public static final String DATE_FIELD_NAME = "date";
    public static final String SUM_FIELD_NAME = "sum";
    public static final String PAID_FIELD_NAME = "paid";
    public static final String DONE_FIELD_NAME = "done";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;
    @DatabaseField(canBeNull = false, foreign = true, columnName = USER_FIELD_NAME)
    private User user;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true,
            columnDefinition = "integer references client(_id) on delete cascade",
            columnName = CLIENT_FIELD_NAME)
    private Client client;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true,
            columnDefinition = "integer references service(_id) on delete cascade",
            columnName = SERVICE_FIELD_NAME)
    private Service service;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true,
            columnDefinition = "integer references tools(_id) on delete cascade",
            columnName = TOOLS_FIELD_NAME)
    private Tools tools;
    @DatabaseField(canBeNull = true, columnName = INFO_FIELD_NAME)
    private String info;
    @DatabaseField(canBeNull = false, columnName = DATE_FIELD_NAME,
            dataType = DataType.DATE_STRING, format = Constants.DATE_TIME_FORMAT)
    private Date date;
    @DatabaseField(canBeNull = true, columnName = SUM_FIELD_NAME)
    private String sum;
    @DatabaseField(canBeNull = false, columnName = PAID_FIELD_NAME)
    private boolean paid;
    @DatabaseField(canBeNull = false, columnName = DONE_FIELD_NAME)
    private boolean done;

    public Appointment() {
    }

    // Constructor for creating new Appointment without id
    public Appointment(User user, Client client, Service service,
                       Tools tools, String info, Date date, String sum, boolean paid, boolean done) {
        this.user = user;
        this.client = client;
        this.service = service;
        this.tools = tools;
        this.sum = sum;
        this.paid = paid;
        this.done = done;
        this.info = info;
        this.date = date;
    }

    // Constructor for creating new Appointment with specified id
    public Appointment(long id, User user, Client client, Service service,
                       Tools tools, String info, Date date, String sum, boolean paid, boolean done) {
        this.id = id;
        this.user = user;
        this.client = client;
        this.service = service;
        this.tools = tools;
        this.info = info;
        this.date = date;
        this.sum = sum;
        this.paid = paid;
        this.done = done;
    }

    public Appointment(Parcel in) {
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
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(client, i);
        parcel.writeParcelable(service, i);
        parcel.writeParcelable(tools, i);
        parcel.writeString(info);
        parcel.writeString(date.toString());
        parcel.writeString(sum);
        parcel.writeInt(paid ? 1 : 0);
        parcel.writeInt(done ? 1 : 0);
    }

    private void readFromParcel(Parcel in) {
        // Read back each field in the order that it was written to the parcel
        id = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        client = in.readParcelable(Client.class.getClassLoader());
        service = in.readParcelable(Service.class.getClassLoader());
        tools = in.readParcelable(Tools.class.getClassLoader());
        info = in.readString();
        date = new Date(in.readString());
        sum = in.readString();
        paid = in.readInt() == 1;
        done = in.readInt() == 1;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Tools getTools() {
        return tools;
    }

    public void setTools(Tools tools) {
        this.tools = tools;
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

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

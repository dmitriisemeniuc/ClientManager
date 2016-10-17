package com.semeniuc.dmitrii.clientmanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User {

    public static final String USER_GOOGLE_ID_FIELD_NAME = "google_id";
    public static final String USER_NAME_FIELD_NAME = "name";
    public static final String USER_EMAIL_FIELD_NAME = "email";

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false, columnName = USER_GOOGLE_ID_FIELD_NAME, unique = true)
    private String googleId;
    @DatabaseField(canBeNull = false, columnName = USER_NAME_FIELD_NAME)
    private String name;
    @DatabaseField(canBeNull = false, columnName = USER_EMAIL_FIELD_NAME, unique = true)
    private String email;

    public User(){

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
}

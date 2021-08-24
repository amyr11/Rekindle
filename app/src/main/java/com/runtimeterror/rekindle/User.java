package com.runtimeterror.rekindle;

public class User {
    private String userID;
    private String username;
    private String photoURL;

    public User() {}

    public User(String userID, String username, String photoURL) {
        this.userID = userID;
        this.username = username;
        this.photoURL = photoURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}

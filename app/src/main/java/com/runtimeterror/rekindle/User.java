package com.runtimeterror.rekindle;

public class User {
    private String userID;
    private String username;
    private String photoURL;
    private int threadCount;
    private int points;

    public User() {}

    public User(String userID, String username, String photoURL, int threadCount, int points) {
        this.userID = userID;
        this.username = username;
        this.photoURL = photoURL;
        this.threadCount = threadCount;
        this.points = points;
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

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

package com.runtimeterror.rekindle;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private String userID;
    private String username;
    private String photoURL;
    private List<String> threads;
    private int points;
    private boolean competitive;
    private int goldCnt;
    private int silverCnt;
    private int bronzeCnt;

    public UserInfo() {}

    public UserInfo(String userID, String username, String photoURL) {
        this.userID = userID;
        this.username = username;
        this.photoURL = photoURL;
        this.points = 0;
        this.competitive = false;
        this.goldCnt = 0;
        this.silverCnt = 0;
        this.bronzeCnt = 0;
        this.threads = new ArrayList<>();
    }

    public int getGoldCnt() {
        return goldCnt;
    }

    public void setGoldCnt(int goldCnt) {
        this.goldCnt = goldCnt;
    }

    public int getSilverCnt() {
        return silverCnt;
    }

    public void setSilverCnt(int silverCnt) {
        this.silverCnt = silverCnt;
    }

    public int getBronzeCnt() {
        return bronzeCnt;
    }

    public void setBronzeCnt(int bronzeCnt) {
        this.bronzeCnt = bronzeCnt;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isCompetitive() {
        return competitive;
    }

    public void setCompetitive(boolean competitive) {
        this.competitive = competitive;
    }

    public List<String> getThreads() {
        return threads;
    }

    public void setThreads(List<String> threads) {
        this.threads = threads;
    }
}

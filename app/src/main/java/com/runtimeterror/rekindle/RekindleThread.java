package com.runtimeterror.rekindle;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class RekindleThread {
    @Exclude
    private String id;
    @ServerTimestamp
    private Date date;
    private String name;
    private int theme;
    private List<String> members;

    public RekindleThread() {}

    public RekindleThread(Date date, String name, int theme, List<String> members) {
        this.date = date;
        this.name = name;
        this.theme = theme;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public List<String> getMembers() {
        return members;
    }

    public int getMemberCount() {
        return members.size();
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}

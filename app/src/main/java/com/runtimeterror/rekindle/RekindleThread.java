package com.runtimeterror.rekindle;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RekindleThread {
    @Exclude
    private String id;
    @ServerTimestamp
    private Date createdAt;
    private String createdBy;
    private String name;
    private String status;
    private int theme;
    private List<String> members;


    public RekindleThread() {}

    public RekindleThread(Date createdAt, String name, int theme, String creator) {
        this.createdAt = createdAt;
        this.name = name;
        this.theme = theme;
        this.members = new ArrayList<>();
        this.createdBy = creator;
        this.members.add(creator);
        this.status = Constants.THREAD_OPEN;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    @Exclude
    public int getMemberCount() {
        return members.size();
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Exclude
    public boolean isOwned() {
        DBhelper db = new DBhelper();
        return getCreatedBy().equals(db.getUser().getUid());
    }

    public String getStatus() {
        if (status != null)
            return status;
        else
            return Constants.THREAD_OPEN;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

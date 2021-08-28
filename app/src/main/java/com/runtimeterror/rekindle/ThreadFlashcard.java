package com.runtimeterror.rekindle;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

class ThreadFlashcard extends Flashcard {
    @ServerTimestamp
    private Date date;
    @Exclude
    private String id;
    private String question;
    private String answer;
    private int boxNumber;
    private boolean show;
    private String decoy1;

    public String getDecoy1() {
        return decoy1;
    }

    public void setDecoy1(String decoy1) {
        this.decoy1 = decoy1;
    }

    public String getDecoy2() {
        return decoy2;
    }

    public void setDecoy2(String decoy2) {
        this.decoy2 = decoy2;
    }

    private String decoy2;

    public ThreadFlashcard() {}

    public ThreadFlashcard(String question, String answer, Date date, String decoy1, String decoy2) {
        this.question = question;
        this.answer = answer;
        this.date = date;
        this.boxNumber = 1;
        this.show = true;
        this.decoy1 = decoy1;
        this.decoy2 = decoy2;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public int getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}

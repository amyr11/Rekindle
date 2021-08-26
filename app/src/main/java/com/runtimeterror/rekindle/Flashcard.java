package com.runtimeterror.rekindle;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Flashcard {
    @ServerTimestamp
    private Date date;
    @Exclude
    private String id;
    private String question;
    private String answer;

    public Flashcard() {}

    public Flashcard(String question, String answer, Date date) {
        this.question = question;
        this.answer = answer;
        this.date = date;
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
}

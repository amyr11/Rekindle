package com.runtimeterror.rekindle;

import java.util.List;

public class FlashcardCollection {
    private String titleFull;
    private String titleAbbr;
    private String color;

    public FlashcardCollection() {}

    public FlashcardCollection(String titleFull, String titleAbbr, String color) {
        this.titleFull = titleFull;
        this.titleAbbr = titleAbbr;
        this.color = color;
    }

    public String getTitleFull() {
        return titleFull;
    }

    public void setTitleFull(String titleFull) {
        this.titleFull = titleFull;
    }

    public String getTitleAbbr() {
        return titleAbbr;
    }

    public void setTitleAbbr(String titleAbbr) {
        this.titleAbbr = titleAbbr;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

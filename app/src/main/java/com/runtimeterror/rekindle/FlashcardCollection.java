package com.runtimeterror.rekindle;

public class FlashcardCollection {
    private String titleFull;
    private String titleAbbr;
    private int theme;

    public FlashcardCollection() {}

    public FlashcardCollection(String titleFull, String titleAbbr, int theme) {
        this.titleFull = titleFull;
        this.titleAbbr = titleAbbr;
        this.theme = theme;
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

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }
}

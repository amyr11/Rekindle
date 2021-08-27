package com.runtimeterror.rekindle;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FlashcardCollection {
    public static String FCOLLECTION_FULL_TITLE = "titleFull";
    public static String FCOLLECTION_ABBR_TITLE = "titleAbbr";

    @ServerTimestamp
    private Date date;
    @Exclude
    private String id;
    private String titleFull;
    private String titleAbbr;
    private int theme;
    private int box1State, box2State, box3State;

    public FlashcardCollection() {}

    //for FlashcardCollection initialization when adding new item on database not for retrieving
    public FlashcardCollection(Date date, String titleFull, String titleAbbr, int theme) {
        this.date = date;
        this.titleFull = titleFull;
        this.titleAbbr = titleAbbr;
        this.theme = theme;
        this.box1State = 0;
        this.box2State = 0;
        this.box3State = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String generateAbbr(String full) {
        List<Character> vowels = Arrays.asList(
                'A', 'E', 'I', 'O', 'U'
        );
        StringBuilder abbr = new StringBuilder();
        for (int i = 0; i < full.length(); i++) {
            char chr = full.toUpperCase().charAt(i);
            if (!vowels.contains(chr) || i == 0) {
                abbr.append(chr);
                if (abbr.length() == Constants.COLLECTIONS_ABBR_LIMIT)
                    break;
            }
        }
        return abbr.toString();
    }

    public static int selectRandTheme() {
        return new Random().nextInt(Constants.THEME_COUNT);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBox1State() {
        return box1State;
    }

    public void setBox1State(int box1State) {
        this.box1State = box1State;
    }

    public int getBox2State() {
        return box2State;
    }

    public void setBox2State(int box2State) {
        this.box2State = box2State;
    }

    public int getBox3State() {
        return box3State;
    }

    public void setBox3State(int box3State) {
        this.box3State = box3State;
    }
}

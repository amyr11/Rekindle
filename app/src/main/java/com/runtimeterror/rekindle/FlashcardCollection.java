package com.runtimeterror.rekindle;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public static String generateAbbr(String full) {
        List<Character> vowels = Arrays.asList(
                'A', 'E', 'I', 'O', 'U'
        );
        StringBuilder abbr = new StringBuilder();
        for (int i = 0; i < full.length(); i++) {
            char chr = full.toUpperCase().charAt(i);
            if (!vowels.contains(chr) || i == 0) {
                abbr.append(chr);
                if (abbr.length() > Constants.COLLECTIONS_ABBR_LIMIT)
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
}

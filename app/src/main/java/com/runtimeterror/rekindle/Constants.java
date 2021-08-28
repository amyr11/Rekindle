package com.runtimeterror.rekindle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Constants {
    public static String TAG = "RekindleTag";
    public static String COL_THREADS = "threads";
    public static String COL_MESSAGES = "messages";
    public static String THREAD_CLOSED = "closed";
    public static String THREAD_OPEN = "open";
    public static String FIELD_STATUS = "status";
    public static String FIELD_MEMBERS = "members";
    public static String FIELD_USERNAME = "username";
    public static String FIELD_SENT_AT = "sentAt";
    public static String COL_USERS = "users";
    public static String COL_FLASHCARD_COLLECTIONS = "flashcardCollections";
    public static String COL_FLASHCARD_LIST = "flashcards";
    public static int COLLECTIONS_CHAR_LIMIT = 40;
    public static int COLLECTIONS_ABBR_LIMIT = 6;
    public static int BOX1_THRESHOLD = 0;
    public static int BOX2_THRESHOLD = 2;
    public static int BOX3_THRESHOLD = 5;
    public static int THEME_COUNT = 5;
    public static int ORANGE = 0;
    public static int PURPLE = 1;
    public static int ORANGE_YELLOW = 2;
    public static int RED = 3;
    public static int PINK = 4;
}

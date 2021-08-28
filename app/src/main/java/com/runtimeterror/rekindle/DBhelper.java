package com.runtimeterror.rekindle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBhelper {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private CollectionReference usersColRef;
    private DocumentReference userDocRef;
    private CollectionReference flashcardCollectionsColRef;

    private CollectionReference threadsColRef;

    public DBhelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        usersColRef = db.collection(Constants.COL_USERS);
        userDocRef = usersColRef.document(user.getUid());
        threadsColRef = db.collection(Constants.COL_THREADS);
        flashcardCollectionsColRef = userDocRef.collection(Constants.COL_FLASHCARD_COLLECTIONS);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public CollectionReference getUsersColRef() {
        return usersColRef;
    }

    public DocumentReference getUserDocRef() {
        return userDocRef;
    }

    public CollectionReference getFlashcardCollectionsColRef() {
        return flashcardCollectionsColRef;
    }

    public DocumentReference getFlashcardCollectionDocRef(String collectionID) {
        return flashcardCollectionsColRef.document(collectionID);
    }

    public CollectionReference getFlashcardListColRef(String collectionID) {
        return flashcardCollectionsColRef.document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST);
    }

    public DocumentReference getFlashcardDocRef(String collectionID, String flashcardID) {
        return getFlashcardListColRef(collectionID).document(flashcardID);
    }

    public CollectionReference getThreadsColRef() {
        return threadsColRef;
    }
}

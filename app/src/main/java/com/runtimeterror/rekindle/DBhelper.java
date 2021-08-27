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

    private DocumentReference userDocRef;
    private CollectionReference flashcardCollectionsColRef;

    public DBhelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userDocRef = db.collection(Constants.COL_USERS)
                .document(user.getUid());
        flashcardCollectionsColRef = userDocRef.collection(Constants.COL_FLASHCARD_COLLECTIONS);
    }

    public FirebaseUser getUser() {
        return user;
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
}

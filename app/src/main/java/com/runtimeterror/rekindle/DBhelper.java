package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

    public void removeMemberFromThread(
            String threadID,
            String userID,
            OnCompleteListener<Void> threadUpdateListener,
            OnCompleteListener<Void> userUpdateListener) {
        //remove userID from thread's member array
        getThreadDocRef(threadID)
                .update(Constants.FIELD_MEMBERS, FieldValue.arrayRemove(userID))
                .addOnCompleteListener(threadUpdateListener);
        //remove threadID on user's threads array
        getUserDocRef(userID)
                .update(Constants.COL_THREADS, FieldValue.arrayRemove(threadID))
                .addOnCompleteListener(userUpdateListener);
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

    public DocumentReference getUserDocRef(String userID) {
        return usersColRef.document(userID);
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

    public DocumentReference getThreadDocRef(String threadID) {
        return getThreadsColRef().document(threadID);
    }

    public CollectionReference getThreadFlashcardsColRef(String threadID) {
        return getThreadsColRef().document(threadID).collection(Constants.COL_FLASHCARD_LIST);
    }

    public CollectionReference getMessagesColRef(String threadID) {
        return getThreadDocRef(threadID).collection(Constants.COL_MESSAGES);
    }
}

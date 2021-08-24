package com.runtimeterror.rekindle;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBhelper {
    private final FirebaseFirestore db;

    private final CollectionReference collections_users;

    public DBhelper() {
        db = FirebaseFirestore.getInstance();
        collections_users = db.collection(Constants.COL_USERS);
    }

    public void addUser(User user) {
        //add user to database
        //if the user exists, update it
        collections_users
                .document(user.getUserID())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "User registered: " + user.getUserID());
                        } else {
                            Log.w(Constants.TAG, "User not registered.", task.getException());
                        }
                    }
                });
    }
}

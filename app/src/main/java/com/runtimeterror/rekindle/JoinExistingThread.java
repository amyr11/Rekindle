package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.List;

public class JoinExistingThread extends AppCompatActivity {
    private EditText threadCode;
    private TextView joinButton, cancelButton;
    private DBhelper db = new DBhelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_existing_thread);
        threadCode = findViewById(R.id.thread_code);
        joinButton = findViewById(R.id.button_join);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String threadCodeString = threadCode.getText().toString();
                if (!threadCodeString.isEmpty()) {
                    onJoin(threadCodeString);
                    ThreadsFragment.allowRefresh();
                } else {
                    Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onJoin(String threadCodeString) {
        checkIfAlreadyMember(threadCodeString);
    }

    private void joinThread(String threadCodeString) {
        //add user to members array field of the thread
        db.getThreadsColRef()
                .document(threadCodeString)
                .update(Constants.FIELD_MEMBERS,
                        FieldValue.arrayUnion(db.getUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //increment user's thread count
                            db.getUserDocRef()
                                    .update(Constants.FIELD_THREAD_COUNT, FieldValue.increment(1));
                            CreateThread.finishActivity();
                            finish();
                            Toast.makeText(getApplicationContext(), "Joined thread.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot find thread.", Toast.LENGTH_SHORT).show();
                            Log.w(Constants.TAG, "Failed to join thread", task.getException());
                        }
                    }
                });
    }

    private void checkIfAlreadyMember(String threadCodeString) {
        db.getThreadsColRef()
                .document(threadCodeString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            RekindleThread thread = task.getResult().toObject(RekindleThread.class);
                            boolean alreadyAMember= thread.getMembers().contains(db.getUser().getUid());
                            if (!alreadyAMember) {
                                joinThread(threadCodeString);
                            } else {
                                Toast.makeText(getApplicationContext(), "You are already on this thread!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(Constants.TAG, "Failed to join thread CHECK", task.getException());
                        }
                    }
                });
    }
}
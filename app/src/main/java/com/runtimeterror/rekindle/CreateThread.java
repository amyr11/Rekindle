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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateThread extends AppCompatActivity {
    private EditText threadName;
    private TextView joinAThreadButton, saveButton, cancelButton;
    private UserInfo user;
    private DBhelper db = new DBhelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);
        threadName = findViewById(R.id.threadName);
        joinAThreadButton = findViewById(R.id.jatNote);
        joinAThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open join thread activity
            }
        });
        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String threadNameString = threadName.getText().toString();
                if (!threadNameString.isEmpty()) {
                    onSave(threadNameString);
                    ThreadsFragment.allowRefresh();
                    finish();
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

    private void onSave(String threadNameString) {
        getUser();
        //upload thread to db
        RekindleThread rekindleThread = new RekindleThread(
                new Date(),
                threadNameString,
                FlashcardCollection.selectRandTheme(),
                db.getUser().getUid()
        );
        db.getThreadsColRef()
                .add(rekindleThread)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            //update user thread count
                            Map<String, Object> changes = new HashMap<>();
                            changes.put(Constants.FIELD_THREAD_COUNT, user.getThreadCount() + 1);
                            db.getUserDocRef().update(changes);
                            Log.d(Constants.TAG, "Thread successfully created");
                        } else {
                            Log.w(Constants.TAG, "Thread creation failed", task.getException());
                        }
                    }
                });
    }

    private void getUser() {
        db.getUserDocRef()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().toObject(UserInfo.class);
                        }
                    }
                });
    }
}
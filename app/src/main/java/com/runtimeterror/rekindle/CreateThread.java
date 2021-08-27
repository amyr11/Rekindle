package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateThread extends AppCompatActivity {
    private EditText threadName;
    private TextView joinAThreadButton, saveButton, cancelButton;
    private DBhelper db = new DBhelper();
    private static boolean finishActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);
        threadName = findViewById(R.id.threadName);
        joinAThreadButton = findViewById(R.id.jatNote);
        joinAThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinExistingThread.class);
                startActivity(intent);
            }
        });
        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String threadNameString = threadName.getText().toString();
                if (!threadNameString.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Creating thread...", Toast.LENGTH_SHORT).show();
                    onSave(threadNameString);
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

    private void onSave(String threadNameString) {
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
                            //increment user's thread count
                            db.getUserDocRef()
                                    .update(Constants.FIELD_THREAD_COUNT, FieldValue.increment(1));
                            //open copy thread code activity
                            Intent intent = new Intent(getApplicationContext(), CopyThreadCode.class);
                            intent.putExtra("threadCode", task.getResult().getId());
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(Constants.TAG, "Thread creation failed", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (finishActivity) {
            finishActivity = false;
            finish();
        }
    }

    public static void finishActivity() {
        finishActivity = true;
    }
}
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class CreateFlashcardCollection extends AppCompatActivity {
    protected FirebaseFirestore db;
    protected EditText colNameEditText;
    protected TextView saveButton, cancelButton;
    protected String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_collection);
        db = FirebaseFirestore.getInstance();
        userUID = getIntent().getStringExtra("userUID");
        viewsInit();

    }

    protected void viewsInit() {
        colNameEditText = findViewById(R.id.collectionName);
        saveButton = findViewById(R.id.button_save);
        cancelButton = findViewById(R.id.button_cancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleFull = colNameEditText.getText().toString();
                if (!titleFull.isEmpty()) {
                    onSave(titleFull);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addCollection(FlashcardCollection collection) {
        db.collection(Constants.COL_USERS)
                .document(userUID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .add(collection)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Collection successfully added/modified.");
                        } else {
                            Log.d(Constants.TAG, "Collection cannot be added/modified");
                        }
                    }
                });
    }

    //Override
    protected void onSave(String titleFull) {
        FlashcardCollection collection = new FlashcardCollection(
                new Date(),
                titleFull,
                FlashcardCollection.generateAbbr(titleFull),
                FlashcardCollection.selectRandTheme()
        );
        addCollection(collection);
        HomeFragment.allowRefresh();
    }
}
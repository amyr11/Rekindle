package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateFlashcardCollection extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText colNameEditText;
    private TextView saveButton, cancelButton;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_collection);

        db = FirebaseFirestore.getInstance();
        userUID = getIntent().getStringExtra("userUID");
        colNameEditText = findViewById(R.id.collectionName);
        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleFull = colNameEditText.getText().toString();
                FlashcardCollection collection = new FlashcardCollection(
                        titleFull,
                        FlashcardCollection.generateAbbr(titleFull),
                        FlashcardCollection.selectRandTheme()
                );
                addCollection(collection);
                HomeFragment.allowRefresh();
                finish();
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

    private void addCollection(FlashcardCollection collection) {
        db.collection(Constants.COL_USERS)
                .document(userUID)
                .collection(Constants.COL_FLASHCARDS)
                .add(collection)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Collection successfully added.");
                        } else {
                            Log.d(Constants.TAG, "Collection cannot be added");
                        }
                    }
                });
    }
}
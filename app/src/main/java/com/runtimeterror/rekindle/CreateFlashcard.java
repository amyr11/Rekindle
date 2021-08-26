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
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.protobuf.TimestampProto;

import java.util.Date;

public class CreateFlashcard extends AppCompatActivity {
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected EditText questionEditText, answerEditText;
    protected TextView saveButton, cancelButton;
    private String collectionID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_personal);

        questionEditText = findViewById(R.id.question);
        answerEditText = findViewById(R.id.answer);
        saveButton = findViewById(R.id.button_save);
        cancelButton = findViewById(R.id.button_cancel);
        viewsInit();
    }

    //Override
    private void viewsInit() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString();
                String answer = answerEditText.getText().toString();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    Flashcard flashcard = new Flashcard(question, answer, new Date());
                    addToCollection(flashcard);
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

    //Override
    private CollectionReference getSubCollection() {
        collectionID = getIntent().getStringExtra("collectionID");
        userID = getIntent().getStringExtra("userID");
        return db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST);
    }

    private void addToCollection(Flashcard flashcard) {
        getSubCollection()
                .add(flashcard)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Flashcard successfully added");
                        } else {
                            Log.d(Constants.TAG, "Flashcard cannot be added.", task.getException());
                        }
                    }
                });
    }
}
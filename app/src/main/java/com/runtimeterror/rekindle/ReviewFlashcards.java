package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewFlashcards extends AppCompatActivity {
    private DBhelper db = new DBhelper();

    private View progressBar;
    private TextView collectionTitle, cardContent, flipButton,
            notMemorizedCount, memorizedCount, memorizedWellCount, resetButton;
    private CardView cardContainer;
    private ImageButton wrongButton, correctButton, backButton;
    private View answerChecker;

    private String collectionId;

    private FlashcardCollection collection;
    private Flashcard currentFlashcard;
    private LeitnerManager leitnerManager;

    private final int CARD_FACE_QUESTION = 0;
    private final int CARD_FACE_ANSWER = 1;
    private int cardState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_flashcards);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        getIntents();
        loadCollectionInfo();
        viewsInit();
    }

    private void getIntents() {
        collectionId = getIntent().getStringExtra("collectionID");
    }

    protected void viewsInit() {
        resetButton = ViewUtils.getHeaderTextButton(this, "Reset");
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leitnerManager.reset();
                updateCardContent();
                updateMemorizedCounters();
            }
        });

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collectionTitle = findViewById(R.id.collectionSet);

        cardContainer = findViewById(R.id.mainFlashcard);
        cardContent = findViewById(R.id.text_content);

        flipButton = findViewById(R.id.button_flip);
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip();
            }
        });

        answerChecker = findViewById(R.id.answerChecker);
        wrongButton = findViewById(R.id.button_wrong);
        wrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leitnerManager.updateCardBoxNumber(currentFlashcard, false);
                updateCardContent();
                updateMemorizedCounters();
            }
        });
        correctButton = findViewById(R.id.button_correct);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leitnerManager.updateCardBoxNumber(currentFlashcard, true);
                updateCardContent();
                updateMemorizedCounters();
            }
        });

        notMemorizedCount = findViewById(R.id.nmCounter);
        memorizedCount = findViewById(R.id.mCounter);
        memorizedWellCount = findViewById(R.id.mwCounter);
    }

    private void updateCardContent() {
        currentFlashcard = leitnerManager.getNextCard();
        showQuestion();
        answerChecker.setVisibility(View.GONE);
    }

    private void flip() {
        if (cardState == CARD_FACE_QUESTION) {
            showAnswer();
        } else if (cardState == CARD_FACE_ANSWER) {
            showQuestion();
        }
        answerChecker.setVisibility(View.VISIBLE);
    }

    private void showQuestion() {
        setCardFace(CARD_FACE_QUESTION);
        cardContent.setText(currentFlashcard.getQuestion());
    }

    private void showAnswer() {
        setCardFace(CARD_FACE_ANSWER);
        cardContent.setText(currentFlashcard.getAnswer());
    }

    private void setCardFace(int face) {
        int color = 0;
        if (face == CARD_FACE_QUESTION) {
            color = ContextCompat.getColor(this, R.color.purple_dark);
        } else if (face == CARD_FACE_ANSWER) {
            color = ContextCompat.getColor(this, R.color.purple_light);
        }
        cardContainer.setCardBackgroundColor(color);
        cardState = face;
    }

    private void updateMemorizedCounters() {
        notMemorizedCount.setText(String.valueOf(leitnerManager.getBoxSize(1)));
        memorizedCount.setText(String.valueOf(leitnerManager.getBoxSize(2)));
        memorizedWellCount.setText(String.valueOf(leitnerManager.getBoxSize(3)));
    }

    private void loadCollectionInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getFlashcardCollectionDocRef(collectionId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    collection = doc.toObject(FlashcardCollection.class);
                                    collection.setId(doc.getId());
                                    //pass a flashcard collection reference to leitner manager
                                    leitnerManager = new LeitnerManager(collection);
                                    collectionTitle.setText(collection.getTitleFull());
                                    loadFlashcards();
                                    Log.d(Constants.TAG, "Collection info successfully loaded");
                                } else {
                                    Log.w(Constants.TAG, "Collection info loading failed", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }

    private void loadFlashcards() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getFlashcardListColRef(collectionId)
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot doc: task.getResult()) {
                                            Flashcard flashcard = docToFlashcard(doc);
                                            flashcard.setId(doc.getId());
                                            //populate boxes in leitner manager
                                            leitnerManager.populateBoxes(flashcard.getBoxNumber(), flashcard);
                                        }
                                        Log.d(Constants.TAG, "Flashcards successfully loaded");
                                        leitnerManager.setFlashcardsToFocus();
                                        updateCardContent();
                                        updateMemorizedCounters();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Log.d(Constants.TAG, "Collection is empty");
                                        Toast.makeText(getApplicationContext(), "Flashcard collection is empty.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } else {
                                    Log.w(Constants.TAG, "Flashcards loading failed", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }

    protected Flashcard docToFlashcard(QueryDocumentSnapshot doc) {
        return doc.toObject(Flashcard.class);
    }
}
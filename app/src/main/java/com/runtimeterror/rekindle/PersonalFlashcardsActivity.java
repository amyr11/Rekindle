package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PersonalFlashcardsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ImageButton backButton;
    private TextView collectionTitle, reviewButton;
    private ImageButton editButton, removeButton;
    private View progressBar;
    private View addFlashCardButton;
    private RecyclerView flashcardsRecyclerView;
    private FlashcardsAdapter flashcardsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String collectionID;
    private FlashcardCollection flashcardCollection;
    private String userID;
    private List<Flashcard> flashcardList;
    private boolean colInfoLoaded = false, listLoaded = false;
    private CollectionReference fCollectionsRef, flashcardsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_flashcards);

        collectionID = getIntent().getStringExtra("collectionID");
        userID = getIntent().getStringExtra("userID");
        db = FirebaseFirestore.getInstance();
        fCollectionsRef = db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS);
        flashcardsRef = fCollectionsRef.document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST);
        backButton = findViewById(R.id.button_back);
        progressBar = findViewById(R.id.progress_bar);
        collectionTitle = findViewById(R.id.collectionSet);
        reviewButton = ViewUtils.getHeaderTextButton(this, "Review");
        editButton = findViewById(R.id.pencil);
        removeButton = findViewById(R.id.remove);
        addFlashCardButton = findViewById(R.id.add_flashcard);
        flashcardsRecyclerView = findViewById(R.id.flashcards_recyclerview);
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewsInit();
    }

    private void viewsInit() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addFlashCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateFlashcard.class);
                intent.putExtra("collectionID", collectionID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = editButton.getContext();
                Intent intent = new Intent(context, EditFlashcardCollection.class);
                intent.putExtra("collectionID", collectionID);
                intent.putExtra("collectionName", flashcardCollection.getTitleFull());
                intent.putExtra("userUID", userID);
                context.startActivity(intent);
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCollection();
                HomeFragment.allowRefresh();
                finish();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCollectionInfo();
                loadFlashcards();
            }
        }, 0);
    }

    private void recyclerViewInit() {
        flashcardsAdapter = new FlashcardsAdapter(flashcardList, collectionID, userID);
        flashcardsRecyclerView.setAdapter(flashcardsAdapter);
        flashcardsRecyclerView.setLayoutManager(layoutManager);
    }

    private void removeCollection() {
        //remove all flashcards
        for (Flashcard flashcard : flashcardList) {
            flashcardsRef.document(flashcard.getId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.w(Constants.TAG, "Document deleted.");
                            } else {
                                Log.w(Constants.TAG, "Deletion failed.", task.getException());
                            }
                        }
                    });
        }

        //remove collection
        fCollectionsRef.document(collectionID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Collection successfully deleted.");
                        } else {
                            Log.w(Constants.TAG, "Parent collection deletion failed.", task.getException());
                        }
                    }
                });
    }

    private void loadCollectionInfo() {
        db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .document(collectionID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            FlashcardCollection collection = task.getResult()
                                    .toObject(FlashcardCollection.class);
                            flashcardCollection = collection;
                            collectionTitle.setText(collection.getTitleFull());
                            colInfoLoaded = true;
                            progressBar.setVisibility(listLoaded ? View.GONE : View.VISIBLE);
                            Log.d(Constants.TAG, "Collection info loaded.");
                        } else {
                            Log.w(Constants.TAG, "Collection info failed to load.", task.getException());
                        }
                    }
                });
    }

    private void loadFlashcards() {
        flashcardList = new ArrayList<>();
        db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Flashcard flashcard = doc.toObject(Flashcard.class);
                                flashcard.setId(doc.getId());
                                flashcardList.add(flashcard);
                            }
                            recyclerViewInit();
                            listLoaded = true;
                            progressBar.setVisibility(colInfoLoaded ? View.GONE : View.VISIBLE);
                            Log.d(Constants.TAG, "Flashcards loaded.");
                        } else {
                            Log.w(Constants.TAG, "Flashcards failed to load.", task.getException());
                        }
                    }
                });
    }
}
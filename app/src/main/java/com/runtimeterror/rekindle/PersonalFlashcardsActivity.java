package com.runtimeterror.rekindle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalFlashcardsActivity extends AppCompatActivity {
    private TextView collectionTitle, reviewButton;
    private ImageView editButton, removeButton;
    private View addFlashCardButton;
    private RecyclerView flashcardsRecyclerView;
    private FlashcardsAdapter flashcardsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_flashcards);

        collectionTitle = findViewById(R.id.collectionSet);
        reviewButton = ViewUtils.getHeaderTextButton(this, "Review");
        editButton = findViewById(R.id.pencil);
        removeButton = findViewById(R.id.remove);
        addFlashCardButton = findViewById(R.id.add_flashcard);
        flashcardsRecyclerView = findViewById(R.id.flashcards_recyclerview);
        layoutManager = new LinearLayoutManager(this);
    }

    private void recyclerViewInit() {
        //TODO: initialize recyclerview
    }

    private void loadFlashcards() {
        //TODO: load from db
    }
}
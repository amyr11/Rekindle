package com.runtimeterror.rekindle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder> {
    private List<Flashcard> flashcardList = new ArrayList<>();
    private String collectionID;
    private String userID;

    public FlashcardsAdapter(List<Flashcard> flashcards, String collectionID, String userID) {
        this.flashcardList = flashcards;
        this.collectionID = collectionID;
        this.userID = userID;
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        private ImageView editButton, removeButton;
        private TextView questionTextView, answerTextView;
        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.pencil);
            removeButton = itemView.findViewById(R.id.remove);
            questionTextView = itemView.findViewById(R.id.fTitle);
            answerTextView = itemView.findViewById(R.id.fDesc);
        }
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard current = flashcardList.get(position);
        holder.questionTextView.setText(current.getQuestion());
        holder.answerTextView.setText(current.getAnswer());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.editButton.getContext();
                Intent intent = new Intent(context, EditFlashcard.class);
                intent.putExtra("userID", userID);
                intent.putExtra("collectionID", collectionID);
                intent.putExtra("flashcardID", current.getId());
                intent.putExtra("question", current.getQuestion());
                intent.putExtra("answer", current.getAnswer());
                context.startActivity(intent);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Remove flashcard
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }
}

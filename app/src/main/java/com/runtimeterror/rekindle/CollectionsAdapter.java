package com.runtimeterror.rekindle;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder> {
    private List<FlashcardCollection> flashcardCollectionList;

    public CollectionsAdapter(List<FlashcardCollection> list) {
        this.flashcardCollectionList = list;
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder {
        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

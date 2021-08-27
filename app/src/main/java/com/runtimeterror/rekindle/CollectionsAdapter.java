package com.runtimeterror.rekindle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADD_COLLECTION = 0;
    private static final int COLLECTION = 1;

    private List<FlashcardCollection> flashcardCollectionList;

    public CollectionsAdapter(List<FlashcardCollection> list) {
        this.flashcardCollectionList = list;
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleAbbrText, titleFullText, reviewButton;
        private CardView container;
        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleAbbrText = itemView.findViewById(R.id.collectionNameAbbr);
            titleFullText = itemView.findViewById(R.id.collectionName);
            reviewButton = itemView.findViewById(R.id.button_review);
            container = itemView.findViewById(R.id.collection_container);
        }
    }

    public static class AddCollectionViewHolder extends RecyclerView.ViewHolder {
        private View container;
        public AddCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.add_collection_container);
        }
    }


    public void bindCollection(RecyclerView.ViewHolder holder, int position) {
        CollectionViewHolder collectionViewHolder = (CollectionViewHolder) holder;
        FlashcardCollection current = flashcardCollectionList.get(position - 1);
        collectionViewHolder.container.setCardBackgroundColor(
                ViewUtils.getCardColor(collectionViewHolder.container.getContext(), current.getTheme())
        );
        collectionViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open all flashcards activity
                Context context = collectionViewHolder.container.getContext();
                Intent intent = new Intent(context, PersonalFlashcardsActivity.class);
                intent.putExtra("collectionID", current.getId());
                context.startActivity(intent);
            }
        });
        collectionViewHolder.titleAbbrText.setTextColor(
                ViewUtils.getAbbrColor(collectionViewHolder.titleAbbrText.getContext(), current.getTheme())
        );
        collectionViewHolder.titleAbbrText.setText(current.getTitleAbbr());
        collectionViewHolder.titleFullText.setText(
                ViewUtils.limitChars(current.getTitleFull(), Constants.COLLECTIONS_CHAR_LIMIT));
        collectionViewHolder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = collectionViewHolder.reviewButton.getContext();
                Intent intent = new Intent(context, ReviewFlashcards.class);
                intent.putExtra("collectionID", current.getId());
                context.startActivity(intent);
            }
        });
    }

    public void bindAddCollectionButton(RecyclerView.ViewHolder holder) {
        AddCollectionViewHolder addCollectionViewHolder = (AddCollectionViewHolder) holder;
        addCollectionViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = addCollectionViewHolder.container.getContext();
                Intent intent = new Intent(context, CreateFlashcardCollection.class);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_COLLECTION) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_collection, parent, false);
            return new AddCollectionViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection_flashcard, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ADD_COLLECTION) {
            bindAddCollectionButton(holder);
        } else {
            bindCollection(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if (position == 0) {
            return ADD_COLLECTION;
        }
        return COLLECTION;
    }

    @Override
    public int getItemCount() {
        return flashcardCollectionList.size() + 1;
    }
}

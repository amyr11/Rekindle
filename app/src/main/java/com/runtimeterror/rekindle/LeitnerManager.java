package com.runtimeterror.rekindle;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LeitnerManager {
    DBhelper db = new DBhelper();

    private FlashcardCollection flashcardCollection;
    Map<String, Flashcard> box1, box2, box3;
    private Stack<Flashcard> flashcardsToFocus;


    public LeitnerManager(FlashcardCollection collection) {
        this.flashcardCollection = collection;
        this.box1 = new HashMap<>();
        this.box2 = new HashMap<>();
        this.box3 = new HashMap<>();
        this.flashcardsToFocus = new Stack<>();
    }

    public void populateBoxes(int boxNumber, Flashcard flashcard) {
        switch (boxNumber) {
            case 1:
                box1.put(flashcard.getId(), flashcard);
                break;
            case 2:
                box2.put(flashcard.getId(), flashcard);
                break;
            case 3:
                box3.put(flashcard.getId(), flashcard);
                break;
        }
    }

    public void reset() {
        List<Flashcard> flashcards = getAllFlashcards();
        box1.clear();
        box2.clear();
        box3.clear();
        for (Flashcard flashcard : flashcards) {
            flashcard.setShow(true);
            flashcard.setBoxNumber(1);
            box1.put(flashcard.getId(), flashcard);
            updateFlashcard(flashcard);
        }
        flashcardCollection.setBox2State(0);
        flashcardCollection.setBox3State(0);
        updateCollection();
        flashcardsToFocus.clear();
        setFlashcardsToFocus();
    }

    public void updateCardBoxNumber(Flashcard flashcard, boolean isAnswerCorrect) {
        //update the card box number based on it's previous box number
        //if the answer is correct, moved to next box
        //else move to box 1
        int previousBoxNumber = flashcard.getBoxNumber();
        if (isAnswerCorrect) {
            if (previousBoxNumber < 3)
                flashcard.setBoxNumber(flashcard.getBoxNumber() + 1);
        } else {
            flashcard.setBoxNumber(1);
        }
        //don't show this card until next session
        flashcard.setShow(false);
        //remove the flashcard from previous box
        switch (previousBoxNumber) {
            case 1:
                box1.remove(flashcard.getId());
                break;
            case 2:
                box2.remove(flashcard.getId());
                break;
            case 3:
                box3.remove(flashcard.getId());
                break;
        }
        //move the flashcard to next box number
        int nextBoxNumber = flashcard.getBoxNumber();
        switch (nextBoxNumber) {
            case 1:
                box1.put(flashcard.getId(), flashcard);
                break;
            case 2:
                box2.put(flashcard.getId(), flashcard);
                break;
            case 3:
                box3.put(flashcard.getId(), flashcard);
                break;
        }
        //remove the flashcard from the flashCardsToFocus map
        flashcardsToFocus.remove(flashcard.getId());
        updateFlashcard(flashcard);
    }

    //should be called after populating all boxes
    public void setFlashcardsToFocus() {
        //find all flashcards to focus based on boxes state
        if (flashcardCollection.getBox1State() == Constants.BOX1_THRESHOLD && !box1.isEmpty())  {
            populateFlashcardsToFocusFromBox(1);
        }
        if (flashcardCollection.getBox2State() == Constants.BOX2_THRESHOLD || box1.isEmpty()) {
            populateFlashcardsToFocusFromBox(2);
        }
        if (flashcardCollection.getBox3State() == Constants.BOX3_THRESHOLD ||
                (box2.isEmpty() && box1.isEmpty())) {
            populateFlashcardsToFocusFromBox(3);
        }
    }

    private void populateFlashcardsToFocusFromBox(int boxNumber) {
        List<Flashcard> flashcards = iterateBox(boxNumber);
        for (Flashcard flashcard : flashcards) {
            if (flashcard.isShow()) {
                flashcardsToFocus.add(flashcard);
            }
        }
    }

    private List<Flashcard> iterateBox(int boxNumber) {
        List<Flashcard> flashcards = new ArrayList<>();
        Iterator<Map.Entry<String, Flashcard>> boxIterator = getIterator(boxNumber);
        while (boxIterator.hasNext()) {
            flashcards.add(boxIterator.next().getValue());
        }
        return flashcards;
    }

    private Iterator<Map.Entry<String, Flashcard>> getIterator(int boxNumber) {
        switch (boxNumber) {
            case 1:
                return box1.entrySet().iterator();
            case 2:
                return box2.entrySet().iterator();
            case 3:
                return box3.entrySet().iterator();
        }
        return null;
    }

    private void updateBoxesState() {
        //update boxes state when flashcardsToFocus from last session is exhausted
        int box1State = flashcardCollection.getBox1State();
        int box2State = flashcardCollection.getBox2State();
        int box3State = flashcardCollection.getBox3State();
        flashcardCollection.setBox1State(incrementState(box1State, Constants.BOX1_THRESHOLD));
        flashcardCollection.setBox2State(incrementState(box2State, Constants.BOX2_THRESHOLD));
        flashcardCollection.setBox3State(incrementState(box3State, Constants.BOX3_THRESHOLD));
        resetShowForAllCards();
        //reset flashcard to focus
        setFlashcardsToFocus();
    }

    private void resetShowForAllCards() {
        flashcardsToFocus.clear();
        List<Flashcard> flashcardList = getAllFlashcards();
        box1.clear();
        box2.clear();
        box3.clear();
        for (Flashcard flashcard : flashcardList) {
            flashcard.setShow(true);
            populateBoxes(flashcard.getBoxNumber(), flashcard);
        }
    }

    private int incrementState(int before, int threshold) {
        return before == threshold ? 0 : before + 1;
    }

    private boolean isNextSession() {
        boolean nextSession = flashcardsToFocus.isEmpty();
        if (nextSession) {
            Log.d(Constants.TAG, "--------NEXT SESSION---------");
            updateCollection();
        }
        return nextSession;
    }

    public Flashcard getNextCard() {
        //pop the next flashcard on flashcards to focus stack
        if (isNextSession()) {
            updateBoxesState();
            return flashcardsToFocus.pop();
        }
        return flashcardsToFocus.pop();
    }

    public int getBoxSize(int boxNumber) {
        switch (boxNumber) {
            case 1:
                return box1.size();
            case 2:
                return box2.size();
            case 3:
                return box3.size();
        }
        return -1;
    }

    public void updateDB() {
        //get all flashcards from all boxes
        List<Flashcard> allFlashcards = getAllFlashcards();
        //update each flashcard document
        for (Flashcard flashcard : allFlashcards) {
            updateFlashcard(flashcard);
        }
        updateCollection();
    }

    private void updateCollection() {
        //update flashcard collection metadata
        Map<String, Object> changes = new HashMap<>();
        changes.put("box1State", flashcardCollection.getBox1State());
        changes.put("box2State", flashcardCollection.getBox2State());
        changes.put("box3State", flashcardCollection.getBox3State());
        db.getFlashcardCollectionDocRef(flashcardCollection.getId())
                .update(changes)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Collection metadata updated successfully");
                        } else {
                            Log.w(Constants.TAG, "Collection metadata update failed", task.getException());
                        }
                    }
                });
    }

    private void updateFlashcard(Flashcard flashcard) {
        Map<String, Object> changes = new HashMap<>();
        changes.put("show", flashcard.isShow());
        changes.put("boxNumber", flashcard.getBoxNumber());
        db.getFlashcardDocRef(flashcardCollection.getId(), flashcard.getId())
                .update(changes)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Flashcard metadata updated successfully");
                        } else {
                            Log.w(Constants.TAG, "Flashcard metadata update failed", task.getException());
                        }
                    }
                });
    }

    private List<Flashcard> getAllFlashcards() {
        //get all flashcards from all boxes
        List<Flashcard> allFlashcards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            allFlashcards.addAll(iterateBox(i));
        }
        return allFlashcards;
    }
}

package com.runtimeterror.rekindle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private DBhelper db;
    private RecyclerView collectionsRecyclerview;
    private CollectionsAdapter collectionsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View progressBar;
    private List<FlashcardCollection>flashcardCollections;
    private static boolean allowRfresh;

    public static void allowRefresh() {
        allowRfresh = true;
    }

    private void viewsInit(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        collectionsRecyclerview = view.findViewById(R.id.flashcard_collections);
        layoutManager = new GridLayoutManager(
                view.getContext(), 2, GridLayoutManager.VERTICAL, false);
        loadCollections();
    }

    private void recyclerViewInit() {
        collectionsAdapter = new CollectionsAdapter(flashcardCollections);
        collectionsRecyclerview.setAdapter(collectionsAdapter);
        collectionsRecyclerview.setLayoutManager(layoutManager);
        collectionsRecyclerview.setNestedScrollingEnabled(false);
    }

    private void loadCollections() {
        progressBar.setVisibility(View.VISIBLE);
        flashcardCollections = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getFlashcardCollectionsColRef()
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        FlashcardCollection collection = doc.toObject(FlashcardCollection.class);
                                        collection.setId(doc.getId());
                                        flashcardCollections.add(collection);
                                    }
                                    recyclerViewInit();
                                    progressBar.setVisibility(View.GONE);
                                    Log.d(Constants.TAG, "Flashcards collections loaded.");
                                } else {
                                    Log.w(Constants.TAG, "Flashcards collections retrieval failed.", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DBhelper();
        viewsInit(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allowRfresh) {
            allowRfresh = false;
            loadCollections();
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
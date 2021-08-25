package com.runtimeterror.rekindle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView collectionsRecyclerview;
    private CollectionsAdapter collectionsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View progressBar;

    private void viewsInit(View view) {
        //TODO:
        progressBar = view.findViewById(R.id.progress_bar);
        loadCollections(view);
    }

    private void recyclerViewInit(View view) {
        collectionsRecyclerview = view.findViewById(R.id.flashcard_collections);
        List<FlashcardCollection> flashcardCollections = new ArrayList<>();
        collectionsAdapter = new CollectionsAdapter(flashcardCollections);
        layoutManager = new GridLayoutManager(
                view.getContext(), 2, GridLayoutManager.VERTICAL, false);
        collectionsRecyclerview.setAdapter(collectionsAdapter);
        collectionsRecyclerview.setLayoutManager(layoutManager);
        collectionsRecyclerview.setNestedScrollingEnabled(false);
    }

    private void loadCollections(View view) {
        progressBar.setVisibility(View.VISIBLE);
        //TODO: load from firebase
        recyclerViewInit(view);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsInit(view);
    }
}
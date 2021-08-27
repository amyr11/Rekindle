package com.runtimeterror.rekindle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThreadsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThreadsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ThreadsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThreadsFragment.
     */

    public static ThreadsFragment newInstance(String param1, String param2) {
        ThreadsFragment fragment = new ThreadsFragment();
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
        return inflater.inflate(R.layout.fragment_threads, container, false);
    }

    private View progressBar;
    private ImageButton addThreadButton;
    private RecyclerView threadsRecyclerView;
    private ThreadsAdapter threadsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsInit(view);
    }

    private void viewsInit(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        addThreadButton = view.findViewById(R.id.add_thread);
        addThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open add thread activity
            }
        });
        threadsRecyclerView = view.findViewById(R.id.threads_recyclerview);
        layoutManager = new LinearLayoutManager(view.getContext());
        loadThreads();
    }

    private void recyclerViewInit(List<RekindleThread> threadList) {
        threadsAdapter = new ThreadsAdapter(threadList);
        threadsRecyclerView.setAdapter(threadsAdapter);
        threadsRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadThreads() {
        progressBar.setVisibility(View.VISIBLE);
        DBhelper db = new DBhelper();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getThreadsColRef()
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<RekindleThread> threadList = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        RekindleThread rekindleThread = doc.toObject(RekindleThread.class);
                                        rekindleThread.setId(doc.getId());
                                        threadList.add(rekindleThread);
                                    }
                                    recyclerViewInit(threadList);
                                    progressBar.setVisibility(View.GONE);
                                    Log.d(Constants.TAG, "Threads loaded successfully");
                                }
                                else {
                                    Log.d(Constants.TAG, "Threads loading failed", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }
}
package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.backup.BackupDataInput;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class MembersPanel extends AppCompatActivity {
    private RecyclerView membersRecyclerview;
    private MembersAdapter membersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View progressBar;
    private String threadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_panel);
        threadID = getIntent().getStringExtra("threadID");
        ViewUtils.setHeader(this, "Members");
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        membersRecyclerview = findViewById(R.id.members_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        loadMembers(threadID);
    }

    private void recyclerviewInit(List<UserInfo> members) {
        membersAdapter = new MembersAdapter(this, members);
        membersRecyclerview.setAdapter(membersAdapter);
        membersRecyclerview.setLayoutManager(layoutManager);
    }

    private void loadMembers(String threadID) {
        DBhelper db = new DBhelper();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getUsersColRef()
                        .whereArrayContains(Constants.COL_THREADS, threadID)
                        .orderBy(Constants.FIELD_USERNAME, Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<UserInfo> members = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        UserInfo member = doc.toObject(UserInfo.class);
                                        member.setUserID(doc.getId());
                                        members.add(member);
                                    }
                                    recyclerviewInit(members);
                                    progressBar.setVisibility(View.GONE);
                                    Log.d(Constants.TAG, "Members loaded successfully");
                                } else {
                                    Log.d(Constants.TAG, "Members loading failed", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }
}
package com.runtimeterror.rekindle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    private DBhelper db = new DBhelper();
    private View progressBar;
    private ImageView profilePhoto, silverMedal, goldMedal, bronzeMedal;
    private TextView usernameText, threadCntText, pointsText,
            silverCntText, goldCntText, bronzeCntText;
    private ImageButton settingsButton;

    private void viewsInit(View view) {
        settingsButton = view.findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to settings
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        progressBar = view.findViewById(R.id.progress_bar);
        profilePhoto = view.findViewById(R.id.profile_pic);
        usernameText = view.findViewById(R.id.username);
        threadCntText = view.findViewById(R.id.numberOfThreads);
        pointsText = view.findViewById(R.id.numberOfPoints);
        silverCntText = view.findViewById(R.id.silverCount);
        goldCntText = view.findViewById(R.id.goldCount);
        bronzeCntText = view.findViewById(R.id.bronzeCount);
        silverMedal = view.findViewById(R.id.secondPlace);
        goldMedal = view.findViewById(R.id.firstPlace);
        bronzeMedal = view.findViewById(R.id.thirdPlace);

        //get user info and set views accordingly
        loadUser();
    }

    private void loadUser() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getUserDocRef()
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                                    assert userInfo != null;
                                    new DownloadSpriteTask().execute(userInfo.getPhotoURL());
                                    usernameText.setText(userInfo.getUsername());
                                    threadCntText.setText(String.valueOf(userInfo.getThreads().size()));
                                    pointsText.setText(String.valueOf(userInfo.getPoints()));
                                    setAchievements(userInfo.getGoldCnt(), userInfo.getSilverCnt(),
                                            userInfo.getBronzeCnt());
                                    progressBar.setVisibility(View.GONE);
                                    Log.d(Constants.TAG, "User info loaded.");
                                } else {
                                    Log.w(Constants.TAG, "User info retrieval failed", task.getException());
                                }
                            }
                        });
            }
        }, 0);
    }

    private void setAchievements(int gold, int silver, int bronze) {
        setAchievementMedalAndText(gold, goldMedal, goldCntText);
        setAchievementMedalAndText(silver, silverMedal, silverCntText);
        setAchievementMedalAndText(bronze, bronzeMedal, bronzeCntText);
    }

    private void setAchievementMedalAndText(int count, ImageView medal, TextView text) {
        if (count > 0) {
            medal.setAlpha((float) 1);
            text.setAlpha(1);
        } else {
            medal.setAlpha((float) 0.2);
            text.setAlpha((float) 0.2);
        }
        text.setText(achievementToText(count));
    }

    private String achievementToText(int count) {
        if (count > 0)
            return count > 1 ? count + "\nThreads" : count + "\nThread";
        return "N/A";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsInit(view);
    }

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (IOException e) {
                Log.e(Constants.TAG, "Download image error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // load the bitmap into the ImageView!
            profilePhoto.setImageBitmap(bitmap);
        }
    }
}
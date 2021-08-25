package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SwitchCompat competitiveSwitch;
    private TextView signOutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;

        ViewUtils.setHeader(this, "Settings");
        signOutView = findViewById(R.id.sign_out);
        competitiveSwitch = ViewUtils.getSwitchView(this, R.id.competitive_option, "Competitive mode");
        setCompetitiveSwitchChecked();
        competitiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCompetitive(isChecked);
            }
        });
        signOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign out user and go back to sign in activity
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signed out.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void updateCompetitive(boolean status) {
        //update user competitive field
        Map<String, Object> competitive = new HashMap<>();
        competitive.put("competitive", status);
        db.collection(Constants.COL_USERS)
                .document(user.getUid())
                .update(competitive);
    }

    public void setCompetitiveSwitchChecked() {
        db.collection(Constants.COL_USERS)
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            assert userInfo != null;
                            competitiveSwitch.setChecked(userInfo.isCompetitive());
                        } else {
                            Log.w(Constants.TAG, "User retrieval failed.", task.getException());
                        }
                    }
                });
    }
}
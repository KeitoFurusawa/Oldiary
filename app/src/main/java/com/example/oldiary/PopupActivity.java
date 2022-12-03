package com.example.oldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class PopupActivity extends AppCompatActivity {
    private static final String TAG = "prof";
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String userId;
    private String userName;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setOnClick();
        getUserId();
        setUserName();
        setComment();
    }

    protected void getUserId() {
        userId = preference.getString("UserID", "");
    }

    private void setUserName() {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    userName = String.valueOf(task.getResult().getValue());
                    TextView textUserName = findViewById(R.id.textViewUserName);
                    textUserName.setText(userName);
                }
            }
        });
    }

    private void setComment() {
        mDatabase.child("users").child(userId).child("comment").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    comment = String.valueOf(task.getResult().getValue());
                    if (comment.equals("null")) {
                        Log.d(TAG, "the result of getting comment data is null");
                    } else {
                        TextView textComment = findViewById(R.id.comment);
                        textComment.setText(userName);
                    }
                }
            }
        });
    }

    private void setOnClick() {
        Button btnCancel = findViewById(R.id.cancel);
        Button btnNext = findViewById(R.id.next);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ProfileActivity.class);
            startActivity(intent);
        });
        btnNext.setOnClickListener(v -> {

        });
    }
}
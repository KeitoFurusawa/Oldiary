package com.example.oldiary;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReadAndWrite extends CreateActivity {
    private static final String TAG = "firebase";
    private static final String CACHE = "cache.txt";
    private  DatabaseReference mDatabase;
    private String userId;

    public ReadAndWrite() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void writeDiary(String userId, String text, String dateTime, long timeInMillis) {
        Log.d(TAG, text);
        mDatabase.child("users").child(userId).child("d_cnt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String getResult = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, getResult);
                    if (getResult.equals("null")) { //初投稿
                        Log.d(TAG, "ERROR: cnt is null");
                        mDatabase.child("users").child(userId).child("d_cnt").setValue(1);
                        //mDatabase.child("users").child(userId).child("diaries").child("d_id").setValue(userId+"1");
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+"1").child("text").setValue(text);
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+"1").child("postedAt").setValue(dateTime);
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+"1").child("timeInMillis").setValue(timeInMillis);

                    } else { // 2回目以降
                        int count = Integer.parseInt(getResult) + 1;
                        mDatabase.child("users").child(userId).child("d_cnt").setValue(count);
                        //mDatabase.child("users").child(userId).child("diaries").child("d_id").setValue(userId+count);
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+count).child("text").setValue(text);
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+count).child("postedAt").setValue(dateTime);
                        mDatabase.child("users").child(userId).child("diaries").child("d_"+userId+count).child("timeInMillis").setValue(timeInMillis);
                    }
                }
            }
        });
    }

    public void addNewUser(String phoneNumber, String password) {
        StringBuffer sb = new StringBuffer();
        sb.append("id_");
        User user = new User(phoneNumber, password);
        sb.append(phoneNumber);
        String ID = sb.toString();
        Log.d(TAG, ID);
        mDatabase.child("users").child(ID).setValue(user);
    }
}
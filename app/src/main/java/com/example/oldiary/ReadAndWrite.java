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
                        mDatabase.child("diaries").child("d_"+userId+"1").child("text").setValue(text);
                        mDatabase.child("diaries").child("d_"+userId+"1").child("postedAt").setValue(dateTime);
                        mDatabase.child("diaries").child("d_"+userId+"1").child("timeInMillis").setValue(timeInMillis);
                        mDatabase.child("diaries").child("d_"+userId+"1").child("postedBy").setValue(userId);
                        addNewDiaryPublic("d_"+userId+"1");
                        addNewDiaryPersonal(userId, "d_"+userId+"1");
                    } else { // 2回目以降
                        int count = Integer.parseInt(getResult) + 1;
                        mDatabase.child("users").child(userId).child("d_cnt").setValue(count);
                        //mDatabase.child("users").child(userId).child("diaries").child("d_id").setValue(userId+count);
                        mDatabase.child("diaries").child("d_"+userId+count).child("text").setValue(text);
                        mDatabase.child("diaries").child("d_"+userId+count).child("postedAt").setValue(dateTime);
                        mDatabase.child("diaries").child("d_"+userId+count).child("timeInMillis").setValue(timeInMillis);
                        mDatabase.child("diaries").child("d_"+userId+count).child("postedBy").setValue(userId);
                        addNewDiaryPublic("d_"+userId+count);
                        addNewDiaryPersonal(userId, "d_"+userId+count);
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

    public void addNewDiaryPublic(String diaryId) {
        mDatabase.child("diaries").child("d_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    if (value.equals("null")) { //first diary
                        mDatabase.child("diaries").child("d_idList").setValue(diaryId);
                    }
                    else {
                        StringBuffer sb = new StringBuffer(value);
                        sb.append(",").append(diaryId);
                        mDatabase.child("diaries").child("d_idList").setValue(sb.toString());
                    }
                    //Log.d(TAG, value);
                }
            }
        });

        mDatabase.child("diaries").child("dCntPublic").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    if (value.equals("null")) { //first diary on public
                        mDatabase.child("diaries").child("dCntPublic").setValue(1);
                    }
                    else { //２つ目以降
                        int cnt = Integer.parseInt(value);
                        mDatabase.child("diaries").child("dCntPublic").setValue(++cnt);
                    }
                    //Log.d(TAG, value);
                }
            }
        });
    }

    public void addNewDiaryPersonal(String userId, String diaryId) {
        mDatabase.child("users").child(userId).child("diaries").child("d_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    if (value.equals("null")) { //first diary
                        mDatabase.child("users").child(userId).child("diaries").child("d_idList").setValue(diaryId);
                    }
                    else {
                        StringBuffer sb = new StringBuffer(value);
                        sb.append(",").append(diaryId);
                        mDatabase.child("users").child(userId).child("diaries").child("d_idList").setValue(sb.toString());
                    }
                    //Log.d(TAG, value);
                }
            }
        });
    }

    public void writeReply(String srcUserId, String dstDiaryId, String text, String dateTime, long timeInMillis) {
        mDatabase.child("diaries").child(dstDiaryId).child("r_cnt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String getResult = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, getResult);
                    if (getResult.equals("null")) { //最初の返信
                        mDatabase.child("diaries").child(dstDiaryId).child("r_cnt").setValue(1);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_1").child("text").setValue(text);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_1").child("repliedAt").setValue(dateTime);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_1").child("timeInMillis").setValue(timeInMillis);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_1").child("repliedBy").setValue(srcUserId);
                        addNewReply(dstDiaryId, "r_1");

                    } else { // 2回目以降
                        int count = Integer.parseInt(getResult) + 1;
                        mDatabase.child("diaries").child(dstDiaryId).child("r_cnt").setValue(count);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_"+count).child("text").setValue(text);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_"+count).child("repliedAt").setValue(dateTime);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_"+count).child("timeInMillis").setValue(timeInMillis);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_"+count).child("repliedBy").setValue(srcUserId);
                        addNewReply(dstDiaryId, "r_"+count);
                    }
                }
            }
        });
    }

    public void addNewReply(String dstDiaryId, String replyId) {
        mDatabase.child("diaries").child(dstDiaryId).child("r_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    if (value.equals("null")) { //first reply
                        mDatabase.child("diaries").child(dstDiaryId).child("r_idList").setValue(replyId);
                    }
                    else {
                        StringBuffer sb = new StringBuffer(value);
                        sb.append(",").append(replyId);
                        mDatabase.child("diaries").child(dstDiaryId).child("r_idList").setValue(sb.toString());
                    }
                }
            }
        });
    }
}
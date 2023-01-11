package com.example.oldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class ReplyActivity extends AppCompatActivity {
    private final static String TAG = "connect";
    private DatabaseReference mDatabase;
    private String userId;
    private String dstUserId;
    private String dstUserName;
    private String dstDiaryId;
    private SharedPreferences preference;
    private int nowDNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("投稿する");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        userId =  preference.getString("UserID", "");
        getDstData();
        checkUserId();
        setOnClickCancel();
        setOnClickPost();
    }

    private void getDstData() {
        Intent i = getIntent();
        dstUserId = i.getStringExtra("dstUserID");
        dstDiaryId = i.getStringExtra("dstDiaryID");
        nowDNum = i.getIntExtra("D-NUM", -1);
        //Log.d(TAG, "Dnum: " + nowDNum);
        mDatabase.child("users").child(dstUserId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    if (value.equals("null")) { //first reply
                        //Log.e(TAG, "failed to get Dest User Name");
                    }
                    else {
                        dstUserName = value;
                        ////Log.d(TAG, dstUserId + "," + dstUserName + "," + dstDiaryId);
                    }
                }
            }
        });
    }

    protected void checkUserId() {
        if (userId.equals("")) {
            //Log.e(TAG, "failed to get User ID");
        } else {
            ////Log.d(TAG, userId);
        }
    }

    protected void setOnClickCancel() {
        Button imageButton = findViewById(R.id.cancel);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ConnectActivity.class);
            intent.putExtra("INTENT_FROM", "notReplied");
            intent.putExtra("REPLY_TO", "null");
            //Log.d(TAG, "DNum: " + nowDNum);
            intent.putExtra("D-NUM", nowDNum);
            startActivity(intent);
        });
    }

    protected void setOnClickPost() {
        Button btnPost = findViewById(R.id.post);
        btnPost.setOnClickListener(v -> {
            TextView textView = findViewById(R.id.editTextReply);
            String txt = textView.getText().toString();
            if (userId.equals("id_0")) {
                Toast.makeText(ReplyActivity.this, "デバッグユーザ0は投稿できません", Toast.LENGTH_SHORT).show();
            }
            else if (txt.length() == 0) {
                ////Log.d(TAG, dateTime());
                Toast.makeText(ReplyActivity.this, "返信内容がありません", Toast.LENGTH_SHORT).show();
            } else {
                ReadAndWrite rad = new ReadAndWrite();
                Calendar c = Calendar.getInstance();
                TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
                c.setTimeZone(tz); //日本時間に設定
                new AlertDialog.Builder(ReplyActivity.this)
                        .setTitle("")
                        .setMessage(dstUserName+"への返信を送信しますか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                rad.writeReply(userId, dstDiaryId, txt, dateTime(), c.getTimeInMillis());
                                Intent intent = new Intent(getApplication(), ConnectActivity.class);
                                intent.putExtra("INTENT_FROM", "reply");
                                intent.putExtra("REPLY_TO", dstUserName);
                                intent.putExtra("D-NUM", nowDNum);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ;
                            }
                        })
                        .show();
            }
        });
    }

    private String dateTime() {
        int year, month, date, hour, minute, second;
        Calendar c = Calendar.getInstance();
        ////Log.d(TAG, String.valueOf(c));
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        c.setTimeZone(tz); //日本時間に設定
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        date = c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        return String.format("%d/%02d/%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
    }
}

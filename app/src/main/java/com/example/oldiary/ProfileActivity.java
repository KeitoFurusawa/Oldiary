package com.example.oldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "prof";
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String userId;
    AlertDialog.Builder dialogBuilder;
    Dialog dialog;
    EditText user_name, content1, content2, content3, comment;
    Button save, cancel;
    private static final String[] GenreList = {
            "サッカー", "野球", "テニス", "ガーデニング", "読書",
            "ピアノ", "ゴルフ", "映画鑑賞", "音楽鑑賞", "散歩",
            "ランニング", "料理", "ボランティア", "将棋", "囲碁",
            "カラオケ", "旅行", "ワープロ", "手芸", "ギター",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        getUserId();
        // setOnClickBack();
        // editProfile();
    }

    protected void getUserId() {
        Intent intent1 = getIntent();
        userId = intent1.getStringExtra("UserID");
        Log.d(TAG, "PhoneNumber: " + userId);
    }

    private void setGenre() {
        for (int i = 0; i < 3; i++) {
            mDatabase.child("users").child(userId).child("favoriteGenre").child(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error getting data", task.getException());
                    }
                    else {
                        String userName = String.valueOf(task.getResult().getValue());
                        Log.d(TAG, userName); //debug
                        if (userName.equals("null")) { //初回ログイン
                            Intent intentNext = new Intent(getApplication(), MakeProfile.class);
                            intentNext.putExtra("UserID", userId); //インテントにユーザIDを渡す
                            startActivity(intentNext);
                        } else { //2回目以降
                            Intent intentNext = new Intent(getApplication(), HomeActivity.class);
                            intentNext.putExtra("UserID", userId);
                            intentNext.putExtra("UserName", userName);
                            editor.putString("UserID", userId);
                            editor.commit();
                            //
                            AlarmActivity alm = new AlarmActivity();
                            alm.mDestroy();
                            //
                            startActivity(intentNext);
                        }
                    }
                }
            });
        }
    }
    /*
    protected void setOnClickBack() {
        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }


    protected void editProfile() {
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), PopupActivity.class);
            startActivity(intent);
        });

    }

     */
}
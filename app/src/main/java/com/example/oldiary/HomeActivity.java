package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private final static String TAG = "home";
    MediaPlayer mediaPlayer;
    String userName;
    String userId;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        userId = preference.getString("UserID", "");
        checkUserId();
        setUserName();
        playMusic();
        setOnClick();
        setOnClick2();
        setOnClick3();
        setOnClick4();
        setOnClick5();
        setOnClick6();
        setOnClickLogout();
    }

    protected void playMusic() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sou);
        mediaPlayer.setLooping(true);
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    protected void setOnClick() {
        ImageButton imagebutton = findViewById(R.id.back_start);
        imagebutton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick2() {
        ImageButton imageButton2 = findViewById(R.id.write);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick3() {
        ImageButton imageButton3 = findViewById(R.id.bookshelf);
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick4() {
        ImageButton imageButton4 = findViewById(R.id.go_outside);
        imageButton4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ConnectActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick5() {
        ImageButton imageButton5 = findViewById(R.id.avatar);
        imageButton5.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ProfileActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick6() {
        ImageButton imageButton6 = findViewById(R.id.collection_of_words);
        imageButton6.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WordActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickLogout() {
        Button btn = findViewById(R.id.buttonLogout);
        btn.setOnClickListener(v -> {
            editor.putString("UserID", "");
            editor.commit();
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void checkUserId() {
        if (userId.equals("")) {
            Log.e(TAG, "failed to get User ID");
        } else {
            Log.d(TAG, userId);
        }
    }

    private void setUserName() {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("エラー")
                            .setMessage("データの取得に失敗しました。\nネットワークに接続してください。")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OKボタン押下時の処理
                                    Intent intent2 = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent2);
                                }
                            })
                            .show();
                }
                else {
                    String userNameResult = String.valueOf(task.getResult().getValue());
                    if (userNameResult.equals("null")) { //初回ログイン
                        Log.e(TAG, "ERROR: cannot get name");
                    } else {
                        userName = userNameResult;
                        TextView textViewUserId = findViewById(R.id.textViewId);
                        textViewUserId.setText(userName);
                    }
                }
            }
        });
    }
}
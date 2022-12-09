package com.example.oldiary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

public class HomeActivity extends AppCompatActivity {
    private final static String TAG = "home";
    private ImageButton avatarObj;
    private TextView nameObj;
    private ProgressDialog progressDialog;
    MediaPlayer mediaPlayer;
    String userName;
    String userId;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    private String gender = "null";
    private String color = "null";
    private boolean checkID = false, checkName= false, checkAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        StartLoading();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        userId = preference.getString("UserID", "");
        checkUserId();
        setUserName();
        loadPrevAvatar();
        playMusic();
        setOnClickBack();
        setOnClickWrite();
        setOnClickHistory();
        setOnClickConnect();
        setOnClickProfile();
        setOnClickHelp();
        setOnClickLogout();
    }


    private void StartLoading() {
        nameObj = findViewById(R.id.textViewId);
        avatarObj = findViewById(R.id.avatar);
        nameObj.setVisibility(View.GONE);
        avatarObj.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        if (checkID && checkName && checkAvatar) {
            progressDialog.dismiss();
            nameObj.setVisibility(View.VISIBLE);
            avatarObj.setVisibility(View.VISIBLE);
        }
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

    protected void setOnClickBack() {
        ImageButton imagebutton = findViewById(R.id.back_start);
        imagebutton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickWrite() {
        ImageButton imageButton2 = findViewById(R.id.write);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });
    }

    protected void setOnClickHistory() {
        ImageButton imageButton3 = findViewById(R.id.bookshelf);
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickConnect() {
        ImageButton imageButton4 = findViewById(R.id.go_outside);
        imageButton4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ConnectActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickProfile() {
        ImageButton imageButton5 = findViewById(R.id.avatar);
        imageButton5.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ProfileActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickHelp() {
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
        checkID = true;
        checkLoading();
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
                    checkName = true;
                    checkLoading();
                }
            }
        });
    }

    private void loadPrevAvatar() {
        mDatabase.child("users").child(userId).child("avatar").child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    gender = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + gender);
                    if (gender.equals("null")) {
                        gender = "man";
                    }
                    Log.d(TAG, gender);
                }
            }
        });
        mDatabase.child("users").child(userId).child("avatar").child("color").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    color = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + color);
                    if (color.equals("null")) {
                        color = "blue";
                    }
                    Log.d(TAG, color);
                    setPrevAvatar();
                }
            }
        });
    }

    private void setPrevAvatar() {
        while(gender.equals("null") || color.equals("null")) {
            Log.d(TAG, gender+color);
        }
        int drawableId = getResources().getIdentifier(color+"_"+gender, "drawable", getPackageName());
        ImageView imageView = findViewById(R.id.avatar);
        imageView.setImageResource(drawableId);
        checkAvatar = true;
        checkLoading();
    }
}
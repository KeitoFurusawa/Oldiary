package com.example.oldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login2copy2Activity extends AppCompatActivity {

    private static final String TAG = "Login";
    String userId;
    String inputPassword = "";
    private String correctPassword;
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    int count = 0;

    SoundPool soundPool;
    int mp3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2copy2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        setOnClick();
        getUserId();
        setCorrectPassword();
        setOnclickConfirm();
        passWord();
        ss();
    }
    protected void ss() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
            mp3a = soundPool.load(this, R.raw.success, 1);
        }
    }

    protected void setOnClick() {
        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }

    protected void getUserId() {
        Intent intent1 = getIntent();
        userId = intent1.getStringExtra("UserID");
        Log.d(TAG, "PhoneNumber: " + userId);

    }

    protected void setOnclickConfirm() {
        Button buttonGo = findViewById(R.id.button_next);
        buttonGo.setOnClickListener(v -> {
            if (inputPassword.length() < 4) {
                Log.e(TAG, "ERROR: the length of inputPassword is not enough.");
                Toast.makeText(
                        Login2copy2Activity.this, "パスワードは4桁入力してください", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "inputPass: " + inputPassword);
                Log.d(TAG, "correctPass: " + correctPassword);
                if (inputPassword.equals(correctPassword)) { //パスワードが正しい
                    checkUserName();
                } else {
                    Log.e(TAG, "ERROR: incorrect password");
                    Toast.makeText(Login2copy2Activity.this, "パスワードが間違っています", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void passWord() {
        Button button0 = findViewById(R.id.button_0);
        Button button1 = findViewById(R.id.button_1);
        Button button2 = findViewById(R.id.button_2);
        Button button3 = findViewById(R.id.button_3);
        Button button4 = findViewById(R.id.button_4);
        Button button5 = findViewById(R.id.button_5);
        Button button6 = findViewById(R.id.button_6);
        Button button7 = findViewById(R.id.button_7);
        Button button8 = findViewById(R.id.button_8);
        Button button9 = findViewById(R.id.button_9);
        Button cancel = findViewById(R.id.cancel);

        button0.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "0";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button1.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "1";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button2.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "2";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button3.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "3";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button4.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "4";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button5.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "5";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button6.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "6";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button7.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "7";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button8.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "8";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        button9.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                inputPassword += "9";
                Log.d(TAG, inputPassword);
                TextView textView = findViewById(R.id.password);
                textView.setText(toAst(inputPassword));
            }
        });

        cancel.setOnClickListener(v -> {
            Log.d(TAG, "Previous Pass:" + inputPassword);
            inputPassword = "";
            count = 0;
            TextView textView = findViewById(R.id.password);
            textView.setText(toAst(inputPassword));
        });
    }

    public void setCorrectPassword() {
        Log.d(TAG, "setCorrectPassword()");
        mDatabase.child("users").child(userId).child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is sCP onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    correctPassword = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, correctPassword); //debug
                }
            }
        });
    }

    protected void checkUserName() {
        Log.d(TAG, "checkUserName()");
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is cUN onComplete");
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
                        startActivity(intentNext);
                    }
                }
            }
        });
    }

    public String mulString(String s, int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public String toAst(String xs) {
        return mulString("*", xs.length());
    }
}
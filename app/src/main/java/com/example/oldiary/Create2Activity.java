package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Create2Activity extends AppCompatActivity {
    String phoneNumber;
    String password = "";
    int count = 0;
    private static final String TAG = "CreateActivity";

    SoundPool soundPool;
    int mp3a, mp3b, back, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setOnClick();
        getPhoneNumber();
        getPassword();
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
            mp3b = soundPool.load(this, R.raw.error, 1);
            back = soundPool.load(this, R.raw.back, 1);
            next = soundPool.load(this, R.raw.next, 1);
        }
    }

    protected void setOnClick() {
        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(v -> {
            soundPool.play(back,15 , 15, 0, 0, 2);
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }

    protected void getPhoneNumber() {
        Intent intent1 = getIntent();
        phoneNumber = intent1.getStringExtra("PhoneNumber");
        ////Log.d(TAG, "PhoneNumber: " + phoneNumber);
    }

    protected void getPassword() {
        Button buttonGo = findViewById(R.id.button_next);
        buttonGo.setOnClickListener(v -> {
            if (password.length() < 4) {
                soundPool.play(mp3b,9 , 9, 0, 0, 1f);
                //Log.e(TAG, "ERROR: the length of password is not enough.");
                Toast.makeText(
                        Create2Activity.this, "パスワードは4桁入力してください", Toast.LENGTH_SHORT).show();
            } else {

                ReadAndWrite readAndWrite = new ReadAndWrite();
                readAndWrite.addNewUser(phoneNumber, password);
                new AlertDialog.Builder(Create2Activity.this)
                        .setTitle("注意")
                        .setMessage("忘れても大丈夫なように、\nパスワードの保管をおススメします。\n" + "電話番号： " + phoneNumber + "\n" + "パスワード： " + password + "\n")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OKボタン押下時の処理
                                Intent intent2 = new Intent(getApplication(), SuccessActivity.class);
                                startActivity(intent2);
                                soundPool.play(mp3a,9 , 9, 0, 0, 2);
                                //Log.d("AlertDialog", "Positive which :" + which);
                            }
                        })
                        .show();
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
                password += "0";
                ////Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button1.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "1";
                ////Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button2.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "2";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button3.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "3";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button4.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "4";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button5.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "5";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button6.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "6";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button7.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "7";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button8.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "8";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button9.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "9";
                //Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        cancel.setOnClickListener(v -> {
            //Log.d(TAG, "Previous Pass:" + password);
            password = "";
            count = 0;
            TextView textView = findViewById(R.id.password);
            textView.setText(password);
        });
    }

}
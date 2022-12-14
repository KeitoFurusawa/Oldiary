package com.example.oldiary;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AlarmActivity {
    private static final String TAG = "Login";
    private String phoneNumber;
    private String password;
    private DatabaseReference mDatabase;
    private static String getResult;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    SoundPool soundPool;
    int mp3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        playMusic();
        setOnClickCreateNew();
        setOnClickLogin();
        ss();
    }

    public void playMusic() {
        try {
            Thread.sleep(800);
            mStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
            mp3a = soundPool.load(this, R.raw.error, 1);

        }
    }

    public void onClick(View view) {
        Button button = findViewById(R.id.backbtn);
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplication(), MainScreen.class);
            mDestroy();
            startActivity(intent);
        });
    }

    //???????????????????????????
    protected void setOnClickCreateNew() {
        Button button= findViewById(R.id.make_account);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }
    //????????????
    protected void setOnClickLogin() {
        Button buttonLogin = findViewById(R.id.buttonLogin);
        EditText textViewPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        //TextView textViewPassword = findViewById(R.id.editTextPassword);
        buttonLogin.setOnClickListener(v -> {
            phoneNumber = textViewPhoneNumber.getText().toString();
            //password = textViewPassword.getText().toString();
            String userId = "id_" + phoneNumber;
            Log.d(TAG, phoneNumber);//debug ?????????
            //Log.d(TAG, password);   //debug ?????????

            if ((phoneNumber.length() > 8) /*&& (password.length() == 4)*/) {
                getData(userId);

            } else {
                if (phoneNumber.equals("0")) { //0????????????
                    Intent intent = new Intent(getApplication(), MakeProfile.class);
                    intent.putExtra("UserID", userId); //???????????????????????????ID?????????
                    startActivity(intent);
                } else {
                    soundPool.play(mp3a,9 , 9, 0, 0, 1);
                    Log.d(TAG, "incorrect input value");//????????????????????????
                    Toast.makeText(LoginActivity.this, "???????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void getData(String userId) {
        //Log.d("debug", "this is getData");
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("?????????")
                            .setMessage("??????????????????????????????????????????\n????????????????????????????????????????????????")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK???????????????????????????
                                    Intent intent2 = new Intent(getApplication(), MainActivity.class);
                                    mDestroy();
                                    startActivity(intent2);
                                }
                            })
                            .show();
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    setResult(value); //firebase????????????????????????????????????????????????
                    checkResult(userId); //?????????????????????????????????
                }
            }
        });
    }

    protected void setResult(String result) {
        Log.d("debug", "setResult");
        this.getResult = result;
    }

    protected void checkResult(String userId) {
        Log.d("debug", "this is checkPhoneNum");
        while (this.getResult == null) {
            Log.d(TAG, "loading info");
        }
        Log.d(TAG, userId + ": " + getResult); //debug
        if (getResult.equals("null")) { //?????????????????????
            soundPool.play(mp3a,9 , 9, 0, 0, 1);
            Log.e(TAG, "ERROR: that PhoneNumber wasn't registered");
            Toast.makeText(LoginActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intentNext = new Intent(getApplication(), Login2Activity.class);
            intentNext.putExtra("UserID", userId);
            startActivity(intentNext);
        }

    }
}
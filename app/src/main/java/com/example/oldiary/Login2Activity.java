package com.example.oldiary;

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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Login2Activity extends AppCompatActivity {

    private static final String TAG = Login2Activity.class.getSimpleName();
    String userId;
    private String inputPassword = "";
    private String correctPassword;
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    int count = 0;
    private TextView textView;
    private boolean hide = true;

    private static final @IdRes
    int[] BUTTONS_ID = {
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
            R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
    };

    SoundPool soundPool;
    int mp3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        textView = findViewById(R.id.password);

        View.OnClickListener listener = v -> {
            if (inputPassword.length() < 4) {
                inputPassword += ((Button)v).getText();
                //Log.(TAG, inputPassword);
                if (hide) {
                    textView.setText(toAst(inputPassword));

                } else {
                    textView.setText(inputPassword);
                }
            }
        };
        Button[] buttons = new Button[BUTTONS_ID.length];
        for (int i = 0; i < BUTTONS_ID.length; i++) {
            buttons[i] = findViewById(BUTTONS_ID[i]);
            buttons[i].setOnClickListener(listener);
        }
        setRandomNumberTo(buttons);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            //Log.(TAG, "Previous PAss:" + inputPassword);
            inputPassword = "";
            textView.setText(toAst(inputPassword));
            setRandomNumberTo(buttons); //キャンセルで入れ変えてみる
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        setOnClick();
        getUserId();
        setCorrectPassword();
        setOnclickConfirm();
        ss();
        setOnChangedListener();
    }

    private  void setRandomNumberTo(Button[] buttons) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < buttons.length; i++) numbers.add(i);
        Collections.shuffle(numbers);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText("" + numbers.get(i));
        }
    }

    private String toAst(String text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) sb.append('*');
        return sb.toString();
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
        //Log.(TAG, "PhoneNumber: " + userId);
    }

    protected void setOnclickConfirm() {
        Button buttonGo = findViewById(R.id.button_next);
        buttonGo.setOnClickListener(v -> {
            if (inputPassword.length() < 4) {
                Log.e(TAG, "ERROR: the length of inputPassword is not enough.");
                Toast.makeText(
                        Login2Activity.this, "パスワードは4桁入力してください", Toast.LENGTH_SHORT).show();
            } else {
                //Log.(TAG, "inputPass: " + inputPassword);
                //Log.(TAG, "correctPass: " + correctPassword);
                if (inputPassword.equals(correctPassword)) { //パスワードが正しい
                    checkUserName();
                } else {
                    Log.e(TAG, "ERROR: incorrect password");
                    Toast.makeText(Login2Activity.this, "パスワードが間違っています", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setCorrectPassword() {
        //Log.(TAG, "setCorrectPassword()");
        mDatabase.child("users").child(userId).child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.("debug", "this is sCP onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    correctPassword = String.valueOf(task.getResult().getValue());
                    //Log.(TAG, correctPassword); //debug
                }
            }
        });
    }

    protected void checkUserName() {
        //Log.(TAG, "checkUserName()");
        mDatabase.child("users").child(userId).child("favoriteGenre").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.("debug", "this is cUN onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String userName = String.valueOf(task.getResult().getValue());
                    //Log.(TAG, userName); //debug
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

    private void setOnChangedListener() {
        Switch hideSwitch = findViewById(R.id.switch1);
        hideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    hide = false;
                    textView.setText(inputPassword);
                    hideSwitch.setText("隠す");
                } else {
                    hide = true;
                    textView.setText(toAst(inputPassword));
                    hideSwitch.setText("表示する");
                }
            }
        });
    }
}
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

import java.util.Random;


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

    //新規アカウント作成
    protected void setOnClickCreateNew() {
        Button button= findViewById(R.id.make_account);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }
    //ログイン
    protected void setOnClickLogin() {
        Button buttonLogin = findViewById(R.id.buttonLogin);
        EditText textViewPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        //TextView textViewPassword = findViewById(R.id.editTextPassword);
        buttonLogin.setOnClickListener(v -> {
            phoneNumber = textViewPhoneNumber.getText().toString();
            //password = textViewPassword.getText().toString();
            String userId = "id_" + phoneNumber;
            Log.d(TAG, phoneNumber);//debug 確認用
            //Log.d(TAG, password);   //debug 確認用

            if ((phoneNumber.length() > 8) /*&& (password.length() == 4)*/) {
                getData(userId);

            } else {
                if (phoneNumber.equals("0")) { //0番は通過
                    Intent intent = new Intent(getApplication(), MakeProfile.class);
                    intent.putExtra("UserID", userId); //インテントにユーザIDを渡す
                    startActivity(intent);
                } else {
                    soundPool.play(mp3a,9 , 9, 0, 0, 1);
                    Log.d(TAG, "incorrect input value");//入力が正しくない
                    Toast.makeText(LoginActivity.this, "電話番号・暗証番号を正しく入力してください", Toast.LENGTH_SHORT).show();
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
                            .setTitle("エラー")
                            .setMessage("データの取得に失敗しました。\nネットワークに接続してください。")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OKボタン押下時の処理
                                    Intent intent2 = new Intent(getApplication(), MainActivity.class);
                                    mDestroy();
                                    startActivity(intent2);
                                }
                            })
                            .show();
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    setResult(value); //firebaseから取ってきたデータをセットする
                    checkResult(userId); //データのチェックをする
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
        if (getResult.equals("null")) { //電話番号未登録
            soundPool.play(mp3a,9 , 9, 0, 0, 1);
            Log.e(TAG, "ERROR: that PhoneNumber wasn't registered");
            Toast.makeText(LoginActivity.this, "その電話番号は登録されていません", Toast.LENGTH_SHORT).show();
        } else { //登録済み
            /*idを渡してパスワード入力画面へ*/
            Random random = new Random();
            int randomValue = random.nextInt(3);
            switch(randomValue) {
                case 0:
                    Intent intentNext = new Intent(getApplication(), Login2Activity.class);
                    intentNext.putExtra("UserID", userId);
                    startActivity(intentNext);
                    break;
                case 1:
                    Intent intentNext2 = new Intent(getApplication(), Login2copy1Activity.class);
                    intentNext2.putExtra("UserID", userId);
                    startActivity(intentNext2);
                    break;
                case 2:
                    Intent intentNext3 = new Intent(getApplication(), Login2copy2Activity.class);
                    intentNext3.putExtra("UserID", userId);
                    startActivity(intentNext3);
            }
        }
    }
}
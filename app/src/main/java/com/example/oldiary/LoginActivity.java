package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        setOnClickBack();
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

    //戻る
    protected void setOnClickBack() {
        ImageButton imageButton = findViewById(R.id.imageButtonBack);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
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
            Intent intentNext = new Intent(getApplication(), Login2Activity.class);
            intentNext.putExtra("UserID", userId);
            startActivity(intentNext);
            /*
            //int idxAddPhoneNum = "phoneNumber=".length(); // = 12
            int idxAddPass = "password=".length(); // = 9
            //int idxOfRePhoneNum = getResult.indexOf("phoneNumber=") + idxAddPhoneNum;
            int idxOfRePass = getResult.indexOf("password=") + idxAddPass;
            String resultPassword = getResult.substring(idxOfRePass, idxOfRePass+4);

            if (password.equals(resultPassword)) { //パスワードが正しい
                Log.d(TAG, "login success");
                checkUserName(userId);
            } else { //パスワードが誤り
                soundPool.play(mp3a,9 , 9, 0, 0, 1);
                Log.e(TAG, "ERROR: incorrect password");
                Toast.makeText(LoginActivity.this, "パスワードが間違っています", Toast.LENGTH_SHORT).show();
            }
            */
        }
    }


}
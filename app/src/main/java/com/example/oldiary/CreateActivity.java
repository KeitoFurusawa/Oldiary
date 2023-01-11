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
import androidx.annotation.NonNull;
import android.media.SoundPool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateActivity extends AppCompatActivity {
    private String phoneNumber;
    private static final String TAG = "CreateActivity";
    private DatabaseReference mDatabase;
    private static String getResult;
    SoundPool soundPool;
    int mp3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setOnClick();
        setOnClick2();
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
            mp3a = soundPool.load(this, R.raw.error, 1);

        }
    }

    //Back
    protected void setOnClick() {
        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }

    //Next
    protected void setOnClick2() {
        Button ButtonNext = findViewById(R.id.button_next);
        TextView textView = findViewById(R.id.editTextPhoneNumber);
        ButtonNext.setOnClickListener(v -> {
            phoneNumber = textView.getText().toString();
            if (phoneNumber.length() > 8) { //エラーハンドル
                String userId = "id_" + phoneNumber;
                getData(userId);
            } else {
                //debug用 0は通過
                if (phoneNumber.equals("0")) {
                    //格納されている文字列の比較は.equalsを使わないといけない
                    Intent intent = new Intent(getApplication(), Create2Activity.class);
                    intent.putExtra("PhoneNumber", phoneNumber);
                    startActivity(intent);
                } else { //電話番号の長さが9未満はエラー
                    soundPool.play(mp3a,9 , 9, 0, 0, 1);
                    //Log.d(TAG, "length of phoneNumber is not enough");
                    Toast.makeText(CreateActivity.this, "正しい電話番号を入力してください", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //onComplete
    // データベースから一度だけ情報を読み取る
    protected void getData(String userId) {
        //Log.d("debug", "this is getData");
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    //Toast.makeText(CreateActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(CreateActivity.this)
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
                    String value = String.valueOf(task.getResult().getValue());
                    setResult(value);
                    checkResult(userId);
                }
            }
        });
    }

    protected void setResult(String result) {
        //Log.d("debug", "setResult");
        this.getResult = result;
    }

    protected void checkResult(String userId) {
        //Log.d("debug", "this is checkPhoneNum");
        while (this.getResult == null) {
            //Log.d(TAG, "loading info");
        }
        //Log.d(TAG, userId + ": " + getResult); //debug
        if (!(getResult.equals("null"))) { //idでサーバを検索した時登録済みの場合はエラー
            soundPool.play(mp3a,9 , 9, 0, 0, 1);
            //Log.e(TAG, "ERROR: that PhoneNumber was already registered");
            Toast.makeText(CreateActivity.this, "その電話番号はすでに使われています。", Toast.LENGTH_SHORT).show();
        } else { //未登録の番号はOK
            Intent intent = new Intent(getApplication(), Create2Activity.class);
            intent.putExtra("PhoneNumber", phoneNumber);
            startActivity(intent);
        }
    }
}
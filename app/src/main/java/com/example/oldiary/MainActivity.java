package com.example.oldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "main";
    MediaPlayer mediaPlayer;
    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3a;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();


        if (preference.getBoolean("Launched", false)==false) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("テスト")
                    .setMessage("あなたは初めてこのアプリを立ち上げましたね？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // OKボタン押下時の処理
                            editor.putBoolean("Launched", true);
                            editor.commit();
                        }
                    })
                    .show();
        }
        startScene();
    }

    protected void startScene() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.birds);
        mediaPlayer.setLooping(true);

        imageChange();
        setOnClick();
        ss();
        debug();

        TextView txtView_start = findViewById(R.id.announce);
        blinkText(txtView_start, 650, 200);
    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.info);
        imageButton.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("ヒント")
                    .setMessage("扉を押すと…？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        });
    }

    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.door);
        imageButton.setOnClickListener(v -> {
            String loggedInId = preference.getString("UserID", "");
            try {
                imageButton.setImageResource(R.drawable.opendoor);
                Thread.sleep(500);
                soundPool.play(mp3a,9 , 9, 0, 0, 2);
                checkLoggedIn(loggedInId); //ログインチェック
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    protected void debug() {
        ImageButton imgButtonD = findViewById(R.id.imageButtonDebug);
        imgButtonD.setOnClickListener(v -> {
            Intent intentD = new Intent(getApplication(), SelectGenreActivity.class);
            startActivity(intentD);
        });
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void blinkText(TextView txtView, long duration, long offset){
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatMode(Animation.REVERSE);
        anm.setRepeatCount(Animation.INFINITE);
        txtView.startAnimation(anm);
    }

    protected void ss(){
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
            mp3a = soundPool.load(this, R.raw.opdoor, 1);
        }
    }

    protected void checkLoggedIn(String loggedInId) {
        Log.d(TAG, loggedInId);
        if (loggedInId.length() == 0) { //未ログイン
            Intent intentLogin = new Intent(getApplication(), LoginActivity.class);
            startActivity(intentLogin);
        } else { //ログイン済み
            Intent intentHome = new Intent(getApplication(), HomeActivity.class);
            intentHome.putExtra("UserID", loggedInId);

            mDatabase.child("users").child(loggedInId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Log.d("debug", "this is onComplete");
                    if (!task.isSuccessful()) {
                        //Log.e(TAG, "Error getting data", task.getException());
                    }
                    else {
                        String value = String.valueOf(task.getResult().getValue());
                        intentHome.putExtra("UserName", value);
                        startActivity(intentHome);
                    }
                }
            });
        }
    }
}
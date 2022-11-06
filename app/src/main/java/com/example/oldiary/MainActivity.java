package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;


public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.birds);
        mediaPlayer.setLooping(true);

        imageChange();
        ss();
        debug();

        TextView txtView_start = findViewById(R.id.announce);
        blinkText(txtView_start, 650, 200);

    }

    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.door);
        imageButton.setOnClickListener(v -> {
            try {
                imageButton.setImageResource(R.drawable.opendoor);
                soundPool.play(mp3a,9 , 9, 0, 0, 2);
                Thread.sleep(500);
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    protected void debug() {
        ImageButton imgButtonD = findViewById(R.id.imageButtonDebug);
        imgButtonD.setOnClickListener(v -> {
            Intent intentD = new Intent(getApplication(), RegisterActivity.class);
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
}
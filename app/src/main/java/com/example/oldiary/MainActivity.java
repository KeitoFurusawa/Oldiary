package com.example.oldiary;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int soundOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.birds);
        mediaPlayer.setLooping(true);

        soundPlay();
        playMusic();
        imageChange();

        TextView txtView_start = findViewById(R.id.textView2);
        blinkText(txtView_start, 650, 200);

    }

    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(v -> {
            try {
                imageButton.setImageResource(R.drawable.opendoor);
                Thread.sleep(3000);
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

    protected void onDestory() {
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

    public void soundPlay() {
        soundOne = soundPool.load(this, R.raw.opendoor, 1);
    }

    public void playMusic() {
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener( v -> {
            soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1.0f);
        });
    }
}

package com.example.oldiary;
// https://akira-watson.com/android/activity-1.html←参考

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.media.SoundPool;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.media.MediaPlayer;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int openDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.birds);
        mediaPlayer.setLooping(true);

        openDoor = soundPool.load(this, R.raw.opendoor, 1);

        imageChange();

        TextView txtView_start = findViewById(R.id.textView2);
        blinkText(txtView_start, 650, 200);

    }

    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(v -> {
            try {
                imageButton.setImageResource(R.drawable.opendoor);
                soundPool.play(openDoor, 1f, 1f, 0, 0, 1);
                Thread.sleep(1000);
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

    protected void onDestry() {
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
}
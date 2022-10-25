package com.example.oldiary;
// https://akira-watson.com/android/activity-1.html←参考

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.media.MediaPlayer;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.birds);
        mediaPlayer.setLooping(true);

        imageChange();

        TextView txtView_start = findViewById(R.id.textView2);
        blinkText(txtView_start, 650, 200);

    }

    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(v -> {
            imageButton.setImageResource(R.drawable.opendoor);
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
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
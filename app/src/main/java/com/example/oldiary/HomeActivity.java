package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.content.Intent;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        playMusic();
        setOnClick();
        setOnClick2();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.home_music);
        mediaPlayer.setLooping(true);
    }

    protected void playMusic() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ensolarado);
        mediaPlayer.setLooping(true);

    }

    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    protected void setOnClick() {
        ImageButton button = findViewById(R.id.back_start);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick2() {
        ImageButton button2 = findViewById(R.id.write);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            startActivity(intent);
        });
    }
}
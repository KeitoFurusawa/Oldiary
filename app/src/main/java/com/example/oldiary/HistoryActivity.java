package com.example.oldiary;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.history);
        mediaPlayer.setLooping(true);
    }
}
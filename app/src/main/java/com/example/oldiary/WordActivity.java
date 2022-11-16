package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class WordActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        TextView textView = findViewById(R.id.collection_of_words);
        textView.setMovementMethod(new ScrollingMovementMethod());

        playMusic();
    }

    protected void playMusic() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.studyingmusic);
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
}
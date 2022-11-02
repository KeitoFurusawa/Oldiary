package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent1 = getIntent();
        userId = intent1.getStringExtra("UserID");
        TextView textViewUserId = findViewById(R.id.textViewId);
        textViewUserId.setText(userId);

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
}
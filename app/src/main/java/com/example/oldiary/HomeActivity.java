package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.content.Intent;
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

        playMusic();
        setOnClick();
        setOnClick2();
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
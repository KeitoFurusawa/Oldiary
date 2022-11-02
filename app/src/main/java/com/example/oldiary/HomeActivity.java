package com.example.oldiary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageChange();
        setOnClick();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sou);
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
    protected void  imageChange() {
        ImageButton imageButton = findViewById(R.id.imageButton4);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ProfileActivity.class);
            startActivity(intent);


        });
    }
    protected void setOnClick() {
        ImageButton imagebutton = findViewById(R.id.imageButton7);
        imagebutton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
    }
}
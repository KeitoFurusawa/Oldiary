package com.example.oldiary;
// https://akira-watson.com/android/activity-1.html←参考

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.MotionEvent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.AudioAttributes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.talkative);
        mediaPlayer.setLooping(true);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message2");

        myRef.setValue("Hey! Firebase");
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

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivity(intent);
        }
        return false;
    }
}
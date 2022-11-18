package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MusicTestActivity extends AlarmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_test);
    }

    public void PLAY(View v) {
        mStart();
    }

    public void NEXT(View v) {
        Intent intent = new Intent(this, MusicStopActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MusicStopActivity extends AlarmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_stop);
    }

    public void STOP(View v) {
        mDestroy();
    }
}
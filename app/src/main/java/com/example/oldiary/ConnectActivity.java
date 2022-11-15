package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;

public class ConnectActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private static final String API_KEY = "AIzaSyBtAfSPNfUXI3bUWBf65-nw-50pg9sXyF4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        setOnClick();

    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.back_home);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

}
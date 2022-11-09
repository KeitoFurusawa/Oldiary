package com.example.oldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    String userName;
    String userId;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("UserName");
        userId = intent1.getStringExtra("UserId");
        TextView textViewUserId = findViewById(R.id.textViewId);
        textViewUserId.setText(userName);
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        playMusic();
        setOnClick();
        setOnClick2();
        setOnClick3();
        setOnClick4();
        setOnClick5();
        setOnClickLogout();
    }

    protected void playMusic() {
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

    protected void setOnClick() {
        ImageButton imagebutton = findViewById(R.id.back_start);
        imagebutton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick2() {
        ImageButton imageButton2 = findViewById(R.id.write);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick3() {
        ImageButton imageButton3 = findViewById(R.id.bookshelf);
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick4() {
        ImageButton imageButton4 = findViewById(R.id.go_outside);
        imageButton4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ConnectActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick5() {
        ImageButton imageButton5 = findViewById(R.id.avatar);
        imageButton5.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), ProfileActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickLogout() {
        Button btn = findViewById(R.id.buttonLogout);
        btn.setOnClickListener(v -> {
            editor.putString("UserID", "");
            editor.commit();
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.oldiary;
// https://akira-watson.com/android/activity-1.html←参考

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = findViewById(R.id.send_button);
        // lambda式
        sendButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SubActivity.class);
            startActivity(intent);
        });
    }
}
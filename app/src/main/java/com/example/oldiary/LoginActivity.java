package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageButton;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView = findViewById(R.id.textView9);
        // lambda式
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });

        ImageButton imageButton = findViewById(R.id.imageButton);
        // lambda式
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }
}

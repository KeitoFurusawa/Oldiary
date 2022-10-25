package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button button = findViewById(R.id.button3);
        // lambda式
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button2);
        // lambda式
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), Create2Activity.class);
            startActivity(intent);
        });
    }
}
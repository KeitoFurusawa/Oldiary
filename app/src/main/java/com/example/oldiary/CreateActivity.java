package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        setOnClick();
        setOnClick2();
    }
    protected void setOnClick() {
        Button Button = findViewById(R.id.button3);

        // lambda式
        Button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }
    protected void setOnClick2() {
        Button Button2 = findViewById(R.id.button2);

        // lambda式
        Button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), Create2Activity.class);
            startActivity(intent);
        });
    }
}
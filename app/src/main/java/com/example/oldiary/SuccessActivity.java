package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setOnClick();
    }
    protected void setOnClick() {
        Button Button = findViewById(R.id.try_login);

        Button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });

        Button jumpToRegister = findViewById(R.id.buttonToRegister);
        jumpToRegister.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplication(), RegisterActivity.class);
                    startActivity(intent);
                }
        );
    }
}
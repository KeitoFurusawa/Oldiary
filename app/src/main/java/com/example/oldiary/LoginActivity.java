package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setOnClick();
        setOnClick2();
        setOnClick3();
    }
    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.imageButton);

        // lambda式
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }
    protected void setOnClick2() {
        TextView textView = findViewById(R.id.textView9);
        // lambda式
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClick3() {
        Button button3 = findViewById(R.id.button16);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

}

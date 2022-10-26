package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Create2Activity extends AppCompatActivity {
    String phoneNumber;
    String password = "";

    int count = 0;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("PhoneNumber");
        Log.i(TAG, "PhoneNumber: " + phoneNumber);

        setOnClick();
        setOnClick2();
        passWord();
    }
    protected void setOnClick() {
        Button button = findViewById(R.id.back);
        // lambda式
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }
    protected void setOnClick2() {
        Button button2 = findViewById(R.id.go);
        // lambda式
        if (password.length() < 4)//////here
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SuccessActivity.class);
            startActivity(intent);
        });
    }

    protected void passWord() {
        Button button0 = findViewById(R.id.button_0);
        Button button1 = findViewById(R.id.button_1);
        Button button2 = findViewById(R.id.button_2);
        Button button3 = findViewById(R.id.button_3);
        Button button4 = findViewById(R.id.button_4);
        Button button5 = findViewById(R.id.button_5);
        Button button6 = findViewById(R.id.button_6);
        Button button7 = findViewById(R.id.button_7);
        Button button8 = findViewById(R.id.button_8);
        Button button9 = findViewById(R.id.button_9);
        Button cancel = findViewById(R.id.cancel);

        button0.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "0";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button1.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "1";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button2.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "2";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button3.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "3";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button4.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "4";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button5.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "5";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button6.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "6";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button7.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "7";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button8.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "8";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        button9.setOnClickListener(v -> {
            count += 1;
            if (count < 5) {
                password += "9";
                Log.d(TAG, password);
                TextView textView = findViewById(R.id.password);
                textView.setText(password);
            }
        });

        cancel.setOnClickListener(v -> {
            Log.d(TAG, "Previous Pass:" + password);
            password = "";
            count = 0;
            TextView textView = findViewById(R.id.password);
            textView.setText(password);
        });

    }

}
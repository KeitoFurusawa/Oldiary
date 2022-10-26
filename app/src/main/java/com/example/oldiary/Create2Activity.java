package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Create2Activity extends AppCompatActivity {

    String nStr = "";
    int count = 0;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);

        setOnClick();
        setOnClick2();
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

        while (count == 4) {
            button0.setOnClickListener(v -> {
                nStr += "0";
                count += 1;
                Log.d(TAG, nStr);
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button1.setOnClickListener(v -> {
                nStr += "1";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button2.setOnClickListener(v -> {
                nStr += "2";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button3.setOnClickListener(v -> {
                nStr += "3";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button4.setOnClickListener(v -> {
                nStr += "4";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button5.setOnClickListener(v -> {
                nStr += "5";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button6.setOnClickListener(v -> {
                nStr += "6";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button7.setOnClickListener(v -> {
                nStr += "7";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button8.setOnClickListener(v -> {
                nStr += "8";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            button9.setOnClickListener(v -> {
                nStr += "9";
                count += 1;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });

            cancel.setOnClickListener(v -> {
                nStr = "";
                count = 0;
                TextView textView = findViewById(R.id.password);
                textView.setText(nStr);
            });
        }
    }

}
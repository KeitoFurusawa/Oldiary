package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {
    private String phoneNumber;
    private static final String TAG = "CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        setOnClick();
        setOnClick2();
    }
    //Back
    protected void setOnClick() {
        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }
    //Next
    protected void setOnClick2() {
        Button ButtonNext = findViewById(R.id.button_next);
        TextView textView = findViewById(R.id.input_num);

        ButtonNext.setOnClickListener(v -> {
            phoneNumber = textView.getText().toString();
            Log.d(TAG, phoneNumber);
            if ((phoneNumber.length() > 8) || (phoneNumber != "0")) { //エラーハンドル //0はデバッグ用
                Intent intent = new Intent(getApplication(), Create2Activity.class);
                intent.putExtra("PhoneNumber", phoneNumber);
                startActivity(intent);
            } else {
                Log.d(TAG, "length of phoneNumber is not enough");
                Toast.makeText(
                        CreateActivity.this, "正しい電話番号を入力してください", Toast.LENGTH_LONG).show();
            }

        });
    }
}
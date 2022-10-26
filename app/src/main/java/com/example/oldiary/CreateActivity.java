package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {
    private String phoneNumber;

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

        // lambda式
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }
    //Next
    protected void setOnClick2() {
        Button ButtonNext = findViewById(R.id.button_next);
        TextView textView = findViewById(R.id.editTextPhoneNumber);
        phoneNumber = textView.getText().toString();
        //本来はここで正常な電話番号であることを確認するエラーハンドル
        // lambda式
        ButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), Create2Activity.class);
            intent.putExtra("PhoneNumber", phoneNumber);
            startActivity(intent);
        });
    }
}
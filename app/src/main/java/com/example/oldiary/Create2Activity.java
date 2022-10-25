package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Create2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);

        Button button = findViewById(R.id.button13);
        // lambda式
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button14);
        // lambda式
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SuccessActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);

        Button returnButton = findViewById(R.id.return_button);
        // lambda式
        returnButton.setOnClickListener(v -> finish());
    }
}
package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;



public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);

        ImageView HomeImage = findViewById(R.id.imageView2);

        Button returnButton = findViewById(R.id.return_button);

        // lambda式
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);

            switch (v.getId()) {
                case R.id.return_button:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
            }
        });

        Button jumpToRegister = findViewById(R.id.buttonToRegister);
        jumpToRegister.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplication(), RegisterActivity.class);
                    startActivity(intent);
                }
        );

    }
}

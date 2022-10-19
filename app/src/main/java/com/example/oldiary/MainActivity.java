package com.example.oldiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アクションバー非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //

        ImageView titleLogoView = findViewById(R.id.imageViewTitleLogo);
        titleLogoView.setImageResource(R.drawable.oldiary_title);
        int i = 1024;
        Log.i("=====This is Debug=====", Integer.valueOf(i).toString());

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        sleepf(3000);
        startActivity(intent);

    }

    public void sleepf(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.d("sleepf: ", "ERROR");
        }
    }

}

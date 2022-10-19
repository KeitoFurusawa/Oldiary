package com.example.oldiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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


        ImageView titleLogoView = findViewById(R.id.imageViewTitleLogo);
        titleLogoView.setImageResource(R.drawable.oldiary_title);

        //ハンドラーで3秒待ち gerMainLooperはメインスレッドで実行するためのおまじない
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }, 3000);

        //sleepf(3000);
        //Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        //startActivity(intent);
    }

    //スレッドスリープ関数
    public void sleepf(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.d("sleepf: ", "ERROR");
        }
    }
}

package com.example.oldiary;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.history);
        mediaPlayer.setLooping(true);

        setOnClickBack();
        changePhoto();
        changePhoto2();
        changePhoto3();
    }
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    protected void setOnClickBack() {
        ImageButton imageButton = findViewById(R.id.back_home);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

    protected void changePhoto() {
        ImageView choose1 = findViewById(R.id.photo_example);
        TypedArray photo1 = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);
        int rand = (int)(Math.floor(Math.random() * 3));
        Drawable drawable = photo1.getDrawable(rand);
        choose1.setImageDrawable(drawable);

        TranslateAnimation translate;
        translate = new TranslateAnimation(
                Animation.ABSOLUTE, -100.0f,
                Animation.ABSOLUTE, 1200.0f,
                Animation.ABSOLUTE, 30.0f,
                Animation.ABSOLUTE, 30.0f);
        //アニメーション時間(ミリ秒)
        translate.setDuration(10000);
        // 繰り返し時間
        translate.setRepeatCount(0);
        //アニメーションが終わったらそのまま表示にする
        translate.setFillAfter(true);
        //アニメーションの開始
        choose1.startAnimation(translate);
    }

    protected void changePhoto2() {
        ImageView choose2 = findViewById(R.id.photo_example2);
        TypedArray photo2 = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);
        int rand = (int)(Math.floor(Math.random() * 3));
        Drawable drawable = photo2.getDrawable(rand);
        choose2.setImageDrawable(drawable);

        TranslateAnimation translate;
        translate = new TranslateAnimation(
                Animation.ABSOLUTE, 900.0f,
                Animation.ABSOLUTE, -500.0f,
                Animation.ABSOLUTE, 250.0f,
                Animation.ABSOLUTE, 250.0f);
        //アニメーション時間(ミリ秒)
        translate.setDuration(6000);
        // 繰り返し時間
        translate.setRepeatCount(0);
        //アニメーションが終わったらそのまま表示にする
        translate.setFillAfter(true);
        //アニメーションの開始
        choose2.startAnimation(translate);
    }

    protected void changePhoto3() {
        ImageView choose3 = findViewById(R.id.photo_example3);
        TypedArray photo3 = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);
        int rand = (int)(Math.floor(Math.random() * 3));
        Drawable drawable = photo3.getDrawable(rand);
        choose3.setImageDrawable(drawable);

        TranslateAnimation translate;
        translate = new TranslateAnimation(
                Animation.ABSOLUTE, -100.0f,
                Animation.ABSOLUTE, 1200.0f,
                Animation.ABSOLUTE, 500.0f,
                Animation.ABSOLUTE, 500.0f);
        //アニメーション時間(ミリ秒)
        translate.setDuration(8000);
        // 繰り返し時間
        translate.setRepeatCount(0);
        //アニメーションが終わったらそのまま表示にする
        translate.setFillAfter(true);
        //アニメーションの開始
        choose3.startAnimation(translate);
    }

}
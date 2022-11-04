package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ConnectActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wii_music);
        mediaPlayer.setLooping(true);


        setOnClick();
        changePhoto();
    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.back_home);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
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

    protected void changePhoto() {
        Button button = findViewById(R.id.changePhoto);
        button.setOnClickListener(v -> {
            // ImageViewを変数化
            ImageView albumArt = findViewById(R.id.random_photo);

            // ランダム画像のリソースの配列を宣言
            TypedArray typedArray = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);

            // ランダムな数値を設定 (*今回は配列の長さが3のため、3を指定)
            int rand = (int)(Math.floor(Math.random() * 3));

            // ランダムで画像を選択する
            Drawable drawable = typedArray.getDrawable(rand);

            // ImageViewの画像の値を設定
            albumArt.setImageDrawable(drawable);
        });
    }
}
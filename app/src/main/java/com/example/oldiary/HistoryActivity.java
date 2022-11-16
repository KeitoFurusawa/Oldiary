package com.example.oldiary;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


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
        //changePhoto();
        //changePhoto2();
        //changePhoto3();
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
        int rand = (int) (Math.floor(Math.random() * 3));
        Drawable drawable = photo1.getDrawable(rand);
        choose1.setImageDrawable(drawable);

        choose1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim));
    }

    protected void changePhoto2() {
        ImageView choose2 = findViewById(R.id.photo_example2);
        TypedArray photo2 = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);
        int rand = (int) (Math.floor(Math.random() * 3));
        Drawable drawable = photo2.getDrawable(rand);
        choose2.setImageDrawable(drawable);

        choose2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim2));
    }

    protected void changePhoto3() {
        ImageView choose3 = findViewById(R.id.photo_example3);
        TypedArray photo3 = getApplicationContext().getResources().obtainTypedArray(R.array.history_arrays);
        int rand = (int) (Math.floor(Math.random() * 3));
        Drawable drawable = photo3.getDrawable(rand);
        choose3.setImageDrawable(drawable);

        choose3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim3));
    }

    private void setElm() {
        ImageButton ibNext = findViewById(R.id.imageButtonNext);
        ImageButton ibPrev = findViewById(R.id.imageButtonPrev);
        TextView post = findViewById(R.id.textViewPost);
    }
}

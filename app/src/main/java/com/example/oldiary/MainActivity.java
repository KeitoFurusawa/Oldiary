package com.example.oldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;

    SoundPool soundPool;
    int back, next;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    MediaPlayer mediaPlayer;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);

        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        if (!preference.getBoolean("Launched", false)) {
            tutorial();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.start);
            mediaPlayer.setLooping(true);
            editor.putBoolean("Launched", true);
            editor.commit();
        } else {
            Intent i = new Intent(MainActivity.this,  MainScreen.class);
            startActivity(i);
            finish();
        }

        ss();

    }

    protected void ss(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
            back = soundPool.load(this, R.raw.back, 1);
            next = soundPool.load(this, R.raw.next, 1);
        }
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

    public void tutorial() {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(back,15 , 15, 0, 0, 2);
                if (getItem(0) > 0) {
                    mSlideViewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(next,15 , 15, 0, 0, 2);
                if (getItem(0) < 3) {
                    mSlideViewPager.setCurrentItem(getItem(1), true);
                }

                else {
                    Intent i = new Intent(MainActivity.this,  MainScreen.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(next,15 , 15, 0, 0, 2);
                Intent i = new Intent(MainActivity.this,  Loading.class);
                startActivity(i);
                finish();
            }
        });

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPaper);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position) {
        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("●"));
            dots[i].setTextSize(35);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            }
            mDotLayout.addView(dots[i]);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dots[position].setTextColor(getResources().getColor(R.color.active, getApplicationContext().getTheme()));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);

            if (position > 0) {
                backbtn.setVisibility(View.VISIBLE);
            }
            else {
                backbtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i) {
        return mSlideViewPager.getCurrentItem() + i;
    }

}
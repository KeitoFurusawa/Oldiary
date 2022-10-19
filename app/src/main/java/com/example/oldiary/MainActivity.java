package com.example.oldiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アクションバー
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //

        ImageView titleLogoView =findViewById(R.id.imageViewTitleLogo);
        titleLogoView.setImageResource(R.drawable.oldiary_title);
        int i = 1024;
        Log.d("This is Debug", Integer.valueOf(i).toString());
    }

}

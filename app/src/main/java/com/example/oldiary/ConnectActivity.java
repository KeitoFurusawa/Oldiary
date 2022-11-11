package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class ConnectActivity extends YouTubeBaseActivity {

    MediaPlayer mediaPlayer;
    private static final String API_KEY = "AIzaSyBtAfSPNfUXI3bUWBf65-nw-50pg9sXyF4";
    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        setOnClick();
        playYoutube();

    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.back_home);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

    protected void playYoutube() {
        mYouTubePlayerView = findViewById(R.id.Youtube_view);

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("WIKKyrGGaDk");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        mYouTubePlayerView.initialize(API_KEY, mOnInitializedListener);
    }

}
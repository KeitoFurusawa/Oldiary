package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

public class AlarmActivity extends Activity {

    static protected MediaPlayer mp;
    static protected AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MediaPlayerインスタンスの生成
        if (mp == null) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wii_music);
            mp = MediaPlayer.create(this, uri);
            mp.setLooping(true);
        }

        if (am == null) {
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
    }

    protected void mStart() {
        if (!mp.isPlaying()) {
            mp.start();
        }
    }

    protected void mStop() {
        if (mp.isPlaying()) {
            mp.stop();
        }
    }

    protected int getMaxVolume() {
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return max;
    }

    protected int getNowVolume() {
        int Vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        return Vol;
    }

    protected void setVolume(int vol) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
    }
}
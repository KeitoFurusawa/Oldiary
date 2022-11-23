package com.example.oldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "history";
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    String userName;
    String userId;
    MediaPlayer mediaPlayer;
    int d_cnt;
    int nowDNum;
    ImageButton ibNext;
    ImageButton ibPrev;
    boolean ibNextStatus;
    boolean ibPrevStatus;
    TextView post;
    TextView postedAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.history);
        mediaPlayer.setLooping(true);
        userId = preference.getString("UserID", "");
        setOnClickBack();
        //changePhoto();
        //changePhoto2();
        //changePhoto3();
        setElm();
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
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        post = findViewById(R.id.textViewPost);
        postedAt = findViewById(R.id.textViewPostedAt);
        roadCnt(); //ポストの数を読み込む
        //Log.d(TAG, String.valueOf(d_cnt)); //debug
    }

    private void roadCnt() {
        //Log.d(TAG, "onRoadCnt->userId: " + userId); //debug
        mDatabase.child("users").child(userId).child("d_cnt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String d_cntResult = String.valueOf(task.getResult().getValue());
                    //Log.d(TAG, "result on roadCnt: " + d_cntResult); //debug
                    if (d_cntResult.equals("null")) { //投稿がない
                        d_cnt = 0;
                        Log.e(TAG, "ERROR: cannot get post");
                    } else {
                        d_cnt = Integer.parseInt(d_cntResult);
                    }
                }
                checkCnt(); //データベースへの問い合わせを確認
            }
        });
    }

    private void checkCnt() {
        if (d_cnt == 0) { //投稿がない
            post.setVisibility(View.GONE);
            ibNext.setVisibility(View.GONE);
            ibPrev.setVisibility(View.GONE);
            postedAt.setText("投稿がありません");
        } else { //投稿がある
            nowDNum = 1;
            if (d_cnt > 1) { //Postが2以上
                disableIB("l"); //左ボタンを薄く
                enableIB("r");
            } else { // Postが1つ
                disableIB("l"); //左ボタンを薄く
                disableIB("r"); //右ボタンを薄く
            }
            setDiaryText(); //投稿のテキストをセット
            setDiaryDateTime(); //投稿日時をセット
            setOnClickNext();
            setOnClickPrev();
        }
    }

    private void setDiaryText() {
        String d_id = String.format("d_%s%d", userId, nowDNum);
        mDatabase.child("users").child(userId).child("diaries").child(d_id).child("text").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String textResult = String.valueOf(task.getResult().getValue());
                    if (textResult.equals("null")) { //中身がない
                        Log.e(TAG, "ERROR: cannot get data"); //debug
                    } else {
                        post.setText(textResult);
                    }
                }
            }
        });
    }

    private void setDiaryDateTime() {
        String d_id = String.format("d_%s%d", userId, nowDNum);
        mDatabase.child("users").child(userId).child("diaries").child(d_id).child("postedAt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String textResult = String.valueOf(task.getResult().getValue());
                    if (textResult.equals("null")) { //中身がない
                        Log.e(TAG, "ERROR: cannot get data"); //debug
                    } else {
                        postedAt.setText(textResult);
                    }
                }
            }
        });
    }

    private void setOnClickNext() {
        ibNext.setOnClickListener(v -> {
            if (!ibNextStatus) { //最後に到達している
                Log.d(TAG, "button was disabled"); //debug
            } else {
                nowDNum++;
                if (!ibPrevStatus) {
                    enableIB("l"); //左を濃くする
                }
                if (nowDNum == d_cnt) {
                    disableIB("r"); //右を薄くする
                }
                setDiaryText();
                setDiaryDateTime();
            }
        });
    }

    private void setOnClickPrev() {
        ibPrev.setOnClickListener(v -> {
            if (!ibPrevStatus) { //最初に達している
                Log.d(TAG, "button was disabled"); //debug
            } else {
                nowDNum--;
                if (!ibNextStatus) {
                    enableIB("r"); //右を濃くする
                }
                if (nowDNum == 1) {
                    disableIB("l"); //左を薄くする
                }
                setDiaryText();
                setDiaryDateTime();
            }
        });
    }

    private void disableIB(String lr) {
        if (lr.equals("l")) { //prev
            ibPrevStatus = false;
            ibPrev.setImageResource(R.drawable.prev_disable);
        } else {              //next
            ibNextStatus = false;
            ibNext.setImageResource(R.drawable.next_disable);
        }
    }

    private void enableIB(String lr) {
        if (lr.equals("l")) { //prev
            ibPrevStatus = true;
            ibPrev.setImageResource(R.drawable.prev);
        } else {              //next
            ibNextStatus = true;
            ibNext.setImageResource(R.drawable.next);
        }
    }
}

package com.example.oldiary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "history";
    private ImageView avatarObj;
    private ProgressDialog progressDialog;
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
    Button ibReload;
    boolean ibNextStatus;
    boolean ibPrevStatus;
    TextView post;
    TextView postedAt;
    ArrayList<String> d_idList;
    private String gender = "null";
    private String color = "null";
    private boolean checkID = false, checkPost= false, checkAvatar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("自分の投稿");
        }
        StartLoading();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.history);
        mediaPlayer.setLooping(true);
        userId = preference.getString("UserID", "");
        checkID = true;
        d_idList = new ArrayList<String>();
        setOnClickBack();
        setElm();
        loadPrevAvatar();
        setOnClickReload();
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
        Button button = findViewById(R.id.back_home);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

    private void setElm() {
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibReload = findViewById(R.id.imageButtonReload);
        post = findViewById(R.id.textViewPost);
        postedAt = findViewById(R.id.textViewPostedAt);
        roadDList(); //ポストのidのリストを読み込む
        roadCnt(); //ポストの数を読み込む
        //Log.d(TAG, String.valueOf(d_cnt)); //debug
    }

    private void roadDList() {
        mDatabase.child("users").child(userId).child("diaries").child("d_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String d_idListResult = String.valueOf(task.getResult().getValue());
                    //Log.d(TAG, "result on roadCnt: " + d_cntResult); //debug
                    if (d_idListResult.equals("null")) { //投稿がない
                        Log.e(TAG, "ERROR: cannot get post");
                    } else {
                        String[] split = d_idListResult.split(",");
                        for (String xs : split) {
                            d_idList.add(xs);
                        }
                    }
                }
            }
        });
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
        //String d_id = String.format("d_%s%d", userId, nowDNum);
        String d_id = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("text").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                        checkPost = true;
                        checkLoading();
                    }
                }
            }
        });
    }

    private void setDiaryDateTime() {
        //String d_id = String.format("d_%s%d", userId, nowDNum);
        String d_id = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("postedAt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(HistoryActivity.this)
                            .setTitle("エラー")
                            .setMessage("データの取得に失敗しました。\nネットワークに接続してください。")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OKボタン押下時の処理
                                    Intent intent2 = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent2);
                                }
                            })
                            .show();
                }
                else {
                    String textResult = String.valueOf(task.getResult().getValue());
                    if (textResult.equals("null")) { //中身がない
                        Log.e(TAG, "ERROR: cannot get data"); //debug
                    } else {
                        postedAt.setText(textResult);
                        checkPost = true;
                        checkLoading();
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
                //StartLoading();
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
                //StartLoading();
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

    private void setOnClickReload() {
        ibReload.setOnClickListener(v -> {
            StartLoading();
            setElm();
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

    private void loadPrevAvatar() {
        mDatabase.child("users").child(userId).child("avatar").child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    gender = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + gender);
                    if (gender.equals("null")) {
                        gender = "man";
                    }
                    Log.d(TAG, gender);
                }
            }
        });
        mDatabase.child("users").child(userId).child("avatar").child("color").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    color = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + color);
                    if (color.equals("null")) {
                        color = "blue";
                    }
                    Log.d(TAG, color);
                    setPrevAvatar();
                }
            }
        });
    }

    private void setPrevAvatar() {
        while(gender.equals("null") || color.equals("null")) {
            Log.d(TAG, gender+color);
        }
        int drawableId = getResources().getIdentifier(color+"_"+gender, "drawable", getPackageName());
        ImageView imageView = findViewById(R.id.avatar);
        imageView.setImageResource(drawableId);
        checkAvatar = true;
        checkLoading();
    }

    private void StartLoading() {
        checkPost = false;
        avatarObj = findViewById(R.id.avatar);
        if (!checkAvatar) {
            avatarObj.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        if (checkID && checkAvatar && checkPost) {
            progressDialog.dismiss();
            avatarObj.setVisibility(View.VISIBLE);
        }
    }
}

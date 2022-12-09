package com.example.oldiary;

import android.app.AlertDialog;
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
import java.util.Collections;

public class ConnectActivity extends AppCompatActivity {
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
    Button ibReload;
    boolean ibNextStatus;
    boolean ibPrevStatus;
    TextView post;
    TextView postedAt;
    TextView postedBy;
    ArrayList<String> d_idList;
    private static final String API_KEY = "AIzaSyBtAfSPNfUXI3bUWBf65-nw-50pg9sXyF4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("みんなの投稿");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.history);
        mediaPlayer.setLooping(true);
        userId = preference.getString("UserID", "");
        setUserName();
        d_idList = new ArrayList<String>();
        setOnClickBack();
        setElm();
        setOnClickReload();
        setOnClickNewDiary();
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

    protected void setOnClickNewDiary() {
        Button button = findViewById(R.id.buttonNewDiary);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });
    }

    private void setElm() {
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibReload = findViewById(R.id.imageButtonReload);
        post = findViewById(R.id.textViewPost);
        postedAt = findViewById(R.id.textViewPostedAt);
        postedBy = findViewById(R.id.textViewPostedBy);
        roadPublicDList(); //ポストのidのリストを読み込む
        roadPublicCnt(); //ポストの数を読み込む
    }

    private void roadPublicDList() {
        mDatabase.child("diaries").child("d_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String d_idListResult = String.valueOf(task.getResult().getValue());
                    if (d_idListResult.equals("null")) { //投稿がない
                        Log.e(TAG, "ERROR: cannot get post");
                    } else {
                        String[] split = d_idListResult.split(",");
                        for (String xs : split) {
                            d_idList.add(xs);
                        }
                        Collections.reverse(d_idList);
                    }
                }
            }
        });
    }

    private void roadPublicCnt() {
        mDatabase.child("diaries").child("dCntPublic").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
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
                checkPublicCnt(); //データベースへの問い合わせを確認
            }
        });
    }

    private void checkPublicCnt() {
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
            setDiaryDateTime();//投稿日時をセット
            setPostedBy();
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
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
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
        //String d_id = String.format("d_%s%d", userId, nowDNum);
        String d_id = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("postedAt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(ConnectActivity.this)
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
                    }
                }
            }
        });
    }

    private void setPostedBy() {
        String d_id = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("postedBy").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(ConnectActivity.this)
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
                        innerSetPostedBy(textResult);
                    }
                }
            }
        });
    }

    private void innerSetPostedBy(String userId) {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(ConnectActivity.this)
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
                        postedBy.setText("NoDATA");
                    } else {
                        postedBy.setText(textResult + " さんの投稿");
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
                setPostedBy();
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
                setPostedBy();
            }
        });
    }

    private void setOnClickReload() {
        ibReload.setOnClickListener(v -> {
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

    private void setUserName() {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    new AlertDialog.Builder(ConnectActivity.this)
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
                    String userNameResult = String.valueOf(task.getResult().getValue());
                    if (userNameResult.equals("null")) { //初回ログイン
                        Log.e(TAG, "ERROR: cannot get name");
                    } else {
                        userName = userNameResult;
                    }
                }
            }
        });
    }
}
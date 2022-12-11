package com.example.oldiary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
    private static final String TAG = "connect";
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    String userId;
    private String dstUserId;
    private String dstDiaryId;
    private boolean fromR = false;
    private boolean fromNR = false;
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
    private boolean checkID = false, checkPost = false, checkUserName = false;
    private ProgressDialog progressDialog;

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
        checkID = true;
        d_idList = new ArrayList<String>();
        checkFROM();
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
            startActivity(intent);
        });
    }

    private void checkFROM() {
        Intent i = getIntent();
        String intentFrom = i.getStringExtra("INTENT_FROM");
        if (intentFrom.equals("reply")) {
            fromR = true;
            nowDNum = i.getIntExtra("D-NUM", -1);
            Log.d(TAG, "R/DNum: " + nowDNum);
        } else if (intentFrom.equals("notReplied")) {
            fromNR = true;
            nowDNum = i.getIntExtra("D-NUM", -1);
            Log.d(TAG, "NR/DNum: " + nowDNum);
        }
    }

    private void doneReply() {
        Intent i = getIntent();
        Toast.makeText(ConnectActivity.this, String.format("%sさんへの返信を送信しました", i.getStringExtra("REPLY_TO")), Toast.LENGTH_SHORT).show();
        fromR = false;
    }

    private void setElm() {
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibReload = findViewById(R.id.imageButtonReload);
        post = findViewById(R.id.textViewPost);
        post.setMovementMethod(new ScrollingMovementMethod());
        postedAt = findViewById(R.id.textViewPostedAt);
        postedBy = findViewById(R.id.textViewPostedBy);
        StartLoading();
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
            if (!(fromR || fromNR)) {
                Log.d(TAG, "ref");
                nowDNum = 1;
                if (d_cnt > 1) { //Postが2以上
                    disableIB("l"); //左ボタンを薄く
                    enableIB("r");
                } else { // Postが1つ
                    disableIB("l"); //左ボタンを薄く
                    disableIB("r"); //右ボタンを薄く
                }
            } else {
                if (d_cnt > 1) { //Postが2以上
                    if (nowDNum == 1) {
                        disableIB("l"); //左ボタンを薄く
                        enableIB("r");
                    } else if (nowDNum == d_cnt) { //末端
                        enableIB("l");
                        disableIB("r");
                    } else {
                        enableIB("l");
                        enableIB("r");
                    }
                } else { // Postが1つ
                    disableIB("l"); //左ボタンを薄く
                    disableIB("r"); //右ボタンを薄く
                }
            }
            setDiaryText(); //投稿のテキストをセット
            setDiaryDateTime();//投稿日時をセット
            setPostedBy();
            setOnClickNext();
            setOnClickPrev();
            setOnClickReply();
        }
    }

    private void setDiaryText() {
        //String d_id = String.format("d_%s%d", userId, nowDNum);
        String d_id = d_idList.get(nowDNum-1);
        dstDiaryId = d_idList.get(nowDNum-1);
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
                        dstUserId = textResult;
                        innerSetPostedBy(textResult);
                        checkPost = true;
                        checkLoading();
                    }
                }
            }
        });
    }

    private void innerSetPostedBy(String userId) {
        checkButtonVisibility(userId);
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
                        checkUserName = true;
                        checkLoading();
                    }
                }
            }
        });
    }

    private void checkButtonVisibility(String userId) {
        Button buttonReply = findViewById(R.id.buttonReply);
        if (userId.equals(this.userId)) {
            buttonReply.setVisibility(View.GONE);
        } else {
            buttonReply.setVisibility(View.VISIBLE);
        }
    }


    private void setOnClickNext() {
        ibNext.setOnClickListener(v -> {
            if (!ibNextStatus) { //最後に到達している
                //Log.d(TAG, "button was disabled"); //debug
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
                //Log.d(TAG, "button was disabled"); //debug
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

    private void setOnClickReply() {
        Button buttonReply = findViewById(R.id.buttonReply);
        buttonReply.setOnClickListener(v -> {
            Intent i = new Intent(getApplication(), ReplyActivity.class);
            i.putExtra("dstUserID", dstUserId);
            i.putExtra("dstDiaryID", dstDiaryId);
            i.putExtra("D-NUM", nowDNum);
            startActivity(i);
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



    private void StartLoading() {
        checkPost = false;
        checkUserName = false;
        post.setVisibility(View.GONE);
        postedBy.setVisibility(View.GONE);
        postedAt.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        if (checkID && checkPost && checkUserName) {
            post.setVisibility(View.VISIBLE);
            postedBy.setVisibility(View.VISIBLE);
            postedAt.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            if (fromR) {
                doneReply();
            }
        }
    }
}
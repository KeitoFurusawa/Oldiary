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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ConnectActivity extends AppCompatActivity {
    private static final String TAG = "connect";
    private static final String SPTAG = "bugcheck";
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    String userId;
    private CardView cv;
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
    ListView listView;
    ArrayList<String> d_idList;
    private static final String API_KEY = "AIzaSyBtAfSPNfUXI3bUWBf65-nw-50pg9sXyF4";
    private boolean checkID = false, checkPost = false, checkUserName = false, checkReply = false;
    private ProgressDialog progressDialog;


    //ここからリプライに使う
    private ArrayList<String> r_idList;
    private ArrayList<String> text;
    private ArrayList<String> repliedBy;
    private ArrayList<String> userName;
    private ArrayList<String> repliedAt;
    private ArrayList<Integer> iconId;
    private ArrayList<String> gender;
    private ArrayList<String> color;
    private ArrayList<ReplyData> listItems = new ArrayList<>();
    private TextView repTitle;
    ListView listViewReply;
    private boolean repBtnVisible = false;
    private Button buttonReply;

    //ジャンル
    private int loadGCnt;
    private TextView textViewGenre;

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
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.forconnect);
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
        cv = findViewById(R.id.writeSpace);
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibReload = findViewById(R.id.imageButtonReload);
        post = findViewById(R.id.textViewPost);
        post.setMovementMethod(new ScrollingMovementMethod());
        postedAt = findViewById(R.id.textViewPostedAt);
        postedBy = findViewById(R.id.textViewPostedBy);
        listView = findViewById(R.id.listViewReplies);
        textViewGenre = findViewById(R.id.textGenre);
        repTitle = findViewById(R.id.textTitleReply);
        buttonReply = findViewById(R.id.buttonReply);
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
                        Log.i(SPTAG, String.valueOf(d_idList));
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
            textViewGenre.setVisibility(View.GONE);
            postedAt.setText("投稿がありません");
            repTitle.setVisibility(View.GONE);
        } else { //投稿がある
            if (!(fromR || fromNR)) {
                //Log.d(TAG, "ref");
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
        StartLoading();
        String d_id = d_idList.get(nowDNum-1);
        //Log.i(SPTAG, d_id);
        dstDiaryId = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("text").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String textResult = String.valueOf(task.getResult().getValue());
                    if (textResult.equals("null")) { //中身がない
                        //Log.e(TAG, "ERROR: cannot get data"); //debug
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
                    //Log.e(TAG, "Error getting data", task.getException());
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
                        //Log.e(TAG, "ERROR: cannot get data"); //debug
                    } else {
                        postedAt.setText(textResult);
                    }
                }
            }
        });
    }

    private void setGenre() {
        loadGCnt = 0;
        //Log.i(TAG, "setGenre()");
        String d_id = d_idList.get(nowDNum-1);
        ArrayList<String> selectedGenreList = new ArrayList<>();
        for (int i = 0; i < PutGenreAdapter.MAX_LENGTH_P; i++) {
            mDatabase.child("diaries").child(d_id).child("genre").child(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        //Log.e(TAG, "Error getting data", task.getException());
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
                        String result = String.valueOf(task.getResult().getValue());
                        //Log.i(TAG, String.format("%d, %s", loadGCnt, result));
                        if (result.equals("null")) { //中身がない
                            //Log.e(TAG, "ERROR: cannot get genre"); //debug
                        } else {
                            selectedGenreList.add(GenreData.genreList[Integer.parseInt(result)]);
                        }
                        loadGCnt++;
                        if (loadGCnt == PutGenreAdapter.MAX_LENGTH_P) {
                            //Log.i(TAG, String.valueOf(selectedGenreList));
                            if (selectedGenreList.size() == 0) {
                                textViewGenre.setText("#ジャンルなし");
                            } else {
                                StringBuffer sb = new StringBuffer();
                                for (int j = 0; j < selectedGenreList.size(); j++) {
                                    sb.append("#" + selectedGenreList.get(j)).append(" ");
                                }
                                textViewGenre.setText(sb.toString());
                            }
                        }
                    }
                }
            });
        }
    }

    private void setPostedBy() {
        String d_id = d_idList.get(nowDNum-1);
        mDatabase.child("diaries").child(d_id).child("postedBy").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
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
                        //Log.e(TAG, "ERROR: cannot get data"); //debug
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
                    //Log.e(TAG, "Error getting data", task.getException());
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
                        //Log.e(TAG, "ERROR: cannot get data"); //debug
                        postedBy.setText("NoDATA");
                    } else {
                        postedBy.setText(textResult + " さんの投稿");
                        checkUserName = true;
                        setGenre(); ///
                        getRepIdList();
                        checkLoading();
                    }
                }
            }
        });
    }

    private void checkButtonVisibility(String userId) {
        buttonReply = findViewById(R.id.buttonReply);
        if (userId.equals(this.userId)) {
            repBtnVisible = false;
        } else {
            repBtnVisible = true;
        }
    }

    private void setOnClickNext() {
        ibNext.setOnClickListener(v -> {
            if (!ibNextStatus) { //最後に到達している
                //Log.d(TAG, "button was disabled"); //debug
            } else {
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_left));
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
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_right));
            }
        });
    }

    private void setOnClickPrev() {
        ibPrev.setOnClickListener(v -> {
            if (!ibPrevStatus) { //最初に達している
                //Log.d(TAG, "button was disabled"); //debug
            } else {
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_right));
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
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_left));
            }
        });
    }

    private void setOnClickReload() {
        ibReload.setOnClickListener(v -> {
            setElm();
        });
    }

    private void setOnClickReply() {
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

    private void getRepIdList() {
        //Log.i(TAG, "getRepIdList()");
        String d_id = d_idList.get(nowDNum - 1);
        r_idList = new ArrayList<>();
        r_idList.clear();

        mDatabase.child("diaries").child(d_id).child("r_idList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String r_idListResult = String.valueOf(task.getResult().getValue());
                    if (r_idListResult.equals("null")) { //返信がない
                        //Log.e(TAG, "ERROR: cannot get post");
                        listView.setVisibility(View.GONE);
                        TextView text = findViewById(R.id.textTitleReply);
                        text.setText("返信がありません");
                        checkReply = true;
                        checkLoading();
                    } else {
                        String[] split = r_idListResult.split(",");
                        r_idList.clear();
                        for (String xs : split) {
                            r_idList.add(xs);
                        }
                        Collections.reverse(r_idList);
                        //Log.i(TAG, String.valueOf(r_idList)); //debug
                        listView.setVisibility(View.VISIBLE);
                        TextView text = findViewById(R.id.textTitleReply);
                        text.setText("返信一覧");
                        setReplies();
                    }
                }
            }
        });
    }

    private void setReplies() {
        listViewReply = findViewById(R.id.listViewReplies);
        String d_id = d_idList.get(nowDNum - 1);
        listItems.clear();
        text = new ArrayList<>();
        //text.clear();
        repliedBy = new ArrayList<>();
        //repliedBy.clear();
        userName = new ArrayList<>();
        //userName.clear();
        repliedAt = new ArrayList<>();
        //repliedAt.clear();
        iconId = new ArrayList<>();
        //iconId.clear();
        gender = new ArrayList<>();
        //gender.clear();
        color = new ArrayList<>();
        //color.clear();
        for (int i = 0; i < r_idList.size(); i++) {
            text.add("");
            repliedBy.add("");
            userName.add("");
            repliedAt.add("");
            iconId.add(-1);
            gender.add("");
            color.add("");
        }
        int index;
        for (String r_id : r_idList) {
            index = r_idList.indexOf(r_id);
            loadText(d_id, r_id, index);
        }
    }



    private void loadText(String d_id, String r_id, int index) {
        //テキストを読み込み
        mDatabase.child("diaries").child(d_id).child(r_id).child("text").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        text.set(index, result);
                        loadRepliedBy(d_id, r_id, index);
                        //Log.i(TAG, String.format("fin load text [%s]", text.get(index)));
                    }
                }
            }
        });
    }

    private void loadRepliedBy(String d_id, String r_id, int index) {
        //リプライ者IDを読み込み
        mDatabase.child("diaries").child(d_id).child(r_id).child("repliedBy").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        repliedBy.set(index, result);
                        loadUserName(d_id, r_id, index);
                        //Log.i(TAG, String.format("fin load repliedBy [%s]", repliedBy.get(index)));
                    }
                }
            }
        });
    }

    private void loadUserName(String d_id, String r_id, int index) {
        //リプライ者名を読み込み
        mDatabase.child("users").child(repliedBy.get(index)).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        userName.set(index, result);
                        loadRepliedAt(d_id, r_id, index);
                        //Log.i(TAG, String.format("fin load userName [%s]", userName.get(index)));
                    }
                }
            }
        });
    }

    private void loadRepliedAt(String d_id, String r_id, int index) {
        //リプライ日時を読み込み
        mDatabase.child("diaries").child(d_id).child(r_id).child("repliedAt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(ConnectActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");

                    } else {
                        repliedAt.set(index, result);
                        loadPrevAvatar(repliedBy.get(index), index);
                        //Log.i(TAG, String.format("fin load repliedAt [%s]", repliedAt.get(index)));
                    }
                }
            }
        });
    }

    private void loadPrevAvatar(String repliedBy, int index) {
        mDatabase.child("users").child(repliedBy).child("avatar").child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    gender.set(index, String.valueOf(task.getResult().getValue()));
                    //Log.d(TAG, "result: " + gender);
                    if (gender.get(index).equals("null")) {
                        gender.set(index, "man");
                    }
                    //Log.i(TAG, gender.get(index));
                }
            }
        });
        mDatabase.child("users").child(repliedBy).child("avatar").child("color").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    color.set(index, String.valueOf(task.getResult().getValue()));
                    //Log.d(TAG, "result: " + color);
                    if (color.get(index).equals("null")) {
                        color.set(index, "blue");
                    }
                    //Log.i(TAG, color.get(index));
                    setPrevAvatar(index);
                }
            }
        });
    }

    private void setPrevAvatar(int index) {
        while(gender.equals("null") || color.equals("null")) {
            //Log.d(TAG, gender.get(index)+color.get(index));
        }
        iconId.set(index, getResources().getIdentifier("icon_"+color.get(index)+"_"+gender.get(index), "drawable", getPackageName()));
        checkGetReplyData(index);
        //Log.i(TAG, String.format("fin load iconId [%d]", iconId.get(index)));
    }

    private void checkGetReplyData(int index) {
        ReplyData item = new ReplyData(text.get(index), repliedBy.get(index), userName.get(index), repliedAt.get(index), iconId.get(index));
        listItems.add(item);
        if (index+1 == r_idList.size()) {
            //Log.i(TAG, "All FIN");
            ReplyListAdapter adapter = new ReplyListAdapter(this, R.layout.replylist_item, listItems);
            listViewReply.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //Log.i(TAG, "r_idList: " + String.valueOf(r_idList));
            checkReply = true;
            checkLoading();
        }
            index++;
    }


    private void StartLoading() {
        checkPost = false;
        checkUserName = false;
        checkReply = false;
        post.setVisibility(View.GONE);
        postedBy.setVisibility(View.GONE);
        postedAt.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        textViewGenre.setVisibility(View.GONE);
        repTitle.setVisibility(View.GONE);
        ibNext.setVisibility(View.GONE);
        ibPrev.setVisibility(View.GONE);
        buttonReply.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        if (checkID && checkPost && checkUserName && checkReply) {
            post.setVisibility(View.VISIBLE);
            postedBy.setVisibility(View.VISIBLE);
            postedAt.setVisibility(View.VISIBLE);
            textViewGenre.setVisibility(View.VISIBLE);
            repTitle.setVisibility(View.VISIBLE);
            ibNext.setVisibility(View.VISIBLE);
            ibPrev.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            if (repBtnVisible) {
                buttonReply.setVisibility(View.VISIBLE);
            }
            if (fromR) {
                doneReply();
            }
        }
    }

}
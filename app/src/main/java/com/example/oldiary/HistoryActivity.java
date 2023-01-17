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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "history";
    private ProgressDialog progressDialog;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    private CardView cv;
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
    ListView listView;
    ArrayList<String> d_idList;
    private String gender = "null";
    private String color = "null";
    private boolean checkID = false, checkPostText = false, checkPostTime = false, checkReply = false;

    //ジャンル
    private int loadGCnt;
    private TextView textViewGenre;

    //ここからリプライに使う
    private ArrayList<String> r_idList;
    private ArrayList<String> text;
    private ArrayList<String> repliedBy;
    private ArrayList<String> userNameR;
    private ArrayList<String> repliedAt;
    private ArrayList<Integer> iconId;
    private ArrayList<String> genderR;
    private ArrayList<String> colorR;
    private ArrayList<ReplyData> listItems = new ArrayList<>();
    private TextView repTitle;
    ListView listViewReply;

    private boolean fromNR = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("自分の投稿");
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

    private void checkFROM() {
        Intent i = getIntent();
        String intentFrom = i.getStringExtra("INTENT_FROM");
        if ((intentFrom != null) && (intentFrom.equals("notReplied") || intentFrom.equals("profile"))) {
            fromNR = true;
            nowDNum = i.getIntExtra("D-NUM", -1);
            //Log.d(TAG, "NR/DNum: " + nowDNum);
        }
    }

    private void setElm() {
        cv = findViewById(R.id.writeSpace);
        ibNext = findViewById(R.id.imageButtonNext);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibReload = findViewById(R.id.imageButtonReload);
        post = findViewById(R.id.textViewPost);
        post.setMovementMethod(new ScrollingMovementMethod());
        postedAt = findViewById(R.id.textViewPostedAt);
        textViewGenre = findViewById(R.id.textGenre);
        listView = findViewById(R.id.listViewReplies);
        repTitle = findViewById(R.id.textTitleReply);
        roadDList(); //ポストのidのリストを読み込む
        roadCnt(); //ポストの数を読み込む
        ////Log.(TAG, String.valueOf(d_cnt)); //debug
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
                    ////Log.(TAG, "result on roadCnt: " + d_cntResult); //debug
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
        ////Log.(TAG, "onRoadCnt->userId: " + userId); //debug
        mDatabase.child("users").child(userId).child("d_cnt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                }
                else {
                    String d_cntResult = String.valueOf(task.getResult().getValue());
                    ////Log.(TAG, "result on roadCnt: " + d_cntResult); //debug
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
            post.setVisibility(View.INVISIBLE);
            ibNext.setVisibility(View.INVISIBLE);
            ibPrev.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.GONE);
            //avatarObj.setVisibility(View.VISIBLE);
            postedAt.setText("投稿がありません");
            skipCheck();
        } else { //投稿がある
            if (!fromNR) {
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
            setDiaryDateTime(); //投稿日時をセット
            setOnClickNext();
            setOnClickPrev();
        }
    }

    private void setDiaryText() {
        StartLoading();
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
                        checkPostText = true;
                    } else {
                        post.setText(textResult);
                        checkPostText = true;
                        setGenre();
                        getRepIdList();
                        //Log.d(TAG, "1: setDiaryText()");
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
                        checkPostTime = true;
                        checkLoading();
                    } else {
                        postedAt.setText(textResult);
                        checkPostTime = true;
                        //Log.d(TAG, "2: setDiaryDateTime()");
                        checkLoading();
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
                                //Log.d(TAG, "3: setGenre()");
                                textViewGenre.setText(sb.toString());
                            }
                        }
                    }
                }
            });
        }
    }

    private void setOnClickNext() {
        ibNext.setOnClickListener(v -> {
            if (!ibNextStatus) { //最後に到達している
                //Log.(TAG, "button was disabled"); //debug
            } else {
                //StartLoading();
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
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_right));
            }
        });
    }

    private void setOnClickPrev() {
        ibPrev.setOnClickListener(v -> {
            if (!ibPrevStatus) { //最初に達している
                //Log.(TAG, "button was disabled"); //debug
            } else {
                //StartLoading();
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
                cv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_left));
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
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
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
                        //Log.d(TAG, "4: getRepIdList()");
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
        repliedBy = new ArrayList<>();
        userNameR = new ArrayList<>();
        repliedAt = new ArrayList<>();
        iconId = new ArrayList<>();
        genderR = new ArrayList<>();
        colorR = new ArrayList<>();
        for (int i = 0; i < r_idList.size(); i++) {
            text.add("");
            repliedBy.add("");
            userNameR.add("");
            repliedAt.add("");
            iconId.add(-1);
            genderR.add("");
            colorR.add("");
        }
        int index;
        //Log.d(TAG, "5: setReplies()");
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
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        //Log.d(TAG, "6: loadText()");
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
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        //Log.d(TAG, "7: loadRepliedBy()");
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
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");
                    } else {
                        //Log.d(TAG, "8: loadUserName()");
                        userNameR.set(index, result);
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
                    Toast.makeText(HistoryActivity.this, "データの取得に失敗しました。\nネットワークに接続してください。", Toast.LENGTH_SHORT).show();
                } else {
                    String result = String.valueOf(task.getResult().getValue());
                    if (result.equals("null")) { //エラー
                        //Log.e(TAG, "ERROR: cannot get data");

                    } else {
                        //Log.d(TAG, "9: loadRepliedAt()");
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
                    genderR.set(index, String.valueOf(task.getResult().getValue()));
                    //Log.d(TAG, "result: " + gender);
                    if (genderR.get(index).equals("null")) {
                        genderR.set(index, "man");
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
                    colorR.set(index, String.valueOf(task.getResult().getValue()));
                    //Log.d(TAG, "result: " + color);
                    if (colorR.get(index).equals("null")) {
                        colorR.set(index, "blue");
                    }
                    //Log.d(TAG, "10: loadPrevAvatar()");
                    setPrevAvatar(index);
                }
            }
        });
    }

    private void setPrevAvatar(int index) {
        while(genderR.equals("null") || colorR.equals("null")) {
            //Log.d(TAG, genderR.get(index)+colorR.get(index));
        }
        iconId.set(index, getResources().getIdentifier("icon_"+colorR.get(index)+"_"+genderR.get(index), "drawable", getPackageName()));
        checkGetReplyData(index);
        //Log.d(TAG, "11: setPrevAvatar()");
    }

    private void checkGetReplyData(int index) {
        ReplyData item = new ReplyData(text.get(index), repliedBy.get(index), userNameR.get(index), repliedAt.get(index), iconId.get(index));
        listItems.add(item);
        //Log.d(TAG, "12: checkGetReplyData()");
        if (index+1 == r_idList.size()) {
            //Log.i(TAG, "All FIN");
            ReplyListAdapter adapter = new ReplyListAdapter(this, R.layout.replylist_item, listItems);
            listViewReply.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listViewReply.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String repUserId = repliedBy.get(position);
                            Log.d(TAG, String.valueOf(position) + repUserId);
                            Intent intent = new Intent(getApplication(), ProfileActivity.class);
                            intent.putExtra("FROM", "history");
                            intent.putExtra("LOOK_AT", repUserId);
                            intent.putExtra("D-NUM", nowDNum);
                            startActivity(intent);
                        }
                    }
            );
            checkReply = true;
            checkLoading();
        }
        index++;
    }


    private void skipCheck() {
        //checkID = true;
        checkPostText = true;
        checkPostTime = true;
        checkLoading();
    }

    private void StartLoading() {
        checkPostTime = false;
        checkPostText = false;
        checkReply = false;
        post.setVisibility(View.GONE);
        postedAt.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        textViewGenre.setVisibility(View.GONE);
        repTitle.setVisibility(View.GONE);
        ibNext.setVisibility(View.GONE);
        ibPrev.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        if (checkID && checkPostTime && checkPostText && checkReply) {
            post.setVisibility(View.VISIBLE);
            postedAt.setVisibility(View.VISIBLE);
            textViewGenre.setVisibility(View.VISIBLE);
            repTitle.setVisibility(View.VISIBLE);
            ibNext.setVisibility(View.VISIBLE);
            ibPrev.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            progressDialog.dismiss();
        }
    }
}

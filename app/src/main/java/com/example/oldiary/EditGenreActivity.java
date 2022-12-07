package com.example.oldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class EditGenreActivity extends AppCompatActivity {
    private static ArrayList<Integer> selectedGenreList = new ArrayList<>();//
    public static boolean bStates = false;//
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String userId;
    private String userName;
    private String comment;
    private final static String TAG = "edit";
    private static final String[] textData = {
            "サッカー", "野球", "テニス", "ガーデニング", "読書",
            "ピアノ", "ゴルフ", "映画鑑賞", "音楽鑑賞", "散歩",
            "ランニング", "料理", "ボランティア", "将棋", "囲碁",
            "カラオケ", "旅行", "ワープロ", "手芸", "ギター",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_genre);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        bStates = true;
        selectedGenreList = new ArrayList<Integer>();
        Intent intent1 = getIntent();
        getUserId();
        userName = intent1.getStringExtra("UserName");
        comment = intent1.getStringExtra("Comment");
        Log.d(TAG, String.format("check intent: %s %s %s", userId, userName, comment)); //

        setOnClick(); //確定ボタン

        ListView listView = findViewById(R.id.listViewGenre);
        List<GenreData> list = new ArrayList<>();
        for (int i = 0; i < textData.length; i++) {
            GenreData genreData = new GenreData();
            genreData.setTextData(textData[i]);
            //genreData.setChecked(true);
            list.add(genreData);
        }
        MyAdapter2 adapter = new MyAdapter2(EditGenreActivity.this,R.layout.genre_list,list);
        loadData(adapter, listView);
        //listView.setOnClickListener();
        /*
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "pushed pos: " + position);
                        adapter.onClickElm(position);
                    }
        });
        */
    }

    protected void getUserId() {
        userId = preference.getString("UserID", "");
    }

    private void loadData(MyAdapter2 adapter2, ListView listView) {
        mDatabase.child("users").child(userId).child("favoriteGenre").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String Result = String.valueOf(task.getResult().getValue());
                    String reResult = Result.replace("[", "").replace("]", "").replace(" ", "");
                    String[] split = reResult.split(",");
                    for (String xs : split) {
                        addSelectedGenreListList(Integer.parseInt(xs));
                    }
                    adapter2.alreadyChecked(selectedGenreList);
                    listView.setAdapter(adapter2);
                }
            }
        });
    }


    public void addSelectedGenreListList(int pos) {
        this.selectedGenreList.add(pos);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        Log.d(TAG, sb.toString());
    }

    public void removeSelectedGenreListList(int pos) {
        this.selectedGenreList.remove(selectedGenreList.indexOf(pos));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        Log.d(TAG, sb.toString());
    }

    public int sizeOfSelectedGenreList() {
        return selectedGenreList.size();
    }


    public void enableButton() { //チェック済みが3つになったら呼ぶ
        //Button b = findViewById(R.id.buttonConfirm);
        //ここで色をはっきりさせたい
        bStates = true; //ボタンの有効化
    }

    public void disableButton() { //チェック済みが３未満になったら呼ぶ
        //Button b = findViewById(R.id.buttonConfirm);
        //ここで色を薄くしたい
        bStates = false;
    }

    //確定ボタン
    protected void setOnClick() {
        Button button = findViewById(R.id.buttonConfirm);
        button.setOnClickListener(v -> {
            Log.d(TAG, String.valueOf("Button States: " + bStates)); //有効
            if (bStates) { //ボタン有効時
                new AlertDialog.Builder(EditGenreActivity.this)
                        .setTitle("確認")
                        .setMessage(String.format("名前: %s\nひとことコメント: %s\nジャンル: %s,%s,%s\n\n以上の内容でプロフィールの編集を完了しますか？",
                                userName, comment, textData[selectedGenreList.get(0)], textData[selectedGenreList.get(1)], textData[selectedGenreList.get(2)]))
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.child("users").child(userId).child("userName").setValue(userName);
                                mDatabase.child("users").child(userId).child("comment").setValue(comment);
                                mDatabase.child("users").child(userId).child("favoriteGenre").setValue(selectedGenreList); //firebaseにデータ送信
                                Intent intentDone = new Intent(getApplication(), HomeActivity.class);
                                //intentDone.putExtra("UserID", userId);
                                intentDone.putExtra("UserName", userName);
                                //AlarmActivity alm = new AlarmActivity();
                                //alm.mDestroy();
                                startActivity(intentDone);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentBack = new Intent(getApplication(), PopupActivity.class);
                                intentBack.putExtra("BACK", true);
                                intentBack.putExtra("UserName", userName);
                                intentBack.putExtra("Comment", comment);
                                Log.d(TAG, userName+comment);
                                startActivity(intentBack);
                            }
                        })
                        .show();
            } else { //無効時
                Toast.makeText(EditGenreActivity.this, "ジャンルを3つ選んで下さい", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
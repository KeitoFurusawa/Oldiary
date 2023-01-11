package com.example.oldiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PutGenreActivity extends AppCompatActivity {

    private static ArrayList<Integer> selectedGenreList = new ArrayList<>();//
    public static boolean bStates = true;
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String userId;
    String userName;
    private final static String TAG = "genre";

    //new field
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_genre);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        selectedGenreList = new ArrayList<Integer>();

        //プリファレンス
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        getDiaryText();
        setOnClick(); //確定ボタン

        ListView listView = findViewById(R.id.listViewGenre);
        List<GenreData> list = new ArrayList<>();
        for (int i = 0; i < GenreData.genreList.length; i++) {
            GenreData genreData = new GenreData();
            genreData.setTextData(GenreData.genreList[i]);
            //genreData.setChecked(true);
            list.add(genreData);
        }
        PutGenreAdapter adapter = new PutGenreAdapter(PutGenreActivity.this,R.layout.genre_list,list);
        //listView.setOnClickListener();
        listView.setAdapter(adapter);
    }
    //インテントで編集途中のダイアリーの中身を読み取り
    private void getDiaryText() {
        Intent i = getIntent();
        text = i.getStringExtra("TEXT");
    }

    public void addSelectedGenreListList(int pos) {
        this.selectedGenreList.add(pos);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        //Log.d(TAG, sb.toString());
    }

    public void removeSelectedGenreListList(int pos) {
        this.selectedGenreList.remove(selectedGenreList.indexOf(pos));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        //Log.d(TAG, sb.toString());
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
            Intent intent = new Intent(getApplication(), WriteActivity.class);
            intent.putExtra("FromG", true);
            intent.putExtra("GenreList", selectedGenreList);
            intent.putExtra("TEXT", text);
            startActivity(intent);
        });
    }
}
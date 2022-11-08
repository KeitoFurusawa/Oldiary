package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelectGenreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_genre);

        ListView listView = findViewById(R.id.listViewGenre);
        List<GenreData> list = new ArrayList<>();
        String[] textData = {
                "サッカー", "野球", "テニス", "ガーデニング",
                "読書", "ピアノ", "ゴルフ", "映画鑑賞", "音楽鑑賞",
                "散歩", "ランニング", "料理", "ボランティア", "将棋"
        };
        for (int i = 0; i < textData.length; i++) {
            GenreData genreData = new GenreData();
            genreData.setTextData(textData[i]);
            //genreData.setChecked(true);
            list.add(genreData);
        }
        MyAdapter adapter = new MyAdapter(SelectGenreActivity.this,R.layout.genre_list,list);
        listView.setAdapter(adapter);
    }

    public void enableButton() { //チェック済みが3つになったら呼ぶ
        //ボタンの有効化
        //リスナの立ち上げ
    }
}
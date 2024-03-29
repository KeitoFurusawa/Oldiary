package com.example.oldiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class SelectGenreActivity extends AppCompatActivity {
    private static ArrayList<Integer> selectedGenreList = new ArrayList<>();//
    public static boolean bStates = false;//
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String userId;
    String userName;
    private final static String TAG = "checkbox";
    //private GenreData[] genreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_genre);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bStates = false;
        selectedGenreList = new ArrayList<Integer>();
        //idとnameを受け取る
        Intent intent1 = getIntent();
        userId = intent1.getStringExtra("UserID");
        userName = intent1.getStringExtra("UserName");
        //プリファレンス
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();

        setOnClick(); //確定ボタン

        ListView listView = findViewById(R.id.listViewGenre);
        List<GenreData> list = new ArrayList<>();
        for (int i = 0; i < GenreData.genreList.length; i++) {
            GenreData genreData = new GenreData();
            genreData.setTextData(GenreData.genreList[i]);
            //genreData.setChecked(true);
            list.add(genreData);
        }
        MyAdapter adapter = new MyAdapter(SelectGenreActivity.this,R.layout.genre_list,list);
        //listView.setOnClickListener();
        listView.setAdapter(adapter);
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
            //Log.d(TAG, String.valueOf("Button States: " + bStates)); //有効
            if (bStates) { //ボタン有効時
                mDatabase.child("users").child(userId).child("favoriteGenre").setValue(selectedGenreList); //firebaseにデータ送信
                Intent intentNext = new Intent(getApplication(), HomeActivity.class);
                intentNext.putExtra("UserID", userId);
                intentNext.putExtra("UserName", userName);
                editor.putString("UserID", userId);
                editor.commit();
                AlarmActivity alm = new AlarmActivity();
                alm.mDestroy();
                startActivity(intentNext);
            } else { //無効時
                Toast.makeText(SelectGenreActivity.this, "ジャンルを3つ選んで下さい", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
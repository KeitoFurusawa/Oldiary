package com.example.oldiary;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyAdapter extends ArrayAdapter<GenreData> {

    private final static String TAG = "checkbox";
    private LayoutInflater mLayoutInflater;

    public MyAdapter(Context context, int resourceId, List<GenreData> objects) {
        super(context, resourceId, objects);
        // getLayoutInflater()メソッドはActivityじゃないと使えない
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // getView()は各行を表示しようとした時に呼び出される
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position のデータを取る
        GenreData item = (GenreData)getItem(position);
        // convertViewは使いまわされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) convertView = mLayoutInflater.inflate(R.layout.genre_list, null);

        // GenreDataのデータをViewの各ウィジェットにセットする
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(item.getTextData());
        textView.setTextColor(Color.BLACK); //リストビューの色変更
        textView.setTextSize(20); //リストビューのテキストサイズ
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(item.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SelectGenreActivity sga = new SelectGenreActivity();
                if((sga.sizeOfSelectedGenreList() >= 3) && b) { //3つ以上のチェックはさせない
                    Log.e(TAG, "list is full");
                    GenreData genreData = getItem(position);
                    genreData.setChecked(false);
                    compoundButton.setChecked(false);
                    Toast.makeText(getContext(), "選択できるジャンルは最大3つです", Toast.LENGTH_SHORT).show(); //トーストを表示
                } else {
                    if (b) { //trueの時チェックがついた。
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        Log.d(TAG, "checked " + position); //debug
                        sga.addSelectedGenreListList(position);//selectedIdList.append(position);
                        Log.d(TAG, "Size of list: " + sga.sizeOfSelectedGenreList());
                        if (sga.sizeOfSelectedGenreList() == 3) {
                            sga.enableButton(); //もし３つチェックになったらボタンを有効化
                        }
                    } else { //falseの時チェックが外れた
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        Log.d(TAG, "unchecked " + position); //debug
                        sga.removeSelectedGenreListList(position);//remove position from list
                        if (sga.bStates) { //もしボタンが有効化されていたら無効化
                            sga.disableButton();
                        }
                    }
                }
            }
        });

        return convertView;
    }
}
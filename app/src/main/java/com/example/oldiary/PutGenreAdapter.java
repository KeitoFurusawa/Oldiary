package com.example.oldiary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class PutGenreAdapter extends ArrayAdapter<GenreData> {

    private final static String TAG = "checkbox";
    private LayoutInflater mLayoutInflater;
    public static final int MAX_LENGTH_P = 3;

    public PutGenreAdapter(Context context, int resourceId, List<GenreData> objects) {
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
        //GenreDataのデータをViewの各ウィジェットにセットする
        TextView textView = convertView.findViewById(R.id.collection_of_words);
        textView.setText(item.getTextData());
        textView.setTextColor(Color.BLACK); //リストビューの色変更
        textView.setTextSize(20); //リストビューのテキストサイズ

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        //Log.d(TAG, "setCheckBok"+position);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(item.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PutGenreActivity pga = new PutGenreActivity();
                if((pga.sizeOfSelectedGenreList() >= MAX_LENGTH_P) && b) { //3つ以上のチェックはさせない
                    //Log.e(TAG, "list is full");
                    GenreData genreData = getItem(position);
                    genreData.setChecked(false);
                    compoundButton.setChecked(false);
                    Toast.makeText(getContext(), "選択できるジャンルは最大3つです", Toast.LENGTH_SHORT).show(); //トーストを表示
                } else {
                    if (b) { //trueの時チェックがついた。
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        //Log.d(TAG, "checked " + position); //debug
                        pga.addSelectedGenreListList(position);//selectedIdList.append(position);
                        //Log.d(TAG, "Size of list: " + pga.sizeOfSelectedGenreList());
                        if (pga.sizeOfSelectedGenreList() == 3) {
                            pga.enableButton(); //もし３つチェックになったらボタンを有効化
                        }
                    } else { //falseの時チェックが外れた
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        //Log.d(TAG, "unchecked " + position); //debug
                        pga.removeSelectedGenreListList(position);//remove position from list
                        if (pga.bStates) { //もしボタンが有効化されていたら無効化
                            pga.disableButton();
                        }
                    }
                }
            }
        });

        return convertView;
    }
}

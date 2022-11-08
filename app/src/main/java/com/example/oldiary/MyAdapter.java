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

public class MyAdapter extends ArrayAdapter<GenreData> {

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
                if(/*&&selectedIdList.length > 2 */) {
                    //3つまで選択可能
                    //トーストを出す
                } else {
                    if (b) { //trueの時チェックがついた。
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        Log.d("button", "checked " + position); //debug
                        //selectedIdList.append(position);
                        //もし３つチェックになったらボタンを有効化
                    } else { //falseの時チェックが外れた
                        GenreData genreData = getItem(position);
                        genreData.setChecked(b);
                        Log.d("button", "unchecked " + position); //debug
                        //remove position from list
                        //もしボタンが有効化されていたら無効化
                    }
                }
            }
        });

        return convertView;
    }
}

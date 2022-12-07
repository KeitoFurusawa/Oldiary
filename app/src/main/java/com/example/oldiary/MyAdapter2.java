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

public class MyAdapter2 extends ArrayAdapter<GenreData> {

    private final static String TAG = "checkbox";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> selectedGenreList = new ArrayList<>();
    private static ArrayList<String> checkBoxTextList = new ArrayList<>(); ///
    private static ArrayList<CheckBox> checkBoxList = new ArrayList<>(); ///



    public MyAdapter2(Context context, int resourceId, List<GenreData> objects) {
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
        if (selectedGenreList.size() != 0) {
            int pos = selectedGenreList.get(0);
            if (position == pos) {
                //Log.d(TAG, textView.getText().toString() + " checked");
                checkBox.setChecked(true);
                item.setChecked(true);
                //Log.d(TAG, pos + "is" +  String.valueOf(checkBox.isChecked()));
                selectedGenreList.remove(0);
            }
        }
        if (!checkBoxTextList.contains(item.getTextData())) {
            //checkBox.setId(position);
            //Log.d(TAG, "setCheckBox"+position);
            //checkBoxTextList.add(item.getTextData());
            //checkBoxList.add(checkBox);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(item.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    EditGenreActivity ega = new EditGenreActivity();
                    if((ega.sizeOfSelectedGenreList() >= 3) && b) { //3つ以上のチェックはさせない
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
                            ega.addSelectedGenreListList(position);//selectedIdList.append(position);
                            Log.d(TAG, "Size of list: " + ega.sizeOfSelectedGenreList());
                            if (ega.sizeOfSelectedGenreList() == 3) {
                                ega.enableButton(); //もし３つチェックになったらボタンを有効化
                            }
                        } else { //falseの時チェックが外れた
                            GenreData genreData = getItem(position);
                            genreData.setChecked(b);
                            Log.d(TAG, "unchecked " + position); //debug
                            ega.removeSelectedGenreListList(position);//remove position from list
                            if (ega.bStates) { //もしボタンが有効化されていたら無効化
                                ega.disableButton();
                            }
                        }
                    }
                }
            });
        } else {
            checkBoxList.get(position).setOnCheckedChangeListener(null);
            checkBoxList.get(position).setChecked(item.isChecked());
            Log.d(TAG, "else" + position);
            checkBoxList.get(position).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    EditGenreActivity ega = new EditGenreActivity();
                    if((ega.sizeOfSelectedGenreList() >= 3) && b) { //3つ以上のチェックはさせない
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
                            ega.addSelectedGenreListList(position);//selectedIdList.append(position);
                            Log.d(TAG, "Size of list: " + ega.sizeOfSelectedGenreList());
                            if (ega.sizeOfSelectedGenreList() == 3) {
                                ega.enableButton(); //もし３つチェックになったらボタンを有効化
                            }
                        } else { //falseの時チェックが外れた
                            GenreData genreData = getItem(position);
                            genreData.setChecked(b);
                            Log.d(TAG, "unchecked " + position); //debug
                            ega.removeSelectedGenreListList(position);//remove position from list
                            if (ega.bStates) { //もしボタンが有効化されていたら無効化
                                ega.disableButton();
                            }
                        }
                    }
                }
            });

        }

        return convertView;
    }

    public void alreadyChecked(ArrayList<Integer> selectedGenreList) {
        Log.d(TAG, "ALC");
        for (int pos : selectedGenreList) {
            this.selectedGenreList.add(pos);
        }
    }

    public void onClickElm(int pos) {
        for (String s: checkBoxTextList) {
            //Log.d(TAG, s);
        }
        checkBoxList.get(pos).performClick();
        //Log.d(TAG, "sizeOfCheckbox: " + checkBoxTextList.size());
    }
}

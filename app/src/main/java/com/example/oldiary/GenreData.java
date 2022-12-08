package com.example.oldiary;

public class GenreData {
    private String textData = "";
    private Boolean checked = false;

    public void setTextData(String str){
        textData = str;
    }
    public String getTextData(){
        return textData;
    }
    public void setChecked(boolean bool){
        checked = bool;
    }
    public boolean isChecked(){
        return checked;
    }

    public static final String[] genreList = {
            "サッカー", "野球", "テニス", "ガーデニング", "読書",
            "ピアノ", "ゴルフ", "映画鑑賞", "音楽鑑賞", "散歩",
            "ランニング", "料理", "ボランティア", "将棋", "囲碁",
            "カラオケ", "旅行", "ワープロ", "手芸", "ギター",
    };
}

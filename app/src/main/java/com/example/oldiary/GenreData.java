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
}

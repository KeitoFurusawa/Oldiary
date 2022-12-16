package com.example.oldiary;

public class ReplyData {
    private String text; //リプライ本文
    private String repliedBy; //名前
    private String repliedAt; //リプライ日時
    private int iconId; //アイコン

    //コンストラクタ
    public ReplyData(String text, String repliedBy, String repliedAt, int iconId) {
        this.text = text;
        this.repliedAt = repliedAt;
        this.repliedBy = repliedBy;
        this.iconId = iconId;
    }


}

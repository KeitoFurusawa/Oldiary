package com.example.oldiary;

public class ReplyData {
    private String text; //リプライ本文
    private String repliedBy; //ID
    private String userName; //ユーザネーム
    private String repliedAt; //リプライ日時
    private int iconId; //アイコン

    //空コンストラクタ
    public ReplyData() {};

    //コンストラクタ
    public ReplyData(String text, String repliedBy,String userName, String repliedAt, int iconId) {
        String noLineBreakText = text.replace("\n", "");
        this.text = noLineBreakText;
        this.repliedAt = repliedAt;
        this.repliedBy = repliedBy;
        this.userName = userName;
        this.iconId = iconId;
    }

    //本文を設定
    public void setText(String text) {
        this.text = text;
    }

    //IDを設定
    public void setRepliedBy(String repliedBy) {
        this.repliedBy = repliedBy;
    }

    //リプライ日時を設定
    public void setRepliedAt(String repliedAt) {
        this.repliedAt = repliedAt;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

}

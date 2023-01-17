package com.example.oldiary;

import java.util.HashMap;

public class ExpandablelistDataPump {

    public static HashMap<String, String> getData() {
        HashMap<String, String> expandableListDetail = new HashMap<String, String>();

        expandableListDetail.put("ID", "サービスやシステムの登録者や利用者を識別するための\n利用者それぞれについている名前。");
        expandableListDetail.put("アカウント", "個人を識別するために事前登録するIDとパスワードの\nセットのこと。会員登録のような意味。");
        expandableListDetail.put("アバター", "自分の分身となるキャラクターのこと。");
        expandableListDetail.put("コメント", "ページの閲覧者が入力欄に文字列を記入して投稿し、\nページ上に設けられた枠内に表示することができる機能。");
        expandableListDetail.put("パスワード", "本人確認をするための暗証番号。");
        expandableListDetail.put("ホーム", "起点となる場所。\nこのアプリを立ち上げたときに最初に表示される画面のこと。");
        expandableListDetail.put("ログイン", "インターネット上のサービスを利用する際に、\nIDとパスワードを使って本人を確認する仕組みのこと。");
        expandableListDetail.put("ログアウト", "ログイン状態を解除すること。");
        expandableListDetail.put("ユーザー", "サービスを利用する人を指す。");

        return expandableListDetail;
    }
}

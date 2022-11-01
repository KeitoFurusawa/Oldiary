package com.example.oldiary;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadAndWrite {
    private static final String TAG = "CreateActivity";
    private DatabaseReference mDatabase;

    public ReadAndWrite() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public static void writeMessage(String path, String message) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(message);
    }
    public void addNewUser(String phoneNumber, String password) {
        StringBuffer sb = new StringBuffer();
        sb.append("id_");
        User user = new User(phoneNumber, password);
        sb.append(phoneNumber);
        String ID = sb.toString();
        Log.d(TAG, ID);
        mDatabase.child("users").child(ID).setValue(user);

        addEventListener(mDatabase);
        // login画面でチェックする
    }

    // パスにあるデータを読み取って、変更をリッスンするには、addValueEventListener() メソッドを使用して
    // ValueEventListener を DatabaseReference に追加します。
    public void addEventListener(DatabaseReference mPostReference) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override   // 特定のパスにあるコンテンツの静的スナップショットを、イベントの発生時に存在していたとおりに読み取ることができる
            // Listenerがアタッチされたときに1回、また、データ(子も含む)が変更されるたびにコールされる
            /**
             * @param dataSnapshot:その場所にあるすべてのデータ（子のデータも含む）を含んでいるスナップショット
             */
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * Snapshot.getValue():そのデータの Java オブジェクト表現が返され、その場所にデータが存在しない場合、null が返される。
                 */
                //Post post = dataSnapshot.getValue(Post.class);

                // realtime databaseから情報を読み取る
                String email = (String)dataSnapshot.child("users").child("1").child("email").getValue();
                String username = (String)dataSnapshot.child("users").child("1").child("username").getValue();
                Log.i(TAG, "email:" + email);
                Log.i(TAG, "username:" + username);
            }

            @Override   // 読み取りがキャンセルされた場合に呼び出される
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

}

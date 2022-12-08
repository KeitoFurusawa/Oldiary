package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "edit";
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String userId;
    private String userName;
    private String comment;
    private List<Integer> selectedGenreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getUserId();
        setUserName();
        setGenre();
        setComment();
        setOnClick();
    }

    private void getUserId() {
        userId = preference.getString("UserID", "");
    }

    private void setUserName() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("BACK", false)) {
            userName = intent.getStringExtra("UserName");
            TextView textUserName = findViewById(R.id.textViewUserName);
            textUserName.setText(userName);
        } else {
            mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error getting data", task.getException());
                    }
                    else {
                        userName = String.valueOf(task.getResult().getValue());
                        TextView textUserName = findViewById(R.id.textViewUserName);
                        textUserName.setText(userName);
                    }
                }
            });
        }
    }

    private void setComment() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("BACK", false)) {
            comment = intent.getStringExtra("Comment");
            TextView textComment = findViewById(R.id.comment);
            textComment.setMovementMethod(new ScrollingMovementMethod());
            textComment.setText(comment);
        } else {
            mDatabase.child("users").child(userId).child("comment").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error getting data", task.getException());
                    }
                    else {
                        comment = String.valueOf(task.getResult().getValue());
                        if (comment.equals("null")) {
                            Log.d(TAG, "the result of getting comment data is null");
                        } else {
                            TextView textComment = findViewById(R.id.comment);
                            textComment.setText(comment);
                        }
                    }
                }
            });
        }
    }

    private void setGenre() {
        Intent intent = getIntent();
        selectedGenreList = new ArrayList<>();
        selectedGenreList.add(intent.getIntExtra("GenreCode1", -1));
        selectedGenreList.add(intent.getIntExtra("GenreCode2", -1));
        selectedGenreList.add(intent.getIntExtra("GenreCode3", -1));
        int i = 0;
        for (Integer n : selectedGenreList) {
            int viewId = getResources().getIdentifier("content" + (i+1), "id", getPackageName());
            TextView genre = findViewById(viewId);
            if (n < 0) { //インテントからジャンルデータの読み込み失敗
                genre.setText("ERROR");
            } else {
                new GenreData();
                genre.setText((GenreData.genreList[n]));
            }
            i++;
        }
    }

    private void confirm() {
        TextView textUserName = findViewById(R.id.textViewUserName);
        TextView textComment = findViewById(R.id.comment);
        userName = textUserName.getText().toString();
        comment = textComment.getText().toString();
    }

    private void setOnClick() {
        //Button btnCancel = findViewById(R.id.cancel);
        Button btnChose = findViewById(R.id.buttonChoseContent);
        Button btnConfirm = findViewById(R.id.confirm);
        /** 戻るボタン
        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("注意")
                    .setMessage("プロフィールの編集を中断しますか?\n(変更内容は破棄されます)")
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent(getApplication(), ProfileActivity.class);
                            startActivity(intent2);
                        }
                    })
                    .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ;
                        }
                    })
                    .show();
        });
        */

        btnChose.setOnClickListener(v -> {
            confirm(); //ここで変更内容を格納
            Intent intent = new Intent(getApplication(), EditGenreActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("Comment", comment);
            intent.putExtra("GenreCode1", selectedGenreList.get(0));
            intent.putExtra("GenreCode2", selectedGenreList.get(1));
            intent.putExtra("GenreCode3", selectedGenreList.get(2));
            startActivity(intent);
        });
        btnConfirm.setOnClickListener(v -> {
            confirm(); //ここで変更内容を格納
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("確認")
                    .setMessage("変更内容を保存しますか？")
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabase.child("users").child(userId).child("userName").setValue(userName);
                            mDatabase.child("users").child(userId).child("comment").setValue(comment);
                            mDatabase.child("users").child(userId).child("favoriteGenre").setValue(selectedGenreList); //firebaseにデータ送信
                            Intent intentDone = new Intent(getApplication(), HomeActivity.class);
                            startActivity(intentDone);
                        }
                    })
                    .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ;
                        }
                    })
                    .show();
        });
    }
}
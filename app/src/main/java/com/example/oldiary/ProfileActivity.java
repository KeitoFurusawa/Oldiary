package com.example.oldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "prof";
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private List<Integer> gCodeList = new ArrayList<>();
    String userId;
    AlertDialog.Builder dialogBuilder;
    Dialog dialog;
    EditText user_name, content1, content2, content3, comment;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("プロフィール");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        getUserId();
        setUserName();
        setComment();
        setGenre();
        setOnClickEdit();
        setOnClickLogout();
        setOnclickAvatar();
        // setOnClickBack();
    }

    protected void getUserId() {
        userId = preference.getString("UserID", "");
    }

    private void setUserName() {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String resultUserName = String.valueOf(task.getResult().getValue());
                    TextView textUserName = findViewById(R.id.textViewUserName);
                    textUserName.setText(resultUserName);
                }
            }
        });
    }

    private void setComment() {
        mDatabase.child("users").child(userId).child("comment").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String resultComment = String.valueOf(task.getResult().getValue());
                    if (!resultComment.equals("null")) {
                        TextView textViewComment = findViewById(R.id.comment);
                        textViewComment.setText(resultComment);
                    }
                }
            }
        });
    }

    private void setGenre() {
        mDatabase.child("users").child(userId).child("favoriteGenre").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String Result = String.valueOf(task.getResult().getValue());
                    String reResult = Result.replace("[", "").replace("]", "").replace(" ", "");
                    String[] split = reResult.split(",");
                    int i = 0;
                    if (!userId.equals("id_0")) {
                        for (String xs : split) {
                            i++;
                            gCodeList.add(Integer.parseInt(xs));
                            int viewId = getResources().getIdentifier("textViewGenre" + i, "id", getPackageName());
                            TextView genre = findViewById(viewId);
                            genre.setText(GenreData.genreList[Integer.parseInt(xs)]);
                        }
                    }
                }
            }
        });
    }
    private void setOnClickEdit() {
        LinearLayout lnEdit = findViewById(R.id.linearlayout_editProf);
        lnEdit.setOnClickListener(v -> {
            if (userId.equals("id_0")) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("注意")
                        .setMessage("debugユーザ0はその機能を使えません。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(getApplication(), HomeActivity.class);
                                startActivity(intent2);
                            }
                        })
                        .show();
            } else {
                Intent intent = new Intent(getApplication(), EditProfileActivity.class);
                intent.putExtra("GenreCode1", gCodeList.get(0));
                intent.putExtra("GenreCode2", gCodeList.get(1));
                intent.putExtra("GenreCode3", gCodeList.get(2));
                startActivity(intent);
            }
        });
    }

    private void setOnClickLogout() {
        LinearLayout lnLogout = findViewById(R.id.linearlayout_logout);
        lnLogout.setOnClickListener(v -> {
            editor.putString("UserID", "");
            editor.commit();
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnclickAvatar() {
        LinearLayout linearLayout = findViewById(R.id.linearlayout_EditAvatar);
        linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), AvatarMakeActivity.class);
            startActivity(intent);
        });

    }
}
package com.example.oldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
    private String userId;
    AlertDialog.Builder dialogBuilder;
    Dialog dialog;
    EditText user_name, content1, content2, content3, comment;
    Button save, cancel;
    TextView textView1, textView2;
    int count = 0;
    private String gender = "null";
    private String color = "null";
    private ProgressDialog progressDialog;
    private boolean checkID, checkName, checkComment, checkGenre, checkIcon;
    private ImageView imgProf;
    private TextView textUserName;
    private CardView cvInfo;


    private static final String[] genreList = {
            "サッカー", "野球", "テニス", "ガーデニング", "読書",
            "ピアノ", "ゴルフ", "映画鑑賞", "音楽鑑賞", "散歩",
            "ランニング", "料理", "ボランティア", "将棋", "囲碁",
            "カラオケ", "旅行", "ワープロ", "手芸", "ギター",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("プロフィール");
        }
        startLoading();

        textView1 = findViewById(R.id.titleComment);
        textView2 = findViewById(R.id.comment);
        textView2.setMovementMethod(new ScrollingMovementMethod());

        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        getUserId();
        setUserName();
        setComment();
        loadPrevAvatar();
        setGenre();
        setOnClickEdit();
        setOnClickLogout();
        setOnclickAvatar();

    }

    protected void getUserId() {
        userId = preference.getString("UserID", "");
        checkID = true;
        checkLoading();
    }

    public void onClick(View view) {
        ImageButton imageButton = findViewById(R.id.allow);
        textView1 = findViewById(R.id.titleComment);
        textView2 = findViewById(R.id.comment);

        if (count % 2 == 0) {
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            imageButton.setImageResource(R.drawable.ic_keyboard_double_arrow_up);
            count++;
        }

        else {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            imageButton.setImageResource(R.drawable.ic_keyboard_double_arrow_down);
            count++;
        }

    }

    public void onClick2(View view) {
        Button button = findViewById(R.id.backbtn);
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
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
                    textUserName = findViewById(R.id.textViewUserName);
                    textUserName.setText(resultUserName);
                    checkName = true;
                    checkLoading();
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
                    checkComment = true;
                    checkLoading();
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
                            genre.setText(genreList[Integer.parseInt(xs)]);
                        }
                    }
                    checkGenre = true;
                    checkLoading();
                }
            }
        });
    }
    private void setOnClickEdit() {
        Button profBtn = findViewById(R.id.linearlayout_editProf);
        profBtn.setOnClickListener(v -> {
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
        Button avatarBtn = findViewById(R.id.linearlayout_EditAvatar);
        avatarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), AvatarMakeActivity.class);
            startActivity(intent);
        });

    }

    private void loadPrevAvatar() {
        mDatabase.child("users").child(userId).child("avatar").child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    gender = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + gender);
                    if (gender.equals("null")) {
                        gender = "man";
                    }
                    Log.d(TAG, gender);
                }
            }
        });
        mDatabase.child("users").child(userId).child("avatar").child("color").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    color = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, "result: " + color);
                    if (color.equals("null")) {
                        color = "blue";
                    }
                    Log.d(TAG, color);
                    setPrevAvatar();
                }
            }
        });
    }

    private void setPrevAvatar() {
        while(gender.equals("null") || color.equals("null")) {
            Log.d(TAG, gender+color);
        }
        int drawableId = getResources().getIdentifier("icon_"+color+"_"+gender, "drawable", getPackageName());
        imgProf = findViewById(R.id.profile_image);
        imgProf.setImageResource(drawableId);
        checkIcon = true;
        checkLoading();
    }

    private void skipCheck() {
        //checkID = true;
        //check = true;
        //checkComment = true;
        //checkAvatar = true;
        checkLoading();
    }

    private void startLoading() {
        checkID = false;
        checkName = false;
        checkComment = false;
        checkGenre = false;
        checkIcon = false;

        cvInfo = findViewById(R.id.myInfo);
        textUserName = findViewById(R.id.textViewUserName);
        if (!checkComment || !checkGenre || !checkIcon || checkName) {
            cvInfo.setVisibility(View.GONE);
            textUserName.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setNavigationBarColor(0);
        progressDialog.setMessage("ロード中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void checkLoading() {
        /*Log.d(TAG, "C-ID: " + checkID);
        Log.d(TAG, "C-AV: " + checkAvatar);
        Log.d(TAG, "C-Post:" + checkPostText);
        Log.d(TAG, "C-Time:" + checkPostTime);
        */
        if (checkID && checkIcon && checkGenre && checkName && checkComment) {
            progressDialog.dismiss();
            cvInfo.setVisibility(View.VISIBLE);
            textUserName.setVisibility(View.VISIBLE);
        }
    }
}
package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AvatarMakeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String userId;
    private String gender = "null";
    private String color = "null";
    int image;
    private static final String TAG = "avatar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_make);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getUserId();
        loadPrevAvatar();

    }

    private void getUserId() {
        userId = preference.getString("UserID", "");
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
        };
        RadioGroup rg1 = findViewById(R.id.choice1);
        RadioGroup rg2 = findViewById(R.id.choice2);
        int viewIdGender = getResources().getIdentifier(gender, "id", getPackageName());
        int viewIdColor = getResources().getIdentifier(color, "id", getPackageName());
        rg1.check(viewIdGender);
        rg2.check(viewIdColor);
        changeImg();
        setRadioListener();
    }

    private void setRadioListener() {
        RadioGroup rg1 = findViewById(R.id.choice1);
        RadioGroup rg2 = findViewById(R.id.choice2);
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                changeImg();
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                changeImg();
            }
        });

    }

    private void changeImg() {
        RadioGroup q1 = (RadioGroup) findViewById(R.id.choice1);
        RadioGroup q2 = (RadioGroup) findViewById(R.id.choice2);
        int id = q1.getCheckedRadioButtonId();
        int id2 = q2.getCheckedRadioButtonId();
        ImageView imageView = findViewById(R.id.avatar);

        if (id == R.id.man) {
            gender = "man";
            if (id2 == R.id.blue) {
                color = "blue";
                image = R.drawable.blue_man;
            }
            else if (id2 == R.id.green) {
                color = "green";
                image = R.drawable.green_man;
            }
            else if (id2 == R.id.purple) {
                color = "purple";
                image = R.drawable.purple_man;
            }
            else if (id2 == R.id.red) {
                color = "red";
                image = R.drawable.red_man;
            }
            else if (id2 == R.id.pink) {
                color = "pink";
                image = R.drawable.pink_man;
            }
            else if (id2 == R.id.light_blue) {
                color = "light_blue";
                image = R.drawable.lightblue_man;
            }
            else if (id2 == R.id.yellow) {
                color = "yellow";
                image = R.drawable.yellow_man;
            }
            else {
                Toast.makeText(AvatarMakeActivity.this, "error1", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.woman) {
            gender = "woman";
            if (id2 == R.id.blue) {
                color = "blue";
                image = R.drawable.blue_woman;
            }
            else if (id2 == R.id.green) {
                color = "green";
                image = R.drawable.green_woman;
            }
            else if (id2 == R.id.purple) {
                color = "purple";
                image = R.drawable.purple_woman;
            }
            else if (id2 == R.id.red) {
                color = "red";
                image = R.drawable.red_woman;
            }
            else if (id2 == R.id.pink) {
                color = "pink";
                image = R.drawable.pink_woman;
            }
            else if (id2 == R.id.light_blue) {
                color = "light_blue";
                image = R.drawable.lightblue_woman;
            }
            else if (id2 == R.id.yellow) {
                color = "yellow";
                image = R.drawable.yellow_woman;
            }
            else {
                Toast.makeText(AvatarMakeActivity.this, "error2", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(AvatarMakeActivity.this, "error3", Toast.LENGTH_SHORT).show();
        }

        imageView.setImageResource(image);
    }

    public void onClick2(View view) {
        ImageView imageView = new ImageView(getBaseContext());
        imageView.setImageResource(image);

        new AlertDialog.Builder(this)
                .setTitle("これでよろしいですか？")
                .setView(imageView)
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child("users").child(userId).child("avatar").child("gender").setValue(gender);
                        mDatabase.child("users").child(userId).child("avatar").child("color").setValue(color);
                        Intent intent = new Intent(getApplication(), HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("いいえ", null)
                .show();
    }

    public void onClick3(View view) {
        Intent intent = new Intent(getApplication(), ProfileActivity.class);
        startActivity(intent);
    }
}
package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeProfile extends AppCompatActivity {
    private static final String TAG = "profile";
    String userId;
    private String userName;
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        getUserId();
        setOnClick();
        setOnClick2();
    }

    private void getUserId() {
        Intent intentPrev = getIntent();
        userId = intentPrev.getStringExtra("UserID");
        Log.d(TAG, userId);
    }

    //Back
    protected void setOnClick() {
        ImageButton imgButton = findViewById(R.id.imageButtonBack);
        imgButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            //mDestroy();
            startActivity(intent);
        });
    }

    //Confirm
    protected void setOnClick2() {
        Button ButtonNext = findViewById(R.id.buttonConfirm);
        TextView textViewUserName = findViewById(R.id.editTextUserName);
        TextView textViewYear = findViewById(R.id.editTextYear);
        TextView textViewMonth = findViewById(R.id.editTextMonth);
        TextView textViewDate = findViewById(R.id.editTextDate);
        ButtonNext.setOnClickListener(v -> {
            if (userId.equals("id_0") && textViewUserName.getText().toString().length() == 0) {
                Intent intentNext = new Intent(getApplication(), HomeActivity.class);
                intentNext.putExtra("UserID", userId);
                intentNext.putExtra("UserName", userName);
                editor.putString("UserID", userId);
                editor.commit();
                startActivity(intentNext);
                //
                AlarmActivity alm = new AlarmActivity();
                alm.mDestroy();
                //
            }
            else if (textViewUserName.getText().toString().length() == 0 ||
                textViewYear.getText().toString().length() == 0 ||
                textViewMonth.getText().toString().length() == 0 ||
                textViewDate.getText().toString().length() == 0) {
                Log.e(TAG, "fill in the blank correctly");
                Toast.makeText(
                        MakeProfile.this, "入力が不足しています", Toast.LENGTH_SHORT).show();
            } else {
                userName = textViewUserName.getText().toString();
                int year = Integer.parseInt(textViewYear.getText().toString());
                int month = Integer.parseInt(textViewMonth.getText().toString());
                int date = Integer.parseInt(textViewDate.getText().toString());
                AgeCalculator ac = new AgeCalculator();
                int age = ac.getAge(year, month, date);
                Log.d(TAG, String.format("name: %s, %d/%d/%d, age: %d", userName, year, month, date, age));
                if (userName.equals("null")) {
                    Toast.makeText(
                            MakeProfile.this, "その名前はご利用いただけません。", Toast.LENGTH_SHORT).show();
                }
                else if(age == -1) {
                    Log.e(TAG, "input values of birthday are wrong");
                    Toast.makeText(
                            MakeProfile.this, "正しい生年月日を入力してください", Toast.LENGTH_SHORT).show();
                }
                else if (age < 60) {
                    Log.e(TAG, "Not available for people under 60");
                    Toast.makeText(
                            MakeProfile.this, "60歳未満の方はご利用いただけません", Toast.LENGTH_SHORT).show();
                } else { //正しく動く時
                    mDatabase.child("users").child(userId).child("userName").setValue(userName);
                    AgeCalculator setData = new AgeCalculator(year, month, date, age);
                    mDatabase.child("users").child(userId).child("DateOfBirth").setValue(setData);
                    Intent intentNext = new Intent(getApplication(), SelectGenreActivity.class);
                    intentNext.putExtra("UserID", userId);
                    intentNext.putExtra("UserName", userName);
                    startActivity(intentNext);
                }
            }
        });
    }
}


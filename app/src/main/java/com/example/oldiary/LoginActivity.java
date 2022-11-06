package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    private String phoneNumber;
    private String password;
    private DatabaseReference mDatabase;
    private static String getResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setOnClickBack();
        setOnClickCreateNew();
        setOnClickLogin();
    }

    //戻る
    protected void setOnClickBack() {
        ImageButton imageButton = findViewById(R.id.imageButtonBack);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }
    //新規アカウント作成
    protected void setOnClickCreateNew() {
        TextView textView = findViewById(R.id.textViewCreateNewButton);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), CreateActivity.class);
            startActivity(intent);
        });
    }
    //ログイン
    protected void setOnClickLogin() {
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        TextView textViewPassword = findViewById(R.id.editTextPassword);
        buttonLogin.setOnClickListener(v -> {
            phoneNumber = textViewPhoneNumber.getText().toString();
            password = textViewPassword.getText().toString();
            String userId = "id_" + phoneNumber;
            Log.d(TAG, phoneNumber);//debug 確認用
            Log.d(TAG, password);   //debug 確認用

            if ((phoneNumber.length() > 8) && (password.length() == 4)) {
                getData(userId);

            } else {
                if (phoneNumber.equals("0")) { //0番は通過
                    Intent intent = new Intent(getApplication(), MakeProfile.class);
                    intent.putExtra("UserID", userId); //インテントにユーザIDを渡す
                    startActivity(intent);
                } else {
                    Log.d(TAG, "incorrect input value");//入力が正しくない
                    Toast.makeText(LoginActivity.this, "電話番号・暗証番号を正しく入力してください", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void getData(String userId) {
        Log.d("debug", "this is getData");
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    setResult(value);
                    checkResult(userId);
                }
            }
        });
    }

    protected void setResult(String result) {
        Log.d("debug", "setResult");
        this.getResult = result;
    }

    protected void checkResult(String userId) {
        Log.d("debug", "this is checkPhoneNum");
        while (this.getResult == null) {
            Log.d(TAG, "loading info");
        }
        Log.d(TAG, userId + ": " + getResult); //debug
        if (getResult.equals("null")) { //電話番号未登録
            Log.e(TAG, "ERROR: that PhoneNumber wasn't registered");
            Toast.makeText(LoginActivity.this, "その電話番号は登録されていません", Toast.LENGTH_SHORT).show();
        } else {
            //int idxAddPhoneNum = "phoneNumber=".length(); // = 12
            int idxAddPass = "password=".length(); // = 9
            //int idxOfRePhoneNum = getResult.indexOf("phoneNumber=") + idxAddPhoneNum;
            int idxOfRePass = getResult.indexOf("password=") + idxAddPass;
            String resultPhoneNumber = getResult.substring(idxOfRePass, idxOfRePass+4);
            if (password.equals(resultPhoneNumber)) { //パスワードが正しい
                Log.d(TAG, "login success");
                checkUserName(userId);
            } else { //パスワードが誤り
                Log.e(TAG, "ERROR: incorrect password");
                Toast.makeText(LoginActivity.this, "パスワードが間違っています", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void checkUserName(String userId) {
        mDatabase.child("users").child(userId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String userName = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, userName); //debug
                    if (userName.equals("null")) { //初回ログイン
                        Intent intentNext = new Intent(getApplication(), MakeProfile.class);
                        intentNext.putExtra("UserID", userId); //インテントにユーザIDを渡す
                        startActivity(intentNext);
                    } else { //2回目以降
                        Intent intentNext = new Intent(getApplication(), LoginActivity.class);
                        intentNext.putExtra("UserName", userName);
                        startActivity(intentNext);
                    }
                }
            }
        });
    }
}
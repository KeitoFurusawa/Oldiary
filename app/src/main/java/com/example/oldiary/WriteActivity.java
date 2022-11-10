package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    private final static String TAG = "home";
    private String userId;
    private SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        userId =  preference.getString("UserID", "");
        checkUserId();
        setOnClick();
        setOnClickPost();
    }

    protected void checkUserId() {
        if (userId.equals("")) {
            Log.e(TAG, "failed to get User ID");
        } else {
            Log.d(TAG, userId);
        }
    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.cancel);

        // lambda式
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

    protected void setOnClickPost() {
        Button btnPost = findViewById(R.id.post);
        btnPost.setOnClickListener(v -> {
            TextView textView = findViewById(R.id.editTextTextMultiLine2);
            String txt = textView.getText().toString();
            ReadAndWrite rad = new ReadAndWrite();
            rad.writeDiary(userId, txt);
            new AlertDialog.Builder(WriteActivity.this)
                    .setTitle("")
                    .setMessage("投稿されました！")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // OKボタン押下時の処理
                            Intent intent = new Intent(getApplication(), HomeActivity.class);
                            startActivity(intent);
                            Log.d("AlertDialog", "Positive which :" + which);
                        }
                    })
                    .show();

        });
    }
}
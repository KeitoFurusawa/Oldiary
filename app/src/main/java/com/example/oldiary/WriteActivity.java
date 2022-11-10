package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    private final static String TAG = "write";
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
    }

    protected void checkUserId() {
        if (userId.equals("")) {
            Log.e(TAG, "failed to get User ID");
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

}
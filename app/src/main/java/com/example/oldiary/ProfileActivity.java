package com.example.oldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    AlertDialog.Builder dialogBuilder;
    Dialog dialog;
    EditText user_name, content1, content2, content3, comment;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editProfile();
        editAvatar();

    }

    protected void editProfile() {
        LinearLayout linearLayout = findViewById(R.id.linearlayout_4);
        linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), EditprofileActivity.class);
            startActivity(intent);
        });

    }



    protected void editAvatar() {
        LinearLayout linearLayout = findViewById(R.id.linearlayout_5);
        linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), AvatarMakeActivity.class);
            startActivity(intent);
        });

    }


}
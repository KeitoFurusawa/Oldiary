package com.example.oldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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

        setOnClickBack();
        editProfile();

    }

    protected void setOnClickBack() {
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }

    protected void editProfile() {
        Button button = findViewById(R.id.button2);
        dialog = new Dialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.popup_prof);
                final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_prof, null);
            }
        });

    }
}
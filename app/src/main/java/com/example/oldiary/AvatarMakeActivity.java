package com.example.oldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvatarMakeActivity extends AppCompatActivity {

    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_make);
    }

    public void onClick(View view) {
        RadioGroup q1 = (RadioGroup) findViewById(R.id.choice1);
        RadioGroup q2 = (RadioGroup) findViewById(R.id.choice2);
        int id = q1.getCheckedRadioButtonId();
        int id2 = q2.getCheckedRadioButtonId();
        ImageView imageView = findViewById(R.id.avatar);

        if (id == R.id.man) {
            if (id2 == R.id.blue) {
                image = R.drawable.blue_man;
            }
            else if (id2 == R.id.green) {
                image = R.drawable.green_man;
            }
            else if (id2 == R.id.purple) {
                image = R.drawable.purple_man;
            }
            else if (id2 == R.id.red) {
                image = R.drawable.red_man;
            }
            else if (id2 == R.id.pink) {
                image = R.drawable.pink_man;
            }
            else if (id2 == R.id.light_blue) {
                image = R.drawable.lightblue_man;
            }
            else if (id2 == R.id.yellow) {
                image = R.drawable.yellow_man;
            }
            else {
                Toast.makeText(AvatarMakeActivity.this, "error1", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.woman) {
            if (id2 == R.id.blue) {
                image = R.drawable.blue_woman;
            }
            else if (id2 == R.id.green) {
                image = R.drawable.green_woman;
            }
            else if (id2 == R.id.purple) {
                image = R.drawable.purple_woman;
            }
            else if (id2 == R.id.red) {
                image = R.drawable.red_woman;
            }
            else if (id2 == R.id.pink) {
                image = R.drawable.pink_woman;
            }
            else if (id2 == R.id.light_blue) {
                image = R.drawable.lightblue_woman;
            }
            else if (id2 == R.id.yellow) {
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
                        // OK button pressed
                    }
                })
                .setNegativeButton("いいえ", null)
                .show();
    }
}
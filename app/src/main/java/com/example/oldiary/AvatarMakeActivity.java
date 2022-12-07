package com.example.oldiary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvatarMakeActivity extends AppCompatActivity {

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
                imageView.setImageResource(R.drawable.blue_man);
            }
            else if (id2 == R.id.green) {
                imageView.setImageResource(R.drawable.green_man);
            }
            else if (id2 == R.id.green) {
                imageView.setImageResource(R.drawable.green_man);
            }
            else if (id2 == R.id.purple) {
                imageView.setImageResource(R.drawable.purple_man);
            }
            else if (id2 == R.id.red) {
                imageView.setImageResource(R.drawable.red_man);
            }
            else if (id2 == R.id.pink) {
                imageView.setImageResource(R.drawable.pink_man);
            }
            else if (id2 == R.id.light_blue) {
                imageView.setImageResource(R.drawable.lightblue_man);
            }
            else if (id2 == R.id.yellow) {
                imageView.setImageResource(R.drawable.yellow_man);
            }
            else {
                Toast.makeText(AvatarMakeActivity.this, "error1", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.woman) {
            if (id2 == R.id.blue) {
                imageView.setImageResource(R.drawable.blue_woman);
            }
            else if (id2 == R.id.green) {
                imageView.setImageResource(R.drawable.green_woman);
            }
            else if (id2 == R.id.green) {
                imageView.setImageResource(R.drawable.green_woman);
            }
            else if (id2 == R.id.purple) {
                imageView.setImageResource(R.drawable.purple_woman);
            }
            else if (id2 == R.id.red) {
                imageView.setImageResource(R.drawable.red_woman);
            }
            else if (id2 == R.id.pink) {
                imageView.setImageResource(R.drawable.pink_woman);
            }
            else if (id2 == R.id.light_blue) {
                imageView.setImageResource(R.drawable.lightblue_woman);
            }
            else if (id2 == R.id.yellow) {
                imageView.setImageResource(R.drawable.yellow_woman);
            }
            else {
                Toast.makeText(AvatarMakeActivity.this, "error2", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(AvatarMakeActivity.this, "error3", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick2(View view) {

    }
}
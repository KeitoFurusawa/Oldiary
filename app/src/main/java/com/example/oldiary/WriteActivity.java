package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

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
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    protected void findViews(){
        button1 = findViewById(R.id.button3);
    }
    protected void setListeners(){
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(
                        intent,
                        REQUEST_CAPTURE_IMAGE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAPTURE_IMAGE
                && resultCode == Activity.RESULT_OK) {
            Bitmap capturedImage =
                    (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(capturedImage);
        }
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();

                try {
                    Bitmap bmp = getBitmapFromUri(uri);
                    imageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
package com.example.oldiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileDescriptor;
import java.io.IOException;

public class WriteActivity extends AppCompatActivity {

    static final int REQUEST_CAPTURE_IMAGE = 100;

    Button button1;


    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        findViews();
        setListeners();

        imageView = findViewById(R.id.image_view);

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });

        setOnClick();
    }

    protected void setOnClick() {
        ImageButton imageButton = findViewById(R.id.cancel);

        // lambda式
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
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
        button1.setOnClickListener(new OnClickListener(){
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

        super.onActivityResult(requestCode, resultCode, data);
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

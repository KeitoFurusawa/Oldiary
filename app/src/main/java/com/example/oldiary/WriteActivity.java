package com.example.oldiary;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class WriteActivity extends AppCompatActivity {
    private final static String TAG = "home";
    private String userId;
    private SharedPreferences preference;
    static final int REQUEST_CAPTURE_IMAGE = 100;
    Button button1;
    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private ImageView imageView;

    //genre
    private static ArrayList<Integer> selectedGenreList = null;
    private boolean fromG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("投稿する");
        }
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        userId =  preference.getString("UserID", "");
        checkUserId();
        checkFromG();
        setOnClick();
        setOnClickPost();
        findViews();
        setListeners();
        setOnClickGenre();

        imageView = findViewById(R.id.image_view);

        findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });
    }

    private void checkFromG() {
        Intent i = getIntent();
        if (i.getBooleanExtra("FromG", false)) {
            fromG = true;
            EditText et = findViewById(R.id.editTextTextMultiLine2);
            et.setText(i.getStringExtra("TEXT"));
            selectedGenreList = i.getIntegerArrayListExtra("GenreList");
        }
    }

    protected void checkUserId() {
        if (userId.equals("")) {
            Log.e(TAG, "failed to get User ID");
        } else {
            Log.d(TAG, userId);
        }
    }

    protected void setOnClick() {
        Button imageButton = findViewById(R.id.cancel);

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
            if (userId.equals("id_0")) {
                Toast.makeText(WriteActivity.this, "デバッグユーザ0は投稿できません", Toast.LENGTH_SHORT).show();
            }
            else if (txt.length() == 0) {
                Log.d(TAG, dateTime());
                Toast.makeText(WriteActivity.this, "投稿内容がありません", Toast.LENGTH_SHORT).show();
            } else {
                ReadAndWrite rad = new ReadAndWrite();
                Calendar c = Calendar.getInstance();
                TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
                c.setTimeZone(tz); //日本時間に設定
                rad.writeDiary(userId, txt, dateTime(), c.getTimeInMillis(), selectedGenreList);
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
            }
        });
    }

    private void setOnClickGenre() {
        Button btnGenre = findViewById(R.id.genre);
        btnGenre.setOnClickListener(v -> {
            if (userId.equals("id_0")) {
                Toast.makeText(WriteActivity.this, "デバッグユーザ0では制限されています", Toast.LENGTH_SHORT).show();
            } else {
                TextView textView = findViewById(R.id.editTextTextMultiLine2);
                String txt = textView.getText().toString();
                Intent i = new Intent(getApplication(), PutGenreActivity.class);
                i.putExtra("TEXT", txt);
                startActivity(i);
            }
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
        button1 = findViewById(R.id.camera);
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

    private String dateTime() {
        int year, month, date, hour, minute, second;
        Calendar c = Calendar.getInstance();
        Log.d(TAG, String.valueOf(c));
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        c.setTimeZone(tz); //日本時間に設定
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        date = c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        return String.format("%d/%02d/%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
    }
}

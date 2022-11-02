package com.example.oldiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CreateActivity extends AppCompatActivity {
    private String phoneNumber;
    private static final String TAG = "CreateActivity";
    private static final String CACHE = "cache.txt";
    private DatabaseReference mDatabase;
    private static String getResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setOnClick();
        setOnClick2();
    }
    //Back
    protected void setOnClick() {
        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        });
    }
    //Next
    protected void setOnClick2() {
        Button ButtonNext = findViewById(R.id.button_next);
        TextView textView = findViewById(R.id.editTextPhoneNumber);

        ButtonNext.setOnClickListener(v -> {
            phoneNumber = textView.getText().toString();
            if (phoneNumber.length() > 8) { //エラーハンドル
                String userId = "id_" + phoneNumber;
                getData(userId);
            } else {
                //debug用 0は通過
                if (phoneNumber.equals("0")) {
                    //格納されている文字列の比較は.equalsを使わないといけない
                    Intent intent = new Intent(getApplication(), Create2Activity.class);
                    intent.putExtra("PhoneNumber", phoneNumber);
                    startActivity(intent);
                } else { //電話番号の長さが9未満はエラー
                    Log.d(TAG, "length of phoneNumber is not enough");
                    Toast.makeText(CreateActivity.this, "正しい電話番号を入力してください", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //onComplete
    // データベースから一度だけ情報を読み取る
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
                    /*
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("verification.txt", CreateActivity.this.MODE_PRIVATE));
                        outputStreamWriter.write(value);
                        outputStreamWriter.close();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "wERROR: FileNotFoundException");
                    } catch (IOException e) {
                        Log.e(TAG, "wERROR: IOException");
                    }
                    Log.d(TAG, "this is the result in OnCompleteListener: " + value);
                    /*
                    ReadWriteOnCache rwCache = new ReadWriteOnCache(CACHE);
                    rwCache.writeOnCache(value); //キャッシュファイルに書き込み
                    */
                }
            }
        });
        /*
        StringBuffer ret = new StringBuffer();
        try {
            InputStream inputStream = openFileInput("verification.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                ret.append(bufferedReader.readLine());
            }
        } catch (IOException e) {
            Log.e(TAG, "rERROR: IOException");
        }
        Log.d(TAG, "this is Return value: " + ret.toString());
        return ret.toString();

         */
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
        if (!(getResult.equals("null"))) { //idでサーバを検索した時登録済みの場合はエラー
            Log.e(TAG, "ERROR: that PhoneNumber was already registered");
            Toast.makeText(CreateActivity.this, "その電話番号はすでに使われています。", Toast.LENGTH_SHORT).show();
        } else { //未登録の番号はOK
            Intent intent = new Intent(getApplication(), Create2Activity.class);
            intent.putExtra("PhoneNumber", phoneNumber);
            startActivity(intent);
        }
    }

    /*
        try {
            Thread.sleep(1000); // (1000ミリ秒)間だけ処理を止める
        } catch (InterruptedException e) {
            Log.e(TAG, "rERROR: InterruptedException");
        }
        */
    //ReadWriteOnCache rwCache = new ReadWriteOnCache(CACHE);
    //String ret =  rwCache.readOnCache();
    //rwCache.deleteCache();
    //return ret;
    // キーバリューデータを試す

    /*
    public String readData() {
        StringBuffer ret = new StringBuffer();
        try {
            InputStream inputStream = openFileInput("verification.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                ret.append(bufferedReader.readLine());
            }
        } catch (IOException e) {
            Log.e(TAG, "rERROR: IOException");
        }
        Log.d(TAG, "this is read result " + ret.toString());
        return ret.toString();
    }
    */
}
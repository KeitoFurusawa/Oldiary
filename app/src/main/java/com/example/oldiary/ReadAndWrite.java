package com.example.oldiary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ReadAndWrite extends CreateActivity {
    private static final String TAG = "firebase";
    private static final String CACHE = "cache.txt";
    private  DatabaseReference mDatabase;




    public ReadAndWrite() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public static void writeMessage(String path, String message) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(message);
    }
    public void addNewUser(String phoneNumber, String password) {
        StringBuffer sb = new StringBuffer();
        sb.append("id_");
        User user = new User(phoneNumber, password);
        sb.append(phoneNumber);
        String ID = sb.toString();
        Log.d(TAG, ID);
        mDatabase.child("users").child(ID).setValue(user);
    }


    /*  コンテキスト関連でトラブったので一旦CreateActivityに直書き
    データベースから一度だけ情報を読み取る
    public String getData(String userId) {
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    //Log.d(TAG, "this is inner result " + value);

                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("verification.txt", ReadAndWrite.this.MODE_PRIVATE));
                        outputStreamWriter.write(value);
                        outputStreamWriter.close();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "ERROR FileNotFoundException");
                    } catch (IOException e) {
                        Log.e(TAG, "ERROR IOException");
                    }


                    //ReadWriteOnCache rwCache = new ReadWriteOnCache(CACHE);
                    //rwCache.writeOnCache(value); //キャッシュファイルに書き込み

                }
            }
        });
        StringBuffer ret = new StringBuffer();
        try {
            InputStream inputStream = openFileInput("verification.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                ret.append(bufferedReader.readLine());
            }
        } catch (IOException e) {
            Log.e(TAG, "ERROR IOException");
        }

        return ret.toString();
        //ReadWriteOnCache rwCache = new ReadWriteOnCache(CACHE);
        //String ret =  rwCache.readOnCache();
        //rwCache.deleteCache();
        //return ret;
        // キーバリューデータを試す
    }
    */
}

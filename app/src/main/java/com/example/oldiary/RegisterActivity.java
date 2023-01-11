package com.example.oldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Debug";
    static String contentPath, contentMessage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Button btn =  findViewById(R.id.button);

    }
    /*
    public void onClickSubmit(View v) {
        if (sendMessage() == 0) {
            Toast.makeText(
                    RegisterActivity.this,
                    String.format("Submit data\n[path:%s, message:%s]", contentPath, contentMessage),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    RegisterActivity.this,
                    String.format("Failed to submit data", contentPath, contentMessage),
                    Toast.LENGTH_SHORT).show();
        }
    }
    */

    public void onClickDebug(View v) {
        mDatabase.child("users").child("id_0120002222").child("diaries").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    String[] split = value.split("[,{}]");
                    for (String xs : split) {
                        //Log.d(TAG, xs);
                    }
                    ////Log.d(TAG, value);
                }
            }
        });
        /**
        mDatabase.child("users").child("id_0").child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Log.d("debug", "this is onComplete");
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    //String value = task.getResult().getValue();
                    //Log.d(TAG, "value: " + (String)task.getResult().getValue());
                }
            }
        });

         **/
    }

    /*
    public int sendMessage() {
        EditText editTextPath = (EditText) findViewById(R.id.editTextTextPath);
        EditText editTextMessage = (EditText) findViewById(R.id.editTextTextMessage);
        contentPath = editTextPath.getText().toString(); //static
        contentMessage = editTextMessage.getText().toString(); //static
        ReadAndWrite.writeMessage(contentPath, contentMessage); //送信

        return 0;
    }
    */


}
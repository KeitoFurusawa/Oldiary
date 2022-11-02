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
}

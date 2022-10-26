package com.example.oldiary;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadAndWrite {
    private static final String TAG = "CreateActivity";
    private DatabaseReference mDatabase;

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

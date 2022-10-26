package com.example.oldiary;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadAndWrite {
    private DatabaseReference mDatabase;
    public static void writeMessage(String path, String message) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(message);
    }

    public void addNewUser(String phoneNumber, String password) {
        String idTag = "id_";
        User user = new User(phoneNumber, password);
        mDatabase.child("users").child(idTag + phoneNumber).setValue(user);
    }
}

package com.example.oldiary;


public class User {
    public String phoneNumber;
    public String password;
    public String userName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userName = "null";
    }
}

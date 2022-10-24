package com.example.oldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    static String contentPath, contentMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Button btn =  findViewById(R.id.button);
        /*
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editTextPath = (EditText) findViewById(R.id.editTextTextPath);
                        EditText editTextMessage = (EditText) findViewById(R.id.editTextTextMessage);
                        String contentPath = editTextPath.getText().toString();
                        String contentMessage = editTextMessage.getText().toString();
                        Toast.makeText(RegisterActivity.this, String.format("Submit data\n[path:%s, message:%s]", contentPath, contentMessage), Toast.LENGTH_SHORT).show();
                    }
                }
        );
         */
    }

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

    public int sendMessage() {
        EditText editTextPath = (EditText) findViewById(R.id.editTextTextPath);
        EditText editTextMessage = (EditText) findViewById(R.id.editTextTextMessage);
        contentPath = editTextPath.getText().toString(); //static
        contentMessage = editTextMessage.getText().toString(); //static
        ReadAndWrite.writeMessage(contentPath, contentMessage); //送信

        return 0;
    }
}
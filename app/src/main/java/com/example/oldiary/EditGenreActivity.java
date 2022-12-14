package com.example.oldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class EditGenreActivity extends AppCompatActivity {
    private static ArrayList<Integer> selectedGenreList = new ArrayList<>();//
    public static boolean bStates = false;//
    private DatabaseReference mDatabase;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String userId;
    private String userName;
    private String comment;
    private final static String TAG = "edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_genre);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        bStates = true;
        selectedGenreList = new ArrayList<Integer>();
        Intent intent1 = getIntent();
        getUserId();
        userName = intent1.getStringExtra("UserName");
        comment = intent1.getStringExtra("Comment");
        Log.d(TAG, String.format("check intent: %s %s %s", userId, userName, comment)); //

        setOnClick(); //???????????????

        ListView listView = findViewById(R.id.listViewGenre);
        List<GenreData> list = new ArrayList<>();
        new GenreData();
        for (int i = 0; i < GenreData.genreList.length; i++) {
            GenreData genreData = new GenreData();
            genreData.setTextData(GenreData.genreList[i]);
            //genreData.setChecked(true);
            list.add(genreData);
        }
        MyAdapter2 adapter = new MyAdapter2(EditGenreActivity.this,R.layout.genre_list,list);
        loadData(adapter, listView);
        //listView.setOnClickListener();
        /*
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "pushed pos: " + position);
                        adapter.onClickElm(position);
                    }
        });
        */
    }

    protected void getUserId() {
        userId = preference.getString("UserID", "");
    }

    private void loadData(MyAdapter2 adapter2, ListView listView) {
        selectedGenreList = new ArrayList<>();//
        Intent intent = getIntent();
        selectedGenreList.add(intent.getIntExtra("GenreCode1", -1));
        selectedGenreList.add(intent.getIntExtra("GenreCode2", -1));
        selectedGenreList.add(intent.getIntExtra("GenreCode3", -1));
        //Log.d(TAG, String.valueOf(selectedGenreList));
        adapter2.alreadyChecked(selectedGenreList);
        listView.setAdapter(adapter2);
    }


    public void addSelectedGenreListList(int pos) {
        this.selectedGenreList.add(pos);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        Log.d(TAG, sb.toString());
    }

    public void removeSelectedGenreListList(int pos) {
        this.selectedGenreList.remove(selectedGenreList.indexOf(pos));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedGenreList.size(); i++) {
            sb.append(selectedGenreList.get(i));
        }
        Log.d(TAG, sb.toString());
    }

    public int sizeOfSelectedGenreList() {
        return selectedGenreList.size();
    }


    public void enableButton() { //?????????????????????3????????????????????????
        //Button b = findViewById(R.id.buttonConfirm);
        //???????????????????????????????????????
        bStates = true; //?????????????????????
    }

    public void disableButton() { //???????????????????????????????????????????????????
        //Button b = findViewById(R.id.buttonConfirm);
        //??????????????????????????????
        bStates = false;
    }

    //???????????????
    protected void setOnClick() {
        Button button = findViewById(R.id.buttonConfirm);
        button.setOnClickListener(v -> {
            Log.d(TAG, String.valueOf("Button States: " + bStates)); //??????
            if (bStates) { //??????????????????
                new AlertDialog.Builder(EditGenreActivity.this)
                        .setTitle("??????")
                        .setMessage(String.format("1: %s\n2: %s\n3: %s\n\n??????????????????????????????????????????????????????",
                                GenreData.genreList[selectedGenreList.get(0)], GenreData.genreList[selectedGenreList.get(1)], GenreData.genreList[selectedGenreList.get(2)]))
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //mDatabase.child("users").child(userId).child("userName").setValue(userName);
                                //mDatabase.child("users").child(userId).child("comment").setValue(comment);
                                //mDatabase.child("users").child(userId).child("favoriteGenre").setValue(selectedGenreList); //firebase??????????????????
                                Intent intentDone = new Intent(getApplication(), EditProfileActivity.class);
                                intentDone.putExtra("GenreCode1", selectedGenreList.get(0));
                                intentDone.putExtra("GenreCode2", selectedGenreList.get(1));
                                intentDone.putExtra("GenreCode3", selectedGenreList.get(2));
                                intentDone.putExtra("BACK", true);
                                intentDone.putExtra("UserName", userName);
                                intentDone.putExtra("Comment", comment);
                                //AlarmActivity alm = new AlarmActivity();
                                //alm.mDestroy();
                                startActivity(intentDone);
                            }
                        })
                        .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ;
                            }
                        })
                        .show();
            } else { //?????????
                Toast.makeText(EditGenreActivity.this, "???????????????3?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
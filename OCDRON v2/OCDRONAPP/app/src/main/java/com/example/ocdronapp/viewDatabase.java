package com.example.ocdronapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class viewDatabase extends AppCompatActivity {
    RecyclerView recyclerView;

    FloatingActionButton vdbBackToHome_btn;
    ImageView empty_ImageView;

    DBHelper myDB;
    ArrayList<String> id_Num, phLabel,phScale, dateToday, timeToday;
    com.example.ocdronapp.customAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdatabase);

        recyclerView = findViewById(R.id.rv);

        empty_ImageView = findViewById(R.id.empty_ImageView);
        vdbBackToHome_btn = findViewById(R.id.btn_BackToHome);

        myDB = new DBHelper(viewDatabase.this);
        id_Num = new ArrayList<>();
        phLabel = new ArrayList<>();
        phScale = new ArrayList<>();
        dateToday = new ArrayList<>();
        timeToday = new ArrayList<>();


        storeDataInArray();

        customAdapter = new customAdapter(viewDatabase.this,this, id_Num, phLabel,phScale, dateToday, timeToday);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewDatabase.this));

        vdbBackToHome_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(viewDatabase.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }

    }

    void storeDataInArray(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_ImageView.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id_Num.add(cursor.getString(0));
                phLabel.add(cursor.getString(1));
                phScale.add(cursor.getString(2));
                dateToday.add(cursor.getString(3));
                timeToday.add(cursor.getString(4));
            }
            empty_ImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }



}
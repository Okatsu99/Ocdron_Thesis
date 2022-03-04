package com.example.ocdronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { //  to give loading time for the application on start
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                loading.this.startActivity(i);
                loading.this.finish();
            }
        }, 3000);

    }
}
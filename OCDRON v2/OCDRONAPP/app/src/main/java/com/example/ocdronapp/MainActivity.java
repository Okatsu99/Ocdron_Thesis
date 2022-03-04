package com.example.ocdronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean backtoExit = false;
    ImageView gotoControl_btn, gotoPhControl_btn, gotoView_DB_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoControl_btn = (ImageView) findViewById(R.id.imgbtnController);
        gotoPhControl_btn = (ImageView) findViewById(R.id.imgbtnPh_Control);
        gotoView_DB_btn = (ImageView) findViewById(R.id.imgbtnView_DB);

        View.OnClickListener gotoController = new View.OnClickListener() {//button click to go controller activity
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), controller.class);
                startActivity(i);
                finish();
            }
        };
        gotoControl_btn.setOnClickListener(gotoController);

        View.OnClickListener gotoPh_Control = new View.OnClickListener(){ //button click to go phfeedselector activity
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, phSensorController.class);
                startActivity(i);
                finish();
            }
        };
        gotoPhControl_btn.setOnClickListener(gotoPh_Control);

        View.OnClickListener gotoView_DB = new View.OnClickListener(){ //button click to go view database activity
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, viewDatabase.class);
                startActivity(i);
                finish();
            }
        };
        gotoView_DB_btn.setOnClickListener(gotoView_DB);
    }

    @Override
    public void onBackPressed() {
        if (backtoExit) {
            super.onBackPressed();
            finish();
            return;
        }

        this.backtoExit = true;
        Toast.makeText(this, "Double press the back button to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backtoExit =false;
                finish();
            }
        }, 2000);
    }

}
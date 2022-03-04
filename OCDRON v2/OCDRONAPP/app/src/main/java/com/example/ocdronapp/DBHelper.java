package com.example.ocdronapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper { // sqlite database functions
    private Context context;
    private static final String DATABASE_NAME = "OCDRON.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "PHDATA";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PH_LEVEL = "PHLEVEL";
    private static final String COLUMN_PH_SCALE = "SCALE";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_TIME = "TIME";


    DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION );
        this.context =  context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // creates a table
        String query = "CREATE TABLE "+ TABLE_NAME +
                "(" +COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PH_LEVEL + " TEXT, "+
                COLUMN_PH_SCALE + " TEXT, "+
                COLUMN_DATE + " TEXT, "+
                COLUMN_TIME +" TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABlE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void addRecord(String pHdata,String pHscale, String date, String time){ // adds record to the table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv =  new ContentValues();

        cv.put(COLUMN_PH_LEVEL,pHdata);
        cv.put(COLUMN_PH_SCALE,pHscale);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_TIME,time);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context,"Failed to save data. Pls try again",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Saved Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){ // reads all data from the table
        String query ="SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
}

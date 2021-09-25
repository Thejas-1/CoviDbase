package com.example.covidbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Map;

public class RespiratoryDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    private Context context;
    public static final String DATABASE_NAME = "Respiratory.db";
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + RespiratoryData.Respiratory.TABLE_NAME +
            " ( " + RespiratoryData.Respiratory._ID + " INTEGER PRIMARY KEY," +
            RespiratoryData.Respiratory.BREATH_RATE + " TEXT ," +
            RespiratoryData.Respiratory.NAUSEA + " TEXT ," +
            RespiratoryData.Respiratory.HEADACHE + " TEXT ," +
            RespiratoryData.Respiratory.DIARRHEA + " TEXT ," +
            RespiratoryData.Respiratory.SOAR + " TEXT ," +
            RespiratoryData.Respiratory.FEVER + " TEXT ," +
            RespiratoryData.Respiratory.MUSCLE + " TEXT ," +
            RespiratoryData.Respiratory.LOSS + " TEXT ," +
            RespiratoryData.Respiratory.COUGH + " TEXT ," +
            RespiratoryData.Respiratory.BREATH + " TEXT ," +
            RespiratoryData.Respiratory.TIREDNESS + " TEXT) ";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RespiratoryData.Respiratory.TABLE_NAME;

    public RespiratoryDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
        System.out.println("SQL Create Query:"+SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addRecord(Map dataMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        RespiratoryData.Respiratory res = new RespiratoryData.Respiratory();
        System.out.println(dataMap);

        values.put(res.BREATH_RATE, String.valueOf(dataMap.get("BREATH_RATE")));
        values.put(res.NAUSEA, String.valueOf(dataMap.get("NAUSEA")));
        values.put(res.HEADACHE, String.valueOf(dataMap.get("HEADACHE")));
        values.put(res.DIARRHEA, String.valueOf(dataMap.get("DIARRHEA")));
        values.put(res.SOAR, String.valueOf(dataMap.get("SOAR")));
        values.put(res.FEVER, String.valueOf(dataMap.get("FEVER")));
        values.put(res.MUSCLE, String.valueOf(dataMap.get("MUSCLE")));
        values.put(res.LOSS, String.valueOf(dataMap.get("LOSS")));
        values.put(res.COUGH, String.valueOf(dataMap.get("COUGH")));
        values.put(res.BREATH, String.valueOf(dataMap.get("BREATH")));
        values.put(res.TIREDNESS, String.valueOf(dataMap.get("TIREDNESS")));



        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(res.TABLE_NAME, null, values);
        if(newRowId == -1){

        }
    }

   /* protected void finalize(){
        dbHelper.close();
        super.onDestroy();
    }*/
}

package com.example.covidbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RespiratoryDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    private Context context;
    public static final String DATABASE_NAME = "Respiratory.db";
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + RespiratoryData.Respiratory.TABLE_NAME +
            " ( " + RespiratoryData.Respiratory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RespiratoryData.Respiratory.ACCEL_READINGS + " STRING ) ";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RespiratoryData.Respiratory.TABLE_NAME;

    public RespiratoryDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addRecord(String accelReadings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        RespiratoryData.Respiratory res = new RespiratoryData.Respiratory();
        values.put(res.ACCEL_READINGS, accelReadings);

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

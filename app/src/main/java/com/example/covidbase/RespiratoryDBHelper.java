package com.example.covidbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RespiratoryDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Respiratory.db";
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + RespiratoryData.Respiratory.TABLE_NAME +
            " ( " + RespiratoryData.Respiratory._ID + " INTEGER PRIMARY KEY," +
            RespiratoryData.Respiratory.ACCEL_READINGS + "TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RespiratoryData.Respiratory.TABLE_NAME;

    public RespiratoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}

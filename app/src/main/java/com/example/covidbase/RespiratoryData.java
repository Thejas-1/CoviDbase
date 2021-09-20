package com.example.covidbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RespiratoryData {
    private RespiratoryData() {};

    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + Respiratory.TABLE_NAME +
            " ( " + Respiratory._ID + " INTEGER PRIMARY KEY," +
            Respiratory.ACCEL_READINGS + "TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Respiratory.TABLE_NAME;

    public static class Respiratory implements BaseColumns {
        public static final String TABLE_NAME = "RESPIRATORY_DATA";
        public static final String INDEX = "INDEX";
        public static final String ACCEL_READINGS = "VARIATIONS";
    }

    public abstract class RespiratoryDBHelper extends SQLiteOpenHelper {
        //Increment version on changing the schema
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Respiratory.db";
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

}

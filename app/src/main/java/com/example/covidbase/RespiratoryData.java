package com.example.covidbase;

import android.provider.BaseColumns;

public class RespiratoryData {
    private RespiratoryData() {};

    public static class Respiratory implements BaseColumns {
        public static final String TABLE_NAME = "RESPIRATORY_DATA";
        public static final String INDEX = "INDEX";
        public static final String ACCEL_READINGS = "VARIATIONS";
    }

}

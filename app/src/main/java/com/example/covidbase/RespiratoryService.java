package com.example.covidbase;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class RespiratoryService extends Service implements SensorEventListener {
    public RespiratoryService() {
    }
    public SensorManager accelManage;
    public Sensor senseAccel;

    int index = 0;
    float accelValuesX[] = new float[128];
    float accelValuesY[] = new float[128];
    float accelValuesZ[] = new float[128];
    int k = 0;

    /*RespiratoryDBHelper dbHelper = new RespiratoryDBHelper(null);
    SQLiteDatabase db = dbHelper.getWritableDatabase();*/

    /*public void CallFallRecognition(){
        float prev = 0;
        float curr = 0;
        prev = 10;

        for(int i=11; i<128; i++){
            curr = accelValuesZ[i];
            if(Math.abs(prev - curr) > 10){
                Toast.makeText(this, "Fall Detected", Toast.LENGTH_LONG).show();
                //sendSMS();
            }
        }
    }*/

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            index++;
            accelValuesX[index] = sensorEvent.values[0];
            accelValuesY[index] = sensorEvent.values[1];
            accelValuesZ[index] = sensorEvent.values[2];

            if(index < 127) {
                RespiratoryDBHelper dbHelper = new RespiratoryDBHelper(RespiratoryService.this);
                dbHelper.addRecord(Float.toString(accelValuesZ[index]));
            }
            //Create a new map of values, where column names are the keys
           /* ContentValues values = new ContentValues();
            RespiratoryData.Respiratory res = new RespiratoryData.Respiratory();
            values.put(res.ACCEL_READINGS, accelValuesZ[index]);

            //Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(res.TABLE_NAME, null, values);*/
            /*if(index >= 127 ){
                index = 0;
                accelManage.unregisterListener(this);
                CallFallRecognition();
                accelManage.registerListener(this,senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int Accuracy){

    }

    @Override
    public void onCreate(){
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this,senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
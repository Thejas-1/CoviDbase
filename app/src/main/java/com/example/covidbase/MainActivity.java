package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button heartRate = (Button) findViewById(R.id.button2);
        Button respiratoryRate = (Button) findViewById(R.id.button);
//        setContentView(R.layout.activity_heart_rate);

        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent heartRateIntent = new Intent(getApplicationContext(), HeartRateActivity.class);
                startActivity(heartRateIntent);
            }
        });

        respiratoryRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent respiratoryRateIntent = new Intent(getApplicationContext(),RespiratoryRate.class);
                startActivity(respiratoryRateIntent);
            }
        });
    }
}
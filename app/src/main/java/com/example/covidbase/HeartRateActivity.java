package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HeartRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        Button goBack = (Button) findViewById(R.id.button6);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });

        Button measureHearRate = (Button) findViewById(R.id.button5);

        measureHearRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent heartRateService = new Intent(getApplicationContext(), HeartRateService.class);
                startService(heartRateService);
            }
        });
    }
}
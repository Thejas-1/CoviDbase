package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RespiratoryRate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiratory_rate);
        Button goBack = (Button) findViewById(R.id.button8);
        Button resRate = (Button) findViewById(R.id.button7);

        resRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accelIntent = new Intent(getApplicationContext(), RespiratoryService.class);
                /*Bundle b = new Bundle();
                b.putString("phone","14807915874");
                accelIntent.putExtras(b);*/
                startService(accelIntent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

}
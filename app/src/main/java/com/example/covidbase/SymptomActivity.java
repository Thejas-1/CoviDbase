package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class SymptomActivity extends AppCompatActivity {

    private float feverRatingValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        Button goBack = (Button) findViewById(R.id.button11);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SymptomActivity.this, MainActivity.class);
                /*RatingBar feverRating = (RatingBar) findViewById(R.id.ratingBar);
                feverRating.getRating();*/
                startActivity(intent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.button12);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SymptomActivity.this, MainActivity.class);
                RatingBar feverRating = (RatingBar) findViewById(R.id.ratingBar);

                //System.out.println("Fever Rating" + feverRating.getRating());
                feverRatingValue = feverRating.getRating();
                /*Bundle b = new Bundle();
                intent.putExtras(b);*/
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            Thread thread1;
            thread1 = new Thread((Runnable) () -> {
                /*accelManage.unregisterListener(RespiratoryService.this);*/

                Intent intent = new Intent("SendingSymptomRatings");

                System.out.println("onDestroy Called within Symptom monitor.");
                Bundle b = new Bundle();
                b.putString("feverRatingValue", String.valueOf(feverRatingValue));
                intent.putExtras(b);
                LocalBroadcastManager.getInstance(SymptomActivity.this).sendBroadcast(intent);
            });
            thread1.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
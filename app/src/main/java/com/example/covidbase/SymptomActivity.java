package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class SymptomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private float feverRatingValue = 0;
    private String currentItem = "", prevItem = "";
    private float Nausea = 0;
    private float Headache = 0;
    private float Diarrhea = 0;
    private float Soar = 0;
    private float Fever = 0;
    private float Muscle = 0;
    private float Loss = 0;
    private float Cough = 0;
    private float Breath = 0;
    private float Tiredness = 0;
    private String breathingRateValue = "0";

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "Clicked on: " + list.get(position), Toast.LENGTH_SHORT).show();
        System.out.println("Value Changed");
        Spinner dropDown = (Spinner) findViewById(R.id.spinner);
        currentItem = dropDown.getSelectedItem().toString();
        if(currentItem == prevItem){
            //Do nothing for now
        }
        else {
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            //ratingBar.setRating(0);
            Spinner dropDownKey = (Spinner) findViewById(R.id.spinner);
            switch (dropDownKey.getSelectedItem().toString()){
                case "Nausea":
                    ratingBar.setRating(Nausea);
                    break;
                case "Headache":
                    ratingBar.setRating(Headache);
                    break;
                case "Diarrhea":
                    ratingBar.setRating(Diarrhea);
                    break;
                case "Soar Throat":
                    ratingBar.setRating(Soar);
                    break;
                case "Fever":
                    ratingBar.setRating(Fever);
                    break;
                case "Muscle Ache":
                    ratingBar.setRating(Muscle);
                    break;
                case "Loss of Smell or Taste":
                    ratingBar.setRating(Loss);
                    break;
                case "Cough":
                    ratingBar.setRating(Cough);
                    break;
                case "Shortness of Breath":
                    ratingBar.setRating(Breath);
                    break;
                case "Feeling Tired":
                    ratingBar.setRating(Tiredness);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //do something
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        Bundle b = getIntent().getExtras();
        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        breathingRateValue = b.getString("BreathRate");
        //prevItem = dropdown.getSelectedItem().toString();
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
                //feverRatingValue = feverRating.getRating();
                /*Bundle b = new Bundle();
                intent.putExtras(b);*/
                startActivity(intent);
                finish();
            }
        });

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Spinner dropDownKey = (Spinner) findViewById(R.id.spinner);
                switch (dropDownKey.getSelectedItem().toString()){
                    case "Nausea":
                        Nausea = ratingBar.getRating();
                        break;
                    case "Headache":
                        Headache = ratingBar.getRating();
                        break;
                    case "Diarrhea":
                        Diarrhea = ratingBar.getRating();
                        break;
                    case "Soar Throat":
                        Soar = ratingBar.getRating();
                        break;
                    case "Fever":
                        Fever = ratingBar.getRating();
                        break;
                    case "Muscle Ache":
                        Muscle = ratingBar.getRating();
                        break;
                    case "Loss of Smell or Taste":
                        Loss = ratingBar.getRating();
                        break;
                    case "Cough":
                        Cough = ratingBar.getRating();
                        break;
                    case "Shortness of Breath":
                        Breath = ratingBar.getRating();
                        break;
                    case "Feeling Tired":
                        Tiredness = ratingBar.getRating();
                        break;
                    default:
                        break;
                }
            }
        });

        Spinner symptomDropdown = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        symptomDropdown.setAdapter(adapter);

        symptomDropdown.setOnItemSelectedListener(this);

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
                b.putString("Fever", String.valueOf(Fever));
                b.putString("Nausea", String.valueOf(Nausea));
                b.putString("Headache", String.valueOf(Headache));
                b.putString("Diarrhea", String.valueOf(Diarrhea));
                b.putString("Soar", String.valueOf(Soar));
                b.putString("Muscle", String.valueOf(Muscle));
                b.putString("Loss", String.valueOf(Loss));
                b.putString("Cough", String.valueOf(Cough));
                b.putString("Breath", String.valueOf(Breath));
                b.putString("Tiredness", String.valueOf(Tiredness));
                b.putString("breathingRateValue", breathingRateValue);

                intent.putExtras(b);
                LocalBroadcastManager.getInstance(SymptomActivity.this).sendBroadcast(intent);
            });
            thread1.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
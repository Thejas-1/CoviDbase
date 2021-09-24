package com.example.covidbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Uri fileUri;
    private static final int VIDEO_CAPTURE = 101;
    private String rootPath = Environment.getExternalStorageDirectory().getPath();
    private Boolean breathRateFlag = false;

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),"Current Longitute: " + location.getLongitude() + " Current Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
    }

    public class DownloadTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Starting to execute Background Task", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... text) {

            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File directory = new File(SDCardRoot, "/my_folder/"); //create directory to keep your downloaded file
            if (!directory.exists())
            {
                directory.mkdir();
            }
            //publishProgress();
//            Toast.makeText(getApplicationContext(),"In Background Task", Toast.LENGTH_LONG).show();
            String fileName = "Action1" + ".mp4"; //song name that will be stored in your device in case of song
            //String fileName = "myImage" + ".jpeg"; in case of image
            try
            {
                InputStream input = null;
                try{

                    URL url = new URL("https://www.signingsavvy.com/media/mp4-ld/7/7231.mp4"); // link of the song which you want to download like (http://...)
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setReadTimeout(95 * 1000);
                    urlConnection.setConnectTimeout(95 * 1000);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("X-Environment", "android");


                    urlConnection.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            /** if it necessarry get url verfication */
                            //return HttpsURLConnection.getDefaultHostnameVerifier().verify("your_domain.com", session);
                            return true;
                        }
                    });
                    urlConnection.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());


                    urlConnection.connect();
                    input = urlConnection.getInputStream();
                    //input = url.openStream();
                    OutputStream output = new FileOutputStream(new File(directory, fileName));

                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0)
                        {
                            output.write(buffer, 0, bytesRead);

                        }
                        output.close();
                        //Toast.makeText(getApplicationContext(),"Read Done", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception exception)
                    {


                        //Toast.makeText(getApplicationContext(),"output exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                        Log.d("Error", String.valueOf(exception));
                        publishProgress(String.valueOf(exception));
                        output.close();

                    }
                }
                catch (Exception exception)
                {

                    //Toast.makeText(getApplicationContext(), "input exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                    publishProgress(String.valueOf(exception));

                }
                finally
                {
                    input.close();
                }
            }
            catch (Exception exception)
            {
                publishProgress(String.valueOf(exception));
            }

            return "true";
        }




        @Override
        protected void onProgressUpdate(String... text) {
            Toast.makeText(getApplicationContext(), "In Background Task" + text[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String text){
            VideoView vv = (VideoView) findViewById(R.id.videoView);
            vv.setVideoPath(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");
            vv.start();
            Button bt4 = (Button)findViewById(R.id.button3);
            bt4.setEnabled(true);
        }
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                String url = "http://10.218.107.121/cse535/upload_video.php";
                String charset = "UTF-8";
                String group_id = "40";
                String ASUid = "1200072576";
                String accept = "1";


                File videoFile = new File(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");
                String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
                String CRLF = "\r\n"; // Line separator required by multipart/form-data.

                URLConnection connection;

                connection = new URL(url).openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                try (
                        OutputStream output = connection.getOutputStream();
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
                ) {
                    // Send normal accept.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"accept\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(accept).append(CRLF).flush();

                    // Send normal accept.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"id\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(ASUid).append(CRLF).flush();

                    // Send normal accept.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"group_id\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(group_id).append(CRLF).flush();


                    // Send video file.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + videoFile.getName() + "\"").append(CRLF);
                    writer.append("Content-Type: video/mp4; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
                    writer.append(CRLF).flush();
                    FileInputStream vf = new FileInputStream(videoFile);
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = vf.read(buffer, 0, buffer.length)) >= 0)
                        {
                            output.write(buffer, 0, bytesRead);

                        }
                        //   output.close();
                        //Toast.makeText(getApplicationContext(),"Read Done", Toast.LENGTH_LONG).show();
                    }catch (Exception exception)
                    {


                        //Toast.makeText(getApplicationContext(),"output exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                        Log.d("Error", String.valueOf(exception));
                        publishProgress(String.valueOf(exception));
                        // output.close();

                    }

                    output.flush(); // Important before continuing with writer!
                    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


                    // End of multipart/form-data.
                    writer.append("--" + boundary + "--").append(CRLF).flush();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Request is lazily fired whenever you need to obtain information about response.
                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                System.out.println(responseCode); // Should be 200

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            Toast.makeText(getApplicationContext(), "In Background Task " + text[0], Toast.LENGTH_LONG).show();
        }

    }

    public float[] denoise(float[] data, int filter){

        float[] movingAvgArr = new float[231];
        float movingAvg = 0;

        for(int i=0; i< data.length; i++){
            movingAvg += data[i];
            if(i+1 < filter) {
                continue;
            }
            movingAvgArr[i] = ((movingAvg)/filter);
            movingAvg -= data[i+1 - filter];
        }

        return movingAvgArr;

    }

    public int peakFinding(float[] data) {

        float diff, prev, slope = 0;
        int zeroCrossings = 0;
        int j = 0;
        prev = data[0];

        //Get initial slope
        while(slope == 0 && j + 1 < data.length){
            diff = data[j + 1] - data[j];
            if(diff != 0){
                slope = diff/abs(diff);
            }
            j++;
        }

        //Get total number of zero crossings in data curve
        for(int i = 1; i<data.length; i++) {

            diff = data[i] - prev;
            prev = data[i];

            if(diff == 0) continue;

            float currSlope = diff/abs(diff);

            if(currSlope == -1* slope){
                slope *= -1;
                zeroCrossings++;
            }
        }

        return zeroCrossings;
    }

    public void startRecording()
    {
        /*File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);*/
        /*File mediaFile = new
                File(*//*rootPath
                +*//* Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/Pictures");*/


        /*if (!mediaFile.exists()
        ) {
            if (!mediaFile.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }*/
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //intent = intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
        //fileUri = Uri.fromFile(MEDIA_TYPE_VIDEO);
        /*fileUri = Uri.fromFile(mediaFile);

        Uri fileUri = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", mediaFile);
        //intent.setDataAndType(fileUri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, VIDEO_CAPTURE);
        }

    }

    //@Override
    protected void onPostExecute(String text){
        VideoView vv = (VideoView) findViewById(R.id.videoView);
        vv.setVideoPath(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");
        vv.start();
        Button bt4 = (Button)findViewById(R.id.button3);
        bt4.setEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {



                try {
                    AssetFileDescriptor videoAsset = null;
                    String extStorageState = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
                        System.out.println("Yes");
                    }

                    videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");

                    FileInputStream fis = videoAsset.createInputStream();
                File root=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/my_folder/");  //you can replace RecordVideo by the specific folder where you want to save the video
                if (!root.exists()) {
                    System.out.println("No directory");
                    root.mkdirs();
                }

                File file;
                file=new File(root,"videoFile.mp4" );

                FileOutputStream fos = null;

                    fos = new FileOutputStream(file);


                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) > 0) {
                    try {
                        fos.write(buf, 0, len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*fis.close();
                fos.close();*/

                /*File mediaFile = new
                        File(*//*rootPath
                +*//* Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "/my_folder/");*/

                Toast.makeText(this, "Video has been saved to:\n" +
                        rootPath.toString(), Toast.LENGTH_LONG).show();
                /*fileUri = data.getData();
                VideoView myVidView = new VideoView(this);
                myVidView.setVideoURI(fileUri);
                myVidView.start();*/
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            return false;
        }
    }

    public void storagePermissions(Activity activity) {

        int storagePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int REQUEST_EXTERNAL_STORAGE = 1;

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA

        };

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            Log.i("log", "Read/Write Permissions needed!");
        }

        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                REQUEST_EXTERNAL_STORAGE
        );
    }

   /* public void saveToCSV(ArrayList<Integer> data, String path){

        File file = new File(path);

        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);
            String[] header = { "Index", "Data"};
            writer.writeNext(header);
            int i = 0;
            for (int d : data) {
                String dataRow[] = {i + "", d + ""};
                writer.writeNext(dataRow);
                i++;
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    //@Override
    public class BreathingRateDetector implements Runnable{

        public int breathingRate;
        float[] accelValuesX;

        BreathingRateDetector(float[] accelValuesX){
            this.accelValuesX = accelValuesX;
        }

        @Override
        public void run() {

            String csvFilePath = rootPath + "/x_values.csv";
            //saveToCSV(accelValuesX, csvFilePath);

            //Noise reduction from Accelerometer X values
            float[] accelValuesXDenoised = denoise(accelValuesX, 10);

            //csvFilePath = rootPath + "/x_values_denoised.csv";
            //saveToCSV(accelValuesXDenoised, csvFilePath);

            //Peak detection algorithm running on denoised Accelerometer X values
            int  zeroCrossings = peakFinding(accelValuesXDenoised);
            Log.i("log", "Respiratory rate" + breathingRate);
            breathingRate = (zeroCrossings*60)/90;
            TextView breatRate = (TextView) findViewById(R.id.textView);
            breatRate.setText(String.valueOf(breathingRate));
            Log.i("log", "Respiratory rate" + breathingRate);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*TextView breatRate = (TextView) findViewById(R.id.textView);
        breatRate.setText(0);*/

        setContentView(R.layout.activity_main);
        Button heartRate = (Button) findViewById(R.id.button2);
        Button respiratoryRate = (Button) findViewById(R.id.button);

        Button upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
//        setContentView(R.layout.activity_heart_rate);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        //Button bt1 = (Button) findViewById(R.id.button);

        if(!hasCamera()){
            heartRate.setEnabled(false);
        }

        Button playVideo = (Button) findViewById(R.id.button9);

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoView video = (VideoView) findViewById(R.id.videoView);

                MediaController mediaController = new MediaController(MainActivity.this);
// initiate a video view
                //VideoView simpleVideoView = (VideoView) findViewById(R.id.video);
// set media controller object for a video view
                video.setMediaController(mediaController);

                Uri uri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/my_folder/videoFile.mp4"));
                video.setVideoURI(Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/my_folder/videoFile.mp4")));
                video.setVideoURI(uri);
                video.start();
            }
        });

        Button symptomScreenNav = (Button) findViewById(R.id.button3);
        symptomScreenNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SymptomActivity.class);
                startActivity(intent);
            }
        });

        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*DownloadTask dw1 = new DownloadTask();
                Toast.makeText(getApplicationContext(),"Running Background Task", Toast.LENGTH_LONG).show();
                dw1.execute();*/
                /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/
                /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/
                /*Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
                Toast.makeText(getApplicationContext(),"Current Longitute: " + location.getLongitude() + " Current Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();*/

               /* UploadTask up1 = new UploadTask();
                Toast.makeText(getApplicationContext(),"Starting to Upload",Toast.LENGTH_LONG).show();
                up1.execute();*/
                storagePermissions(MainActivity.this);

                startRecording();

                /*DownloadTask dw1 = new DownloadTask();
                Toast.makeText(getApplicationContext(),"Running Background Task", Toast.LENGTH_LONG).show();
                dw1.execute();*/
            }
        });


        respiratoryRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breathRateFlag == false) {
                    Toast.makeText(MainActivity.this, "Place the phone on your chest to measure the breathing",Toast.LENGTH_LONG).show();
                    breathRateFlag = true;
                    Intent accelIntent = new Intent(getApplicationContext(), RespiratoryService.class);
                /*Bundle b = new Bundle();
                b.putString("phone","14807915874");
                accelIntent.putExtras(b);*/
                    startService(accelIntent);
                }
                else {
                    Toast.makeText(MainActivity.this, "There's an ongoing background process for breathing rate!",Toast.LENGTH_LONG).show();
                }
            }
        });

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Intent Values: "+intent.getExtras());
                Bundle b = intent.getExtras();
               // finalAccelReadings = intent.getStringExtra("finalAccelReadings");
                System.out.println("accelValuesX "+(-5.3753*100));
                BreathingRateDetector runnable = new BreathingRateDetector(b.getFloatArray("finalAccelReadings"));

                Thread thread = new Thread(runnable);
                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //breathingRateTextView.setText(runnable.breathingRate + "");

                /*Toast.makeText(HomeScreen.this, "Respiratory rate calculated!", Toast.LENGTH_SHORT).show();
                ongoingBreathingRateProcess = false;*/
                b.clear();
                System.gc();
            }
        },  new IntentFilter("SendingAccelReadings"));

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*System.out.println("Intent Values: "+intent.getExtras());
                Bundle b = intent.getExtras();
                // finalAccelReadings = intent.getStringExtra("finalAccelReadings");
                System.out.println("accelValuesX "+(-5.3753*100));
                BreathingRateDetector runnable = new BreathingRateDetector(b.getFloatArray("finalAccelReadings"));

                Thread thread = new Thread(runnable);
                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //breathingRateTextView.setText(runnable.breathingRate + "");

                *//*Toast.makeText(HomeScreen.this, "Respiratory rate calculated!", Toast.LENGTH_SHORT).show();
                ongoingBreathingRateProcess = false;*//*
                b.clear();
                System.gc();*/
                Bundle b = intent.getExtras();
                System.out.println(b.getString("feverRatingValue"));
                TextView fevVal = (TextView) findViewById(R.id.FeverValue);
                fevVal.setText(b.getString("feverRatingValue"));
                System.out.println(fevVal.getText());
                b.clear();
                System.gc();
            }
        },  new IntentFilter("SendingSymptomRatings"));
    }
}
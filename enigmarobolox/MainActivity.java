package com.enigmarobolox.arm_avi.enigmarobolox;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    Handler mHandler;
    boolean doubleBackToExitPressedOnce = false;

    private String weatherStationUrl = "file:///android_asset/index.html";

    GridView gridView;
    private GridviewAdapter mAdapter;
    private ArrayList<String> listDivision;
    private ArrayList<Integer> listFlag;

    //Variables For JsonObject Parsing
    String responseTemp, responseHumid;

    //Global Variable for store Weather Server IP Address
    String ip;
    String decTemp;

//    String url = "http://192.168.0.102/data";

    private static MainActivity activity;
    TextView status ;
    public static MediaPlayer fireAlart,fireDetected, smokeDetected, intruderDetected;
    int REQUEST_RECEIVE_SMS;

    RelativeLayout fireDetectionBtn, smokeDetectionBtn, intruderDetectionBtn;
    TextView notifierTxt, tempTxt, humidTxt, weatherStatusTxt, tempThresholdDisp;
    RelativeLayout robot, weatherStatusBar;
    ImageView addServerIP, loadServerIP;



    static MainActivity instance(){
        return activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fireDetectionBtn = (RelativeLayout) findViewById(R.id.fireDetectionBtn);
        smokeDetectionBtn = (RelativeLayout) findViewById(R.id.smokeDetectionBtn);
        intruderDetectionBtn = (RelativeLayout) findViewById(R.id.intuderDetectionBtn);
        notifierTxt = (TextView)findViewById(R.id.notifierTxt);
        robot = (RelativeLayout) findViewById(R.id.robot);
        weatherStatusBar = (RelativeLayout) findViewById(R.id.weatherStatusBar);

        //Alart Sound
        fireAlart =  MediaPlayer.create(MainActivity.this,R.raw.firealeart);

        fireDetected = MediaPlayer.create(MainActivity.this,R.raw.firedetected);
        smokeDetected = MediaPlayer.create(MainActivity.this,R.raw.smokedetected);
        intruderDetected = MediaPlayer.create(MainActivity.this,R.raw.intruderdetected);

        tempTxt = (TextView) findViewById(R.id.tempTxt);
        humidTxt = (TextView)findViewById(R.id.humidTxt);
        weatherStatusTxt = (TextView) findViewById(R.id.weatherStatusTxt);
        tempThresholdDisp = (TextView) findViewById(R.id.tempThresholdDisp);


        addServerIP = (ImageView) findViewById(R.id.addServerIP);
        loadServerIP = (ImageView) findViewById(R.id.loadServerIP);

        //SMS Read & Write Permission Function
        smsPermission();


        this.mHandler = new Handler();

        addServerIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ipAdderFunc();
            }
        });

        loadServerIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("ipInfoWeather",Context.MODE_PRIVATE);
                ip = sharedPref.getString("ipaddressWeather","");
                decTemp = sharedPref.getString("decInfo658","");
//                weatherStatusTxt.setText(ip);
                tempThresholdDisp.setText(decTemp);
            }
        });

        fireDetectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fireDetected.isPlaying()){
                   fireDetected.stop();
                    notifierTxt.setText("Status");
                    fireDetectionBtn.setBackgroundColor(Color.parseColor("#5B5B5B"));
                    robot.setBackgroundColor(Color.parseColor("#3C3F41"));
                }

            }
        });

        smokeDetectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(smokeDetected.isPlaying()){
                    smokeDetected.stop();
                    notifierTxt.setText("Status");
                    smokeDetectionBtn.setBackgroundColor(Color.parseColor("#5B5B5B"));
                    robot.setBackgroundColor(Color.parseColor("#3C3F41"));
                }

            }
        });

        intruderDetectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(intruderDetected.isPlaying()){
                    intruderDetected.stop();
                    notifierTxt.setText("Status");
                    intruderDetectionBtn.setBackgroundColor(Color.parseColor("#5B5B5B"));
                    robot.setBackgroundColor(Color.parseColor("#3C3F41"));
                }

            }
        });


        gridView = (GridView) findViewById(R.id.gridview);
        prepareList();
        mAdapter = new GridviewAdapter(this,listDivision,listFlag);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    Intent iotIntent = new Intent(MainActivity.this,RoboloxIoTActivity.class);
                    startActivity(iotIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                }else if(position == 1){
                    Intent weatherStation = new Intent(MainActivity.this,RoboloxWeatherStationServerActivity.class);
                    weatherStation.putExtra("weather_station_url_key_00258",weatherStationUrl);
                    startActivity(weatherStation);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                }else if(position == 2){
//                    Intent chittagongIntent = new Intent(PanelActivity.this,ChittagongActivity.class);
//                    startActivity(chittagongIntent);
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                }else if(position == 3){
                    Intent defenseInfoIntent = new Intent(MainActivity.this,DefenseInfoListActivity.class);
                    startActivity(defenseInfoIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                }
            }
        });

        m_Runnable.run();
        new RoboloxWeatherServer().execute();

    }

    @Override
    public void onStart(){
        super.onStart();
        activity = this;
    }

    public void smsPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS},REQUEST_RECEIVE_SMS);
        }

    }


    public class RoboloxWeatherServer extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String out = null;
            JSONObject jObject;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                final HttpParams httpParameters = httpClient.getParams();

                HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
                HttpConnectionParams.setSoTimeout(httpParameters, 15000);

                HttpGet httpPost = new HttpGet("http://"+ip+"/data");

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                out = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return out;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            tempTxt.setText(result);
//            Log.e(TAG, result);

            try{

//                String tempIndexMesure = tempThresholdDisp.getText().toString();

                JSONObject object = new JSONObject(result);
                responseTemp = object.getString("temperature");
                responseHumid = object.getString("Humidity");
                tempTxt.setText(responseTemp+"*"+"C");
                humidTxt.setText(responseHumid+" "+"%");

//                Double max = Double.parseDouble(responseTemp);
//                if(max >= 20){
//                    weatherStatusTxt.setText("Cool");
//                }

                Double responseTempConvInt = Double.parseDouble(responseTemp);
                Double tempIndexConvInt = Double.parseDouble(tempThresholdDisp.getText().toString());


                if(responseTempConvInt >= tempIndexConvInt){
                    weatherStatusTxt.setText("Dangerous Weather Activity");
                    weatherStatusBar.setBackgroundColor(Color.parseColor("#ff002d"));
                    robot.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                    fireAlart.start();

                }else if(responseTempConvInt <= 22){
                    weatherStatusTxt.setText("Cool Weather");
                    weatherStatusBar.setBackgroundColor(Color.parseColor("#2e9e17"));

                }else if(responseTempConvInt <= 30){
                    weatherStatusTxt.setText("Moderate Cool Weather");
                    weatherStatusBar.setBackgroundColor(Color.parseColor("#184bba"));

                }else if(responseTempConvInt <= 40){
                    weatherStatusTxt.setText("Hot Weather");
                    weatherStatusBar.setBackgroundColor(Color.parseColor("#db3b58"));

                }else if (responseTempConvInt >= 41){
                    weatherStatusTxt.setText("Very Hot Weather");
                    weatherStatusBar.setBackgroundColor(Color.parseColor("#ed1a40"));

                }else if(responseTempConvInt <= tempIndexConvInt){
                    robot.setBackgroundColor(Color.parseColor("#3C3F41"));
                    fireAlart.stop();
                }


            }catch (Exception e){

            }
        }
    }

    public void ipAdderFunc(){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.ip_input_weather_server, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final String ipRegEx = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        final String decimalRegEx = "^\\d*\\.\\d+|\\d+\\.\\d*$";

        final EditText ipTxtField = (EditText) promptView.findViewById(R.id.ipInputWeatherTxt);
        final EditText tempMaxThreshHold = (EditText) promptView.findViewById(R.id.tempMaxThreshHold);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Pattern patternIp = Pattern.compile(ipRegEx);
                        Matcher matcherIp = patternIp.matcher(ipTxtField.getText().toString());

                        Pattern patternDec = Pattern.compile(decimalRegEx);
                        Matcher matcherDec = patternDec.matcher(tempMaxThreshHold.getText().toString());


                        if(matcherIp.find() && matcherDec.find()){

                            SharedPreferences sharedPref = getSharedPreferences("ipInfoWeather", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("ipaddressWeather",ipTxtField.getText().toString());
                            editor.putString("decInfo658",tempMaxThreshHold.getText().toString());
                            editor.apply();
                            tempThresholdDisp.setText(tempMaxThreshHold.getText().toString());

                        }else{
                            Toast.makeText(getApplicationContext(),"Wrong IP Addrsss & Temperature Thresh hold Value", Toast.LENGTH_SHORT).show();
                        }

//                        ipDisplay.setText(ipTxtField.getText());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            new RoboloxWeatherServer().execute();
            MainActivity.this.mHandler.postDelayed(m_Runnable,2000);
        }

    };

    public void prepareList()
    {
        Resources res = getResources();
        // String[]menu_grid = res.getStringArray(R.array.menu_grid);

        listDivision = new ArrayList<String> ();

        listDivision.add("IoT Automation");
        listDivision.add("Weather Station");
        listDivision.add("IP Camera");
        listDivision.add("Defense Info");


        listFlag = new ArrayList<Integer>();
        listFlag.add(R.drawable.iotlogo);
        listFlag.add(R.drawable.weather);
        listFlag.add(R.drawable.ipcam);
        listFlag.add(R.drawable.defense);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Double Tap in Back to exit !", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}

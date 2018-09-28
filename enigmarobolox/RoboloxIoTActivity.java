package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class RoboloxIoTActivity extends AppCompatActivity implements OnDataSendToActivity{

    //Speech Recognition Variable
    private final int SPEECH_RECOGNITION_CODE = 100;

    Handler mHandler;

    RelativeLayout statusBack, weatherStation, webserverAccess ;

    TextView statusTxt, ipDisplayTxt;

    Switch switchLightOne, switchLightTwo, switchFan ;

    ImageView ipAdder, settingIp, speechRecognizer, voiceInstructionList;

    //Weather Station URL
    private String weatherStationUrl = "file:///android_asset/index.html";
//    private String weatherStationUrl = "http://armapprise.com/weatherstation.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robolox_io_t);



        //Connection Status Background ;
        statusBack = (RelativeLayout) findViewById(R.id.statusBack);

        //Switch Button
        switchLightOne = (Switch) findViewById(R.id.switchLightOne);
        switchLightTwo = (Switch) findViewById(R.id.switchLightTwo);
        switchFan = (Switch) findViewById(R.id.switchFan);

        //Speech Recognizer
        speechRecognizer = (ImageView) findViewById(R.id.speechRecognizer);

        //Voice Instruction List
        voiceInstructionList = (ImageView) findViewById(R.id.voiceInstructionList);

        //Robolox Weather Station Button
        weatherStation = (RelativeLayout) findViewById(R.id.weatherStation);

        //Robolox IoT WebServer Access
        webserverAccess = (RelativeLayout)findViewById(R.id.webserverAccess);

        //Status
        statusTxt = (TextView) findViewById(R.id.statusTxt);

        //Activity Refresh
        this.mHandler = new Handler();
        m_Runnable.run();

        //IP Adder
        ipAdder = (ImageView) findViewById(R.id.ipAdder);

        //IP Getter
        ipDisplayTxt = (TextView) findViewById(R.id.ipDisplayTxt);

        //Setting IP
        settingIp = (ImageView) findViewById(R.id.settingIp);


        settingIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("ipInfo",Context.MODE_PRIVATE);
                String ip = sharedPref.getString("ipaddress","");
                ipDisplayTxt.setText(ip);
            }
        });

        ipAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAdderFunc();
            }
        });

        switchLightOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    lightOne();
                }else {
                    lightOne();
                }
            }
        });

        switchLightTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    lightTwo();
                }else {
                    lightTwo();
                }
            }
        });

        switchFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    fan();
                }else {
                    fan();
                }
            }
        });

        speechRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Speech Recognizer Function
                speechRecognizerDetector();
            }
        });

        //Robolox Weather Station Button
        weatherStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherStationIntent = new Intent(RoboloxIoTActivity.this, RoboloxWeatherStationServerActivity.class);
                weatherStationIntent.putExtra("weather_station_url_key_00258",weatherStationUrl);
                startActivity(weatherStationIntent);
            }
        });

        //Robolox WebServer Acces
        webserverAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String webserverIntentAddress = "http://" + ipDisplayTxt.getText();
                Intent webServerIntent = new Intent(RoboloxIoTActivity.this, RoboloxWeatherStationServerActivity.class);
                webServerIntent.putExtra("web_289", "http://"+ipDisplayTxt.getText().toString());
                startActivity(webServerIntent);
            }
        });

        voiceInstructionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceInstructionListIntent = new Intent(getApplicationContext(), VoiceInstructionListActivity.class);
                startActivity(voiceInstructionListIntent);
            }
        });

    }


    //Status Function
    private final Runnable m_Runnable = new Runnable()
    {
        public void run(){
            ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();

            for (NetworkInfo netInfo : networkInfo) {

                if (netInfo.getTypeName().equalsIgnoreCase("WIFI")){
                    if (netInfo.isConnected()) {
                        statusTxt.setText("Successfully Connected Robolox Server");
                    }else{
                        statusTxt.setText("Not Connected to the Robolox Server");
                    }
                }
            }
            RoboloxIoTActivity.this.mHandler.postDelayed(m_Runnable,20000);
        }
    };


    //IoT Automation Functions

    //Light One Function
    public void lightOne(){
        String url = "http://"+ipDisplayTxt.getText().toString()+"/";
        String url_rl = url+"room_light";
        SelectTask task = new SelectTask(url_rl);
        task.execute();
    }

    //Light Two Function
    public void lightTwo(){
        String url = "http://"+ipDisplayTxt.getText().toString()+"/";
        String url_rl = url+"mirror_light";
        SelectTask task = new SelectTask(url_rl);
        task.execute();
    }

    //Fan Function
    public  void fan(){
        String url = "http://"+ipDisplayTxt.getText().toString()+"/";
        String url_rl = url+"fan";
        SelectTask task = new SelectTask(url_rl);
        task.execute();
    }


    @Override
    public void sendData(String str) {
        updateButtonStatus(str);
    }


    //Function for updating Button Status
    private void updateButtonStatus(String jsonStrings){
        try {
            JSONObject json = new JSONObject(jsonStrings);

            String room_light = json.getString("rl");
            String mirror_light = json.getString("ml");
            String fan = json.getString("fan");

            if(room_light.equals("1")){
                switchLightOne.setChecked(true);
            }else{
                switchLightOne.setChecked(false);
            }
            if(mirror_light.equals("1")){
                switchLightTwo.setChecked(true);
            }else{
                switchLightTwo.setChecked(false);
            }
            if(fan.equals("1")){
                switchFan.setChecked(true);
            }else{
                switchFan.setChecked(false);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void ipAdderFunc(){
        LayoutInflater layoutInflater = LayoutInflater.from(RoboloxIoTActivity.this);
        View promptView = layoutInflater.inflate(R.layout.ip_input_field, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RoboloxIoTActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText ipTxtField = (EditText) promptView.findViewById(R.id.ipInputWeatherTxt);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = getSharedPreferences("ipInfo", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ipaddress",ipTxtField.getText().toString());
                        editor.apply();
                        ipDisplayTxt.setText(ipTxtField.getText());
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

    //Detect Speech Recognizer
    public void speechRecognizerDetector(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Give Command to the Robolox");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                 "Sorry this device not support voice recognition !",
                  Toast.LENGTH_SHORT).show();
        }
    }

    //Voice or Speech Recognition
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String recognizerString = result.get(0);
                    statusTxt.setText(recognizerString);

                    if(recognizerString.contains("room light on")){

                        lightOne();

                    }else if(recognizerString.contains("room light off")){

                        lightOne();

                    }else if(recognizerString.contains("garage light on")){

                        lightTwo();

                    }else if(recognizerString.contains("garage light off")){

                        lightTwo();

                    }else if(recognizerString.contains("fan on")){

                        fan();

                    }else if(recognizerString.contains("fan of")){

                        fan();

                    }
                }
                break;
            }
        }


    }
}

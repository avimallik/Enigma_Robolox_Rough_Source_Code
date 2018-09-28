package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Arm_AVI on 1/29/2018.
 */

public class ModuleSMSReciver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try{
            if(bundle != null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for(int i = 0; i < pdusObj.length; i++){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = sms.getDisplayOriginatingAddress();
                    String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();
                    String messageString = sms.getMessageBody();

                    String formattedText = String.format(context.getResources().getString(R.string.sms_message),sender,message);

                    if(phoneNumber.equals("+8801536110004")){
//                        MainActivity get = MainActivity.instance();
//                        MainActivity.fireAlart.reset();
//                        MainActivity.fireAlart = MediaPlayer.create(context,R.raw.firealeart);
//                        MainActivity.fireAlart.setLooping(true);
//                        MainActivity.fireAlart.start();
//                        vibx(context);
//                        get.smokeDetectionBtn.setBackgroundColor(Color.RED);
//                        get.notifierTxt.setText(messageString);

                        if(messageString.contains("fire detected")){
                            firealartSetting(context);
                            MainActivity getUIdata = MainActivity.instance();
                            getUIdata.notifierTxt.setText(messageString);
                            getUIdata.fireDetectionBtn.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                            getUIdata.robot.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                        }else if(messageString.contains("smoke detected")){
                            smokelartSetting(context);
                            MainActivity getUIdata = MainActivity.instance();
                            getUIdata.notifierTxt.setText(messageString);
                            getUIdata.smokeDetectionBtn.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                            getUIdata.robot.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                        }else if(messageString.contains("intruder detected")){
                            intruderalartSetting(context);
                            MainActivity getUIdata = MainActivity.instance();
                            getUIdata.notifierTxt.setText(messageString);
                            getUIdata.intruderDetectionBtn.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                            getUIdata.robot.setBackgroundColor(Color.parseColor("#FFFE1F01"));
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void vibx(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void firealartSetting(Context context){
        MainActivity get = MainActivity.instance();
        MainActivity.fireDetected.reset();
        MainActivity.fireDetected = MediaPlayer.create(context,R.raw.firedetected);
        MainActivity.fireDetected.setLooping(true);
        MainActivity.fireDetected.start();
    }

    public void smokelartSetting(Context context){
        MainActivity get = MainActivity.instance();
        MainActivity.smokeDetected.reset();
        MainActivity.smokeDetected = MediaPlayer.create(context,R.raw.smokedetected);
        MainActivity.smokeDetected.setLooping(true);
        MainActivity.smokeDetected.start();
    }

    public void intruderalartSetting(Context context){
        MainActivity get = MainActivity.instance();
        MainActivity.intruderDetected.reset();
        MainActivity.intruderDetected = MediaPlayer.create(context,R.raw.intruderdetected);
        MainActivity.intruderDetected.setLooping(true);
        MainActivity.intruderDetected.start();
    }

}

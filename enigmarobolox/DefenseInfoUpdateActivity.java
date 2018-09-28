package com.enigmarobolox.arm_avi.enigmarobolox;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DefenseInfoUpdateActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    EditText title, phone;
    Button editInfo , deleteInfo, callDefense ;

    DefenseInfoModel defenseInfoModel;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense_info_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        defenseInfoModel = (DefenseInfoModel) intent.getSerializableExtra("user");
        databaseHelper = new DatabaseHelper(this);

        checkAndRequestPermissions();

        //Title & Phone Number of Defense
        title = (EditText) findViewById(R.id.title);
        phone = (EditText) findViewById(R.id.phone);

        //Edit & Delete Button
        editInfo = (Button) findViewById(R.id.edit);
        deleteInfo = (Button) findViewById(R.id.delete);
        callDefense = (Button) findViewById(R.id.callDefense);

        title.setText(defenseInfoModel.getTitle());
        phone.setText(defenseInfoModel.getPhone());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDefenseInfoIntent = new Intent(getApplicationContext(), DefenseInfoInputActivity.class);
                startActivity(addDefenseInfoIntent);
            }
        });


        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateUser(defenseInfoModel.getId(),title.getText().toString(),phone.getText().toString());
                Toast.makeText(DefenseInfoUpdateActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DefenseInfoUpdateActivity.this,DefenseInfoListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        deleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteUSer(defenseInfoModel.getId());
                Toast.makeText(DefenseInfoUpdateActivity.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DefenseInfoUpdateActivity.this,DefenseInfoListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        callDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().matches(" ")){
                    Snackbar snackbar = Snackbar.make(callDefense,"Phone Number is Empty",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else {
                    String phoneNumber = phone.getText().toString();
                    callIntenStart("tel:"+phoneNumber);
                }
//              Toast.makeText(getApplicationContext(),phone.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void callIntenStart(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(number));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private  boolean checkAndRequestPermissions() {
        int phoneCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (phoneCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

}

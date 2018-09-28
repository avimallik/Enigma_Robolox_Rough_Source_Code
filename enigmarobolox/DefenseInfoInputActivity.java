package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefenseInfoInputActivity extends AppCompatActivity {

    EditText defenseTitle, defenseNumber;
    Button saveInfo ;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense_info_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        defenseNumber = (EditText) findViewById(R.id.defenseNumber);
        defenseTitle = (EditText) findViewById(R.id.title);
        saveInfo = (Button) findViewById(R.id.saveInfo);

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempPhoneNumber = defenseNumber.getText().toString();
                Pattern pattern = Pattern.compile("^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?");
                Matcher matcher = pattern.matcher(tempPhoneNumber );

                if(matcher.matches()){

                    databaseHelper.addDefenseDetail(defenseTitle.getText().toString(),defenseNumber.getText().toString());
                    defenseTitle.setText("");
                    defenseNumber.setText("");
                    Toast.makeText(DefenseInfoInputActivity.this, "Defense Info Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),DefenseInfoListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
//                  Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();

                }else{
                    Snackbar snackbar = Snackbar.make(saveInfo,"Phone Number is Wrong !",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });


    }


}

package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;

public class DefenseInfoListActivity extends AppCompatActivity {

    private ListView defenseList;
    private ArrayList<DefenseInfoModel> defenseInfoModelArrayList;
    private ListViewCustomAdapter listViewCustomAdapter;
    private DatabaseHelper databaseHelper;
    TextView counterCheckList ;
    EditText searchText ;
    CardView searchBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense_info_list);


        defenseList = (ListView) findViewById(R.id.defenseList);
        counterCheckList = (TextView) findViewById(R.id.counterCheckList);
        searchText = (EditText) findViewById(R.id.searchText);
        searchBar = (CardView) findViewById(R.id.searchBar);

        databaseHelper = new DatabaseHelper(this);
        defenseInfoModelArrayList = databaseHelper.getAllDefense();

        listViewCustomAdapter = new ListViewCustomAdapter(this,defenseInfoModelArrayList);
        defenseList.setAdapter(listViewCustomAdapter);
        defenseList.setTextFilterEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDefenseInfoIntent = new Intent(getApplicationContext(), DefenseInfoInputActivity.class);
                startActivity(addDefenseInfoIntent);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setVisibility(View.VISIBLE);
            }
        });

        if(defenseList.getAdapter().getCount() == 0){
            counterCheckList.setText("Defense Info List is Empty !");
            counterCheckList.setVisibility(View.VISIBLE);
        }else if(defenseList.getAdapter().getCount() > 0){
            counterCheckList.setVisibility(View.INVISIBLE);
        }

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                listViewCustomAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        defenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getApplicationContext(),DefenseInfoUpdateActivity.class);
               intent.putExtra("user",defenseInfoModelArrayList.get(position));
               startActivity(intent);
            }
        });

    }


}

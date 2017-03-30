package com.example.nothi.androidamenities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "OptionsActivity";

    private Button findPrinterB;
    private Button findMwashroonB;
    private Button findFwashroonB;
    private Button findMicroB;
    private Button findVendingB;
    private EditText coordInput;

    //store and edit information
    SharedPreferences sharedInfo;
    //first para, name of preference's file
    SharedPreferences.Editor editallInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findPrinterB=(Button)findViewById(R.id.printerbutton);
        findMwashroonB=(Button)findViewById(R.id.malewashroombutton);
        findFwashroonB=(Button)findViewById(R.id.femalewashroombutton);;
        findMicroB=(Button)findViewById(R.id.mircobutton);
        findVendingB=(Button)findViewById(R.id.vendingbutton);
        coordInput=(EditText) findViewById(R.id.coordinateInput);


        sharedInfo= getSharedPreferences("userInfoKey1", Context.MODE_PRIVATE);//only want this app to access info
        //first para, name of preference's file
        editallInfo= sharedInfo.edit();//edit into Info all the info

        findPrinterB.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //need to overrid abstract.
                        //create obj intent class
                        saveInfo(v);
                        //displayInfo(v);

                        String dfCoordInput= (String)  coordInput.getText().toString();

                        Log.i("Coordinate input is ", dfCoordInput);

                        Intent findnearPrinter= new Intent("com.example.nothi.androidamenities.NearPrinterActivity");
                        //put activity package route
                        //now let's start
                        findnearPrinter.putExtra("Coordkey", dfCoordInput);
                        startActivity(findnearPrinter);//done



                    }
                }

        );
        // findMicroB listener added by Scott to launch NearMicrowave.class
        findMicroB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findNearMicro = new Intent(MainActivity.this, NearMicrowave.class);
                startActivity(findNearMicro);
            }
        });

        findVendingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findNearVending = new Intent(MainActivity.this, NearVendingActivity.class);
                startActivity(findNearVending);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "in onResume()" );
    }
    @Override
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "in onStart()" );
        super.onStart();
    }
    @Override
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "in onPause()" );
        super.onPause();
    }
    @Override
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "in onStop()" );
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "in onDestroy()" );
        super.onDestroy();
    }
    public void saveInfo(View view){
        //takes view to get infoz

        editallInfo.putString("Coordinatekey", coordInput.getText().toString() );//from coordinatkey
        //passes same coordinate to all activities

        editallInfo.commit();//this edits all info
        Toast.makeText(this, "commit successful, finding nearest amenity!", Toast.LENGTH_LONG).show();

    }
}
package com.example.nothi.androidamenities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "OptionsActivity";
    private EditText coordInput;

    //store and edit information
    SharedPreferences sharedInfo;
    //first para, name of preference's file
    SharedPreferences.Editor editallInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("FtM?");

//        coordInput = (EditText) findViewById(R.id.coordinateInput);

        sharedInfo = getSharedPreferences("userInfoKey1", Context.MODE_PRIVATE);
        //first para, name of preference's file
        editallInfo = sharedInfo.edit();//edit into Info all the info
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_one:
                startActivity(new Intent(MainActivity.this, NearMicrowave.class));
                break;
            case R.id.action_two:
                startActivity(new Intent(MainActivity.this, NearVendingActivity.class));
                break;
            case R.id.action_three:
//                String dfCoordInput= (String)  coordInput.getText().toString();
//
//                Log.i("Coordinate input is ", dfCoordInput);
//
//                Intent findnearPrinter = new Intent(MainActivity.this, NearPrinterActivity.class);
//                findnearPrinter.putExtra("Coordkey", dfCoordInput);
                startActivity(new Intent(MainActivity.this, NearPrinterActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "in onResume()");
    }

    @Override
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "in onStart()");
        super.onStart();
    }

    @Override
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "in onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "in onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "in onDestroy()");
        super.onDestroy();
    }

    public void saveInfo(View view){
        editallInfo.putString("Coordinatekey", coordInput.getText().toString());//from coordinatkey
        //passes same coordinate to all activities

        editallInfo.commit();//this edits all info
        Toast.makeText(this, "commit successful, finding nearest amenity!", Toast.LENGTH_LONG).show();
    }
}
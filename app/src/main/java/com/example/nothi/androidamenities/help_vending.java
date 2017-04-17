package com.example.nothi.androidamenities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class help_vending extends AppCompatActivity {
    protected final String ACTIVITY_NAME = "Help_Vending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_vending);

        getSupportActionBar().setTitle("Help & About");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu_overflow, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_one: startActivity(new Intent(this, MainActivity.class)); break;
            case R.id.action_two: startActivity(new Intent(this, NearMicrowave.class)); break;
            case R.id.action_three: startActivity(new Intent(this, NearPrinterActivity.class)); break;
            case R.id.action_four: startActivity(new Intent(this, MainActivity.class)); break;
        }
        return true;
    }

    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    protected void onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    protected void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}
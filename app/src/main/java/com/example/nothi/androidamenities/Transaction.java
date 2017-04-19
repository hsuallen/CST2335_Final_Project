package com.example.nothi.androidamenities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Transaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("fragType");
        Fragment fragment = null;
        switch(type) {
            case "building":
                getSupportActionBar().setTitle("List of Floors");
                fragment = new BuildingFragment();
                break;
            case "floor":
                getSupportActionBar().setTitle("List of Vending Machines");
                fragment = new FloorFragment();
                break;
        }

        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).commit();
    }
}
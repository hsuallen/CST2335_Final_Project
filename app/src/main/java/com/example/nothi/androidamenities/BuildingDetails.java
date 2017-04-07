package com.example.nothi.androidamenities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BuildingDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        BuildingFragment fragment = new BuildingFragment();
        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).commit();
    }
}
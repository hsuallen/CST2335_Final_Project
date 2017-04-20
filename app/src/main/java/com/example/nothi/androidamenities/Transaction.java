package com.example.nothi.androidamenities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * Transaction
 *
 * The transaction class is for the transition between an activity and displaying a fragment. It is
 *   called "transaction" mainly because it loads more than just a single fragment in this class
 *   depending on the fragment type being passed in through the intent.
 *
 * @author Allen Hsu
 *
 */
public class Transaction extends AppCompatActivity {

    /**
     * The onCreate function for an activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        // creates a new bundle and gets the extra information from the intent passed in
        Bundle bundle = getIntent().getExtras();
        // gets the fragment type in String
        String type = bundle.getString("fragType");
        // instantiate a null fragment
        Fragment fragment = null;

        // checks the fragment type to see which fragment to set to
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

        // set the fragment arguments to bundle in order to carry the extras from the intent over
        fragment.setArguments(bundle);
        // begins the fragment transaction and loads the fragment
        getFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).commit();
    }
}
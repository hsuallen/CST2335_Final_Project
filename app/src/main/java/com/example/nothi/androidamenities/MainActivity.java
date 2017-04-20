package com.example.nothi.androidamenities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * MainActivity
 *
 * This is the main activity of the entire app, in other words the main menu.
 *
 * @author Allen Hsu
 *
 */
public class MainActivity extends AppCompatActivity {

    /******************* START OF CLASS VARIABLES *******************/

    protected static final String ACTIVITY_NAME = "OptionsActivity";
    private ArrayList<ActivityRow> activities = new ArrayList<>();
    private EditText coordInput;

    // activities and icons associated with them
    private final String[] titles = new String[]{"Microwaves", "Vending Machines", "Printers",
            "Washrooms"};
    private final Integer[] icons = new Integer[]{R.drawable.microwave_colour,
            R.drawable.vending_machine_colour, R.drawable.printer_colour, R.drawable.toilet_colour};
    private final Class destinations[] = new Class[]{NearMicrowave.class, NearVendingActivity.class,
            NearPrinterActivity.class, MainActivity.class};

    //store and edit information
    SharedPreferences sharedInfo;
    //first para, name of preference's file
    SharedPreferences.Editor editallInfo;

    /******************** END OF CLASS VARIABLES ********************/

    /**
     * This is the custom ArrayAdapter for the ListView which contains various amenities in the
     *   main menu.
     */
    protected class ActivitiesAdapter extends ArrayAdapter<ActivityRow> {
        public ActivitiesAdapter(Context ctx, int resource, List<ActivityRow> objects) {
            super(ctx, resource, objects);
        }

        public View getView(int pos, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();

            int layout = R.layout.activities_row;
            View result = inflater.inflate(layout, null);

            TextView title = (TextView)result.findViewById(R.id.textView15);
            ImageView icon = (ImageView)result.findViewById(R.id.imageView);
            title.setText(getItem(pos).getTitle());
            icon.setImageResource(getItem(pos).getIcon());

            return result;
        }
    }

    protected class ActivityRow {
        private int iconID;
        private String title;

        public ActivityRow(int iconID, String title) {
            this.iconID = iconID;
            this.title = title;
        }

        public int getIcon() { return iconID; }
        public String getTitle() {return title; }
    }

    /**
     * The onCreate function for an activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Where's the Microwave?");

        for (int i = 0; i < titles.length; i++) activities.add(new ActivityRow(icons[i], titles[i]));
        ListView listView = (ListView)findViewById(R.id.listView4);
        ActivitiesAdapter adapter = new ActivitiesAdapter(this, R.layout.activities_row, activities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, destinations[position]);
                startActivity(intent);
            }
        });


//        coordInput = (EditText) findViewById(R.id.coordinateInput);

        sharedInfo = getSharedPreferences("userInfoKey1", Context.MODE_PRIVATE);
        //first para, name of preference's file
        editallInfo = sharedInfo.edit();//edit into Info all the info
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_zero:
                startActivity(new Intent(MainActivity.this, Settings.class));
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
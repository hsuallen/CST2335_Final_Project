package com.example.nothi.androidamenities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NearVendingActivity extends AppCompatActivity {
    public static final int DELETE_BUILDING = 5;
    protected final String ACTIVITY_NAME = "NearVendingActivity";
    protected final String info = "Allen's Activity v4.7";

    protected ArrayList<Building> buildings = new ArrayList<>();
    protected boolean isTablet = false;
    protected BuildingAdapter adapter;
    protected Cursor cursor;
    protected int pos;
    protected SQLiteDatabase db;

    protected class BuildingAdapter extends ArrayAdapter<Building> {
        public BuildingAdapter(Context ctx) { super(ctx, 0); }

        public int getCount() { return buildings.size(); }
        public Building getItem(int pos) { return buildings.get(pos); }
        public View getView(int pos, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = NearVendingActivity.this.getLayoutInflater();

            int layout = R.layout.building_row;
            View result = inflater.inflate(layout, null);

            TextView classroom = (TextView)result.findViewById(R.id.textView2);
            TextView description = (TextView)result.findViewById(R.id.textView3);
            classroom.setText(getItem(pos).getBuilding());
            description.setText(getItem(pos).getDescription());
            return result;
        }

        public long getItemId(int pos) {
            cursor.moveToPosition(pos);
            long id = cursor.getLong(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_ID));
            return id;
        }
    }

    private class Building {
        private String building, description;
        private int floors;

        public Building(String classroom, String description, int floors) {
            this.building = classroom;
            this.description = description;
            this.floors = floors;
        }

        public String getBuilding() { return this.building; }
        public String getDescription() { return this.description; }
        public int getFloors() { return this.floors; }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        long id;
        if (resultCode == DELETE_BUILDING) {
            id = data.getLongExtra("ID", 0);
            deleteBuilding(id);
            View root = findViewById(R.id.parent_root);
            genericFunctions.createSnackbar(root, "Building deleted.", Snackbar.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_vending);

        getSupportActionBar().setTitle("List of Buildings");

        BuildingDatabaseHelper c = new BuildingDatabaseHelper(this);
        db = c.getWritableDatabase();

        try {
            updateCursor();
            int columns = cursor.getColumnCount();

            while (!cursor.isAfterLast()) {
                String building = cursor.getString(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_BUILDING));
                String desc = cursor.getString(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_DESCRIPTION));
                int nFloors = cursor.getInt(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_FLOORS));
                buildings.add(new Building(building, desc, nFloors));
                cursor.moveToNext();
            }

            Log.i(ACTIVITY_NAME, "Cursor's column count = " + columns);
            for (int i = 0; i < columns; i++) {
                Log.i(ACTIVITY_NAME, "Column " + i + "'s name is: " + cursor.getColumnName(i));
            }

            Button add = (Button)findViewById(R.id.button3);
            ListView list = (ListView)findViewById(R.id.listview);

            adapter = new BuildingAdapter(this);
            list.setAdapter(adapter);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog();
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    String building = cursor.getString(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_BUILDING));
                    String desc = cursor.getString(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_DESCRIPTION));
                    int floors = cursor.getInt(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_FLOORS));

                    pos = position;

                    if (isTablet) {
                        BuildingFragment fragment = new BuildingFragment();
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    } else {
                        Intent intent = new Intent(NearVendingActivity.this, Transaction.class);
                        intent.putExtra("ID", id);
                        intent.putExtra("building", building);
                        intent.putExtra("description", desc);
                        intent.putExtra("floors", floors);
                        intent.putExtra("isTablet", isTablet);
                        intent.putExtra("fragType", "building");
                        startActivityForResult(intent, 5, bundle);
                    }
                }
            });
        } catch (Exception e) { Log.e("Exception: ", e.getMessage()); }

        isTablet = (findViewById(R.id.frameLayout) != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu_overflow_help, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_zero:
                View root = findViewById(R.id.parent_root);
                Snackbar.make(root, info, Snackbar.LENGTH_LONG).setAction("Help", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NearVendingActivity.this, help_vending.class);
                        startActivity(intent);
                    }
                }).show();
                break;
            case R.id.action_one: startActivity(new Intent(this, MainActivity.class)); break;
            case R.id.action_two: startActivity(new Intent(this, NearMicrowave.class)); break;
            case R.id.action_three: startActivity(new Intent(this, NearPrinterActivity.class)); break;
            case R.id.action_four: startActivity(new Intent(this, MainActivity.class)); break;
        }
        return true;
    }

    public void deleteBuilding(long id) {
        db.delete(BuildingDatabaseHelper.TABLE_BUILDINGS, BuildingDatabaseHelper.KEY_ID + "=" + id, null);
        adapter.remove(adapter.getItem(pos));
        buildings.remove(pos);
        adapter.notifyDataSetChanged();
    }

    private void addBuilding(String room, String desc, int nFloors) {
        ContentValues cv = new ContentValues();

        buildings.add(new Building(room, desc, nFloors));
        cv.put(BuildingDatabaseHelper.KEY_BUILDING, room);
        cv.put(BuildingDatabaseHelper.KEY_DESCRIPTION, desc);
        cv.put(BuildingDatabaseHelper.KEY_FLOORS, nFloors);
        db.insert(BuildingDatabaseHelper.TABLE_BUILDINGS, "Null replacement value", cv);
        updateCursor();
        adapter.notifyDataSetChanged();
    }

    private void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialog = inflater.inflate(R.layout.allen_building_input, null);

        builder.setView(dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog d = (Dialog)dialog;
                EditText room = (EditText)d.findViewById(R.id.editText1);
                EditText desc = (EditText)d.findViewById(R.id.editText2);
                EditText floors = (EditText)d.findViewById(R.id.editText);
                int nFloors = Integer.parseInt(floors.getText().toString());
                addBuilding(room.getText().toString(), desc.getText().toString(), nFloors);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    private void updateCursor() {
        cursor = db.query(BuildingDatabaseHelper.TABLE_BUILDINGS, new String[]{BuildingDatabaseHelper.KEY_BUILDING,
                        BuildingDatabaseHelper.KEY_DESCRIPTION, BuildingDatabaseHelper.KEY_FLOORS,
                        BuildingDatabaseHelper.KEY_ID},null, null, null, null, null);
        cursor.moveToFirst();
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
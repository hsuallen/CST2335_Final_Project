package com.example.nothi.androidamenities;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by allenhsu on 2017-04-07.
 */

public class FloorFragment extends Fragment {
    protected ArrayList<String> vendingMachines = new ArrayList<>();
    protected boolean emptyList;
    protected FloorAdapter adapter;
    protected Cursor cursor;
    protected int pos, floorNum;
    protected SQLiteDatabase db;
    protected String building, noVendingMsg;

    protected class FloorAdapter extends ArrayAdapter<String> {
        public FloorAdapter(Context ctx) { super(ctx, 0); }

        public int getCount() { return vendingMachines.size(); }
        public String getItem(int pos) { return vendingMachines.get(pos); }
        public View getView(int pos, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            int layout = R.layout.floor_row;
            View result = inflater.inflate(layout, null);

            TextView description = (TextView)result.findViewById(R.id.textView12);
            description.setText(getItem(pos));
            return result;
        }

        public long getItemId(int pos) {
            if (!emptyList) {
                cursor.moveToPosition(pos);
                long id = cursor.getLong(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_ID));
                return id;
            }
            return 0;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle bun = getArguments();

        building = bun.getString("building");
        floorNum = bun.getInt("floor");

        noVendingMsg = "No vending machines are found so far on floor " + floorNum + " of " + building;

        BuildingDatabaseHelper c = new BuildingDatabaseHelper(getActivity());
        db = c.getWritableDatabase();

        try {
            updateCursor();

            while (!cursor.isAfterLast()) {
                String desc = cursor.getString(cursor.getColumnIndex(BuildingDatabaseHelper.KEY_DESCRIPTION));
                vendingMachines.add(desc);
                cursor.moveToNext();
            }
            if (vendingMachines.size() == 0) {
                vendingMachines.add(noVendingMsg);
            }
        } catch (Exception e) { Log.e("Exception: ", e.getMessage()); }

        emptyList = (vendingMachines.get(0) == noVendingMsg) ? true : false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gui = inflater.inflate(R.layout.floor_details_layout, null);

        Button addVending = (Button)gui.findViewById(R.id.button2);
        addVending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });

        adapter = new FloorAdapter(getActivity());

        ListView listView = (ListView)gui.findViewById(R.id.listView3);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long itemID) {
                pos = position;
                Log.i("Test", "Result: " + (vendingMachines.get(0) == noVendingMsg));
                if (vendingMachines.get(0) == noVendingMsg) {
                    Snackbar.make(view, "No vending machines to delete!", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else deleteDialog(itemID);
                return false;
            }
        });

        return gui;
    }

    private void addVendingMachine(String buildingFloor, String desc) {
        ContentValues cv = new ContentValues();

        if (emptyList) {
            vendingMachines.remove(0);
            emptyList = false;
        }

        vendingMachines.add(desc);
        cv.put(BuildingDatabaseHelper.KEY_BUILDING_FLOOR, buildingFloor);
        cv.put(BuildingDatabaseHelper.KEY_DESCRIPTION, desc);
        db.insert(BuildingDatabaseHelper.TABLE_VENDING, "Null replacement value", cv);
        updateCursor();
        adapter.notifyDataSetChanged();
    }

    private void deleteVendingMachine(long id) {
        db.delete(BuildingDatabaseHelper.TABLE_VENDING, BuildingDatabaseHelper.KEY_ID + "=" + id, null);
        adapter.remove(adapter.getItem(pos));
        vendingMachines.remove(pos);

        if (vendingMachines.size() == 0) {
            vendingMachines.add(noVendingMsg);
            emptyList = true;
        }

        adapter.notifyDataSetChanged();
    }

    private void addDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.allen_vending_input, null);

        builder.setView(dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog d = (Dialog)dialog;

                EditText description = (EditText)d.findViewById(R.id.editText10);
                addVendingMachine(building + "_" + floorNum, description.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    private void deleteDialog(final long itemID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_vending);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteVendingMachine(itemID);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateCursor() {
        cursor = db.query(BuildingDatabaseHelper.TABLE_VENDING, new String[]{BuildingDatabaseHelper.KEY_BUILDING_FLOOR,
                BuildingDatabaseHelper.KEY_DESCRIPTION, BuildingDatabaseHelper.KEY_ID},
                BuildingDatabaseHelper.KEY_BUILDING_FLOOR + " LIKE \"" + building + "_" + floorNum + "%\"",
                null, null, null, null);
        cursor.moveToFirst();
    }
}
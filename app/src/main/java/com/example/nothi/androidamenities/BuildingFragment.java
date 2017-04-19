package com.example.nothi.androidamenities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by allenhsu on 2017-04-04.
 */

public class BuildingFragment extends Fragment {
    protected boolean isTablet;
    protected int nFloors;
    protected long id;
    protected ProgressBar progressBar;
    protected String buildingStr, descriptionStr, url;
    protected TextView classroomTV, descriptionTV;
    protected View gui;
    private HashMap<String, Integer> buildingFloors;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle bun = getArguments();

        buildingFloors = new HashMap<>();

        id = bun.getLong("ID");
        buildingStr = bun.getString("building");
        descriptionStr = bun.getString("description");
        nFloors = bun.getInt("floors");
        isTablet = bun.getBoolean("isTablet");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gui = inflater.inflate(R.layout.building_details_layout, null);

        classroomTV = (TextView)gui.findViewById(R.id.textView9);
        classroomTV.setText(buildingStr);

        descriptionTV = (TextView)gui.findViewById(R.id.textView10);
        descriptionTV.setText(String.valueOf(descriptionStr));

        Log.i("TEST", "floors: " + nFloors);
        ArrayList<String> floors = new ArrayList<>();
        for (int i = 0; i < nFloors; i++) floors.add("Floor " + (i + 1));

        ListView list = (ListView)gui.findViewById(R.id.listView2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(gui.getContext(), R.layout.floor_row, floors);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isTablet) {
                } else {
                    Intent intent = new Intent(getActivity(), Transaction.class);
                    intent.putExtra("fragType", "floor");
                    intent.putExtra("building", buildingStr);
                    intent.putExtra("floor", position+1);

                    startActivityForResult(intent, 5);
                }
            }
        });

        Button delete = (Button)gui.findViewById(R.id.button6);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTablet) {
//                    ((NearVendingActivity)getActivity()).deleteMessage(id);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ID", id);
                    getActivity().setResult(NearVendingActivity.DELETE_BUILDING, intent);
                    if (!isTablet) getActivity().finish();
                }
            }
        });

        return gui;
    }
}
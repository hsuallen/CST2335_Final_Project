package com.example.nothi.androidamenities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by allenhsu on 2017-04-07.
 */

public class FloorFragment extends Fragment {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle bun = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gui = inflater.inflate(R.layout.floor_details_layout, null);

        ArrayList<String> fakeList = new ArrayList<>();
        fakeList.add("Several vending machines can be found across T119.");
        fakeList.add("3 vending machines can be found at the entrance beside T130.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(gui.getContext(),
                R.layout.floor_row, fakeList);

        ListView listView = (ListView)gui.findViewById(R.id.listView3);
        listView.setAdapter(adapter);

        return gui;
    }
}

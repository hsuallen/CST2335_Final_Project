package com.example.nothi.androidamenities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class NearestinCommons extends AppCompatActivity {
    private ListView printerListViewwc;

    ArrayList<String> printers;

    private Button wcfloorone;
    private Button wcfloortwo;
    private Button wcfloorthree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearestin_commons);

        wcfloorone=(Button)findViewById(R.id.wc1);
        wcfloortwo=(Button)findViewById(R.id.wc2);
        wcfloorthree=(Button)findViewById(R.id.wc3);

        printerListViewwc = (ListView) findViewById(R.id.printerlistview);

        printers =new ArrayList<>( Arrays.asList("WC110","WC112","WC138","WC148"));

        printerListViewwc.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, printers));
        printerListViewwc.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(getApplicationContext(), printers.get(position), Toast.LENGTH_SHORT).show();
            }

        });

    }


}

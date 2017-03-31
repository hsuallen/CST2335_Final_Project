package com.example.nothi.androidamenities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearestinCommons extends AppCompatActivity {
    private ListView printerListViewwc;

    ArrayList<String> printers;

    ArrayAdapter<String> adapter;
    private Button wcfloorone;
    private Button wcfloortwo;
    private Button wcfloorthree;
    private TextView status;
    private Button addprinter;
    private Button removeprinter;

    private int positiontracker=0;//tracks postion to remove from list.

    private EditText printerin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearestin_commons);
        status=(TextView) findViewById(R.id.status);
        printerListViewwc= (ListView) findViewById(R.id.printerlistview);

        wcfloorone=(Button)findViewById(R.id.wc1);
        wcfloortwo=(Button)findViewById(R.id.wc2);
        wcfloorthree=(Button)findViewById(R.id.wc3);

        addprinter=(Button)findViewById(R.id.addPrbt);
        removeprinter=(Button)findViewById(R.id.removePrbt);

        final Map<String, String > printerValueMap=new HashMap<>();
        printerValueMap.put("C110", "Black and White only 500 pages queue: 20");
        printerValueMap.put("C112", "Black and White only 255 pages queue: 0");
        printerValueMap.put("C138", "Colour 58 pages, queue: 10");
        printerValueMap.put("C148", "Colour 670 pages, queue: 6");
        printerValueMap.put("C222", "Colour 70 pages, queue: 7");
        printerValueMap.put("C215", "Colour 6 pages, queue: 0");
        printerValueMap.put("C201", "Colour 0 pages, queue: 14");
        printerValueMap.put("C314", "Black and White only 570 pages, queue: 0");
        printerValueMap.put("C322", "Colour 1246 pages, queue: 0");
        printerValueMap.put("C308", "Colour 518 pages, queue: 4");

        printers = new ArrayList<>(Arrays.asList("C110","C112","C138","C148"));
        adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, printers);
        printerListViewwc.setAdapter(adapter);//we do this to be able to notify change.

        printerListViewwc.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                status.setText(printerValueMap.get(printers.get(position)));
                positiontracker=printers.indexOf(printers.get(position));
            }
        });

        wcfloorone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();

                printers.add("C110");
                printers.add("C112");
                printers.add("C138");
                printers.add("C148");

                adapter.notifyDataSetChanged();
            }
        });

        wcfloortwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();

                printers.add("C222");
                printers.add("C215");
                printers.add("C201");

                adapter.notifyDataSetChanged();
            }
        });

        wcfloorthree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();
                printers.add("C322");
                printers.add("C314");
                printers.add("C308");

                adapter.notifyDataSetChanged();
            }
        });

        addprinter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {printerin=(EditText)findViewById(R.id.newprinterinput);
                String messagePassed=printerin.getText().toString();

                List<String> items = Arrays.asList(messagePassed.split("\\s*,\\s*"));

                printers.add(items.get(0));
                printerValueMap.put(items.get(0), items.get(1));

                adapter.notifyDataSetChanged();
            }
        });

        removeprinter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {printerin=(EditText)findViewById(R.id.newprinterinput);
                printers.remove(positiontracker);

                adapter.notifyDataSetChanged();
            }
        });
    }
}
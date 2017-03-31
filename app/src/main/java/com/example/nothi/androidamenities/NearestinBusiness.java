package com.example.nothi.androidamenities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearestinBusiness extends AppCompatActivity {

    private ListView printerListViewwb;

    ArrayList<String> printers;

    ArrayAdapter<String> adapter;
    private Button wbfloorone;
    private Button wbfloortwo;
    private Button wbfloorthree;
    private TextView statusb;
    private Button addprinter;
    private Button removeprinter;

    private int positiontracker=0;//tracks postion to remove from list.

    private EditText printerin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearestin_business);
        statusb=(TextView) findViewById(R.id.statusb);
        printerListViewwb= (ListView) findViewById(R.id.printerlistviewb);

        wbfloorone=(Button)findViewById(R.id.wb1);
        wbfloortwo=(Button)findViewById(R.id.wb2);
        wbfloorthree=(Button)findViewById(R.id.wb3);

        addprinter=(Button)findViewById(R.id.addPrbtb);
        removeprinter=(Button)findViewById(R.id.removePrbtb);

        final Map<String, String > printerValueMap=new HashMap<>();
        printerValueMap.put("B110", "Black and White only 500 pages queue: 20");
        printerValueMap.put("B112", "Black and White only 255 pages queue: 0");
        printerValueMap.put("B138", "Colour 58 pages, queue: 10");
        printerValueMap.put("B148", "Colour 670 pages, queue: 6");
        printerValueMap.put("B222", "Colour 70 pages, queue: 7");
        printerValueMap.put("B215", "Colour 6 pages, queue: 0");
        printerValueMap.put("B201", "Colour 0 pages, queue: 14");
        printerValueMap.put("B314", "Black and White only 570 pages, queue: 0");
        printerValueMap.put("B322", "Colour 1246 pages, queue: 0");
        printerValueMap.put("B308", "Colour 518 pages, queue: 4");

        printers =new ArrayList<>( Arrays.asList("B110","B112","B138","B148"));

        adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, printers);

        printerListViewwb.setAdapter(adapter);//we do this to be able to notify change.

        printerListViewwb.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                statusb.setText(printerValueMap.get(printers.get(position)));
                positiontracker=printers.indexOf(printers.get(position));
            }

        });

        wbfloorone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();

                printers.add("B110");
                printers.add("B112");
                printers.add("B138");
                printers.add("B148");

                adapter.notifyDataSetChanged();
            }
        });

        wbfloortwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();
                printers.add("B222");
                printers.add("B215");
                printers.add("B201");

                adapter.notifyDataSetChanged();
            }
        });

        wbfloorthree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printers.clear();
                printers.add("B322");
                printers.add("B314");
                printers.add("B308");

                adapter.notifyDataSetChanged();
            }
        });

        addprinter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {printerin=(EditText)findViewById(R.id.newprinterinputb);
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
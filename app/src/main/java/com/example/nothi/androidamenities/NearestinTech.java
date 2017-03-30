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

public class NearestinTech extends AppCompatActivity {
    private ListView printerListViewwt;

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
        setContentView(R.layout.activity_nearestin_tech);
        status=(TextView) findViewById(R.id.status);
        printerListViewwt = (ListView) findViewById(R.id.printerlistviewt);

        wcfloorone=(Button)findViewById(R.id.wc1);
        wcfloortwo=(Button)findViewById(R.id.wc2);
        wcfloorthree=(Button)findViewById(R.id.wc3);

        addprinter=(Button)findViewById(R.id.addPrbt);
        removeprinter=(Button)findViewById(R.id.removePrbt);



        final Map<String, String > printerValueMap=new HashMap<>();
        printerValueMap.put("T110", "Black and White only 500 pages queue: 20");
        printerValueMap.put("T112", "Black and White only 255 pages queue: 0");
        printerValueMap.put("T138", "Colour 58 pages, queue: 10");
        printerValueMap.put("T148", "Colour 670 pages, queue: 6");
        /////////////////
        printerValueMap.put("T222", "Colour 70 pages, queue: 7");
        printerValueMap.put("T215", "Colour 6 pages, queue: 0");
        printerValueMap.put("T201", "Colour 0 pages, queue: 14");
        /////
        printerValueMap.put("T314", "Black and White only 570 pages, queue: 0");
        printerValueMap.put("T322", "Colour 1246 pages, queue: 0");
        printerValueMap.put("T308", "Colour 518 pages, queue: 4");





        printers =new ArrayList<>( Arrays.asList("T110","T112","T138","T148"));

        adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, printers);

        printerListViewwt.setAdapter(adapter);//we do this to be able to notify change.

        printerListViewwt.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){

                status.setText(printerValueMap.get(printers.get(position)));
                positiontracker=printers.indexOf(printers.get(position));

                //Toast.makeText(getApplicationContext(), printers.get(position), Toast.LENGTH_SHORT).show();
            }

        });
        wcfloorone.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        printers.clear();

                        printers.add("T110");
                        printers.add("T112");
                        printers.add("T138");
                        printers.add("T148");



                        adapter.notifyDataSetChanged();




                    }
                }

        );
        wcfloortwo.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        printers.clear();

                        printers.add("T222");
                        printers.add("T215");
                        printers.add("T201");

                        adapter.notifyDataSetChanged();




                    }
                }

        );
        wcfloorthree.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        printers.clear();
                        printers.add("T322");
                        printers.add("T314");
                        printers.add("T308");

                        adapter.notifyDataSetChanged();



                    }
                }

        );

        addprinter.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {printerin=(EditText)findViewById(R.id.newprinterinput);
                        String messagePassed=printerin.getText().toString();

                        List<String> items = Arrays.asList(messagePassed.split("\\s*,\\s*"));

                        printers.add(items.get(0));
                        printerValueMap.put(items.get(0), items.get(1));

                        adapter.notifyDataSetChanged();



                    }
                }

        );

        removeprinter.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {printerin=(EditText)findViewById(R.id.newprinterinput);
                        printers.remove(positiontracker);

                        adapter.notifyDataSetChanged();



                    }
                }

        );

    }


}

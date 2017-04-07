package com.example.nothi.androidamenities;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by allenhsu on 2017-04-04.
 */

public class BuildingFragment extends Fragment {
    protected boolean isTablet;
    protected int resultCode = 0;
    protected long id;
    protected ProgressBar progressBar;
    protected String classroomStr, descriptionStr, url;
    protected TextView classroomTV, descriptionTV;
    protected View gui;
    private int nFloor;
    private Query q;

    class Query extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String ... args) {
            InputStream in = null;
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                in = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) continue;

                    String name = parser.getName();
                    if (name.equals("floors")) {
                        nFloor = Integer.parseInt(parser.getAttributeValue(null, "number_of_floors"));
                    }
                }
            } catch (XmlPullParserException p) {
            } catch (IOException e ) {
            }

            return null;
        }

        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);

            ArrayList<String> floors = new ArrayList<>();
            for (int i = 0; i < nFloor; i++) floors.add("Floor " + (i + 1));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(gui.getContext(),
                    R.layout.floor_row, floors);

            ListView listView = (ListView)gui.findViewById(R.id.listView2);
            listView.setAdapter(adapter);
        }

        protected void onProgressUpdate(Integer ...value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle bun = getArguments();

        id = bun.getLong("ID");
        classroomStr = bun.getString("building");
        descriptionStr = bun.getString("description");
        isTablet = bun.getBoolean("isTablet");

        url = "http://algonquinstudents.ca/~hsu00016/android/B_school_of_business.xml";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gui = inflater.inflate(R.layout.classroom_details_layout, null);

        progressBar = (ProgressBar)gui.findViewById(R.id.progressBar);
        q = new Query();
        q.execute(url);

//        String lst[] = new String[]{"test", "haha", "James"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(gui.getContext(),
//                R.layout.floor_row, lst);
//        ListView lv = (ListView)gui.findViewById(R.id.listView2);
//        lv.setAdapter(adapter);

        classroomTV = (TextView)gui.findViewById(R.id.textView9);
        classroomTV.setText(classroomStr);

        descriptionTV = (TextView)gui.findViewById(R.id.textView10);
        descriptionTV.setText(String.valueOf(descriptionStr));

        Button edit = (Button)gui.findViewById(R.id.button7);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                customDialog();
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

    private void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.allen_classroom_input, null);
        final EditText room = (EditText)dialog.findViewById(R.id.editText1);
        final EditText desc = (EditText)dialog.findViewById(R.id.editText2);
        room.setText(classroomStr);
        desc.setText(descriptionStr);

        builder.setView(dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String classroom = room.getText().toString();
                String description = desc.getText().toString();

                classroomStr = classroom;
                descriptionStr = description;
                classroomTV.setText(classroom);
                descriptionTV.setText(description);

                Intent intent = new Intent();
                intent.putExtra("building", classroom);
                intent.putExtra("desc", description);
                getActivity().setResult(NearVendingActivity.EDIT_BUILDING, intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }
}
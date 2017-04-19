package com.example.nothi.androidamenities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by allenhsu on 2017-03-29.
 */

public class BuildingDatabaseHelper extends SQLiteOpenHelper {
    // database info
    public static String DATABASE_NAME = "ROOT";
    public static int VERSION_NUM = 7;
    // buildings table keys
    public final static String KEY_ID = "_id";
    public final static String KEY_BUILDING = "BUILDING";
    public final static String KEY_DESCRIPTION = "DESC";
    public final static String KEY_FLOORS = "FLOORS";

    // stuffs
    public final static String KEY_BUILDING_FLOOR = "BUILDING_FLOOR";

    public static String TABLE_BUILDINGS = "buildings";
    public static String TABLE_VENDING = "VENDING_MACHINES";
    private String CLASS_NAME = "BuildingDatabaseHelper";

    // urls for master database
    private static String URL_ROOT = "http://algonquinstudents.ca/~hsu00016/android/master_list.xml";

    // other class variables
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<VendingMachine> vending_machines = new ArrayList<>();
    private Query q;
    private SQLiteDatabase database;


    public BuildingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    class Query extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... args) {
            InputStream in;
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

                    String tag = parser.getName();
                    if (!tag.contains("_")) {
                        String name = parser.getAttributeValue(null, "name");
                        int nFloors = Integer.parseInt(parser.getAttributeValue(null, "floors"));
                        buildings.add(new Building(tag, name, nFloors));
                    } else {
                        String desc = parser.getAttributeValue(null, "desc");
                        vending_machines.add(new VendingMachine(tag, desc));
                    }
                }
            } catch (XmlPullParserException p) {
            } catch (IOException e) {
            }

            return null;
        }

        protected void onPostExecute(String s) {
            ContentValues cv = new ContentValues();

            for (int i = 0; i < buildings.size(); i++) {
                cv.put(KEY_BUILDING, buildings.get(i).getCode());
                cv.put(KEY_DESCRIPTION, buildings.get(i).getDescription());
                cv.put(KEY_FLOORS, buildings.get(i).getFloors());
                database.insert(TABLE_BUILDINGS, "Null replacement value", cv);
            }

            cv = new ContentValues();
            for (int i = 0; i < vending_machines.size(); i++) {
                Log.i("asdaddas", vending_machines.get(i).getBuildingFloor());
                cv.put(KEY_BUILDING_FLOOR, vending_machines.get(i).getBuildingFloor());
                cv.put(KEY_DESCRIPTION, vending_machines.get(i).getDescription());
                database.insert(TABLE_VENDING, "Null replacement value", cv);
            }
        }
    }

    private class Building {
        String code, description;
        int floors;

        public Building(String code, String description, int floors) {
            this.code = code;
            this.description = description;
            this.floors = floors;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
        public int getFloors() { return floors; }
    }

    private class VendingMachine {
        String buildingFloor, description;

        public VendingMachine(String buildingFloor, String description) {
            this.buildingFloor = buildingFloor;
            this.description = description;
        }

        public String getBuildingFloor() { return buildingFloor; }
        public String getDescription() { return description; }
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        database = db;

        db.execSQL("CREATE TABLE " + TABLE_BUILDINGS + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING + " text, " + KEY_DESCRIPTION + " text, " + KEY_FLOORS + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_VENDING + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING_FLOOR + " text, " + KEY_DESCRIPTION + " text);");

        q = new Query();
        q.execute(URL_ROOT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDING);
        onCreate(db);
    }
}
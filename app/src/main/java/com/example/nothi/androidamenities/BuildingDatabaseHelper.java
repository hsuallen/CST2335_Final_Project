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
    public static int VERSION_NUM = 1;

    // buildings table keys
    public final static String KEY_ID = "_id";
    public final static String KEY_BUILDING = "BUILDING";
    public final static String KEY_DESCRIPTION = "DESC";
    public final static String KEY_FLOORS = "FLOORS";

    // stuffs
    public final static String KEY_VENDING_MACHINES = "VENDING_MACHINES";
    public final static String KEY_BUILDING_FLOOR = "BUILDING_FLOOR";

    public static String name = "buildings";
    public static String floor_desc = "floorDescriptions";
    private String CLASS_NAME = "BuildingDatabaseHelper";

    // urls for master database
    private static String URL_ROOT = "http://algonquinstudents.ca/~hsu00016/android/";
    private static String URL_LIST = URL_ROOT + "list_of_buildings.xml";

    // other class variables
    private ArrayList<Building> buildings = new ArrayList<>();
    private HashMap<String, String[]> vendingLocations;
    private boolean gettingFloors;
    private Query q;
    private SQLiteDatabase database;


    public BuildingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
        gettingFloors = false;
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
                    if (!gettingFloors) {
                        if (tag != "entry") {
                            String name = parser.getAttributeValue(null, "name");
                            int nFloors = Integer.parseInt(parser.getAttributeValue(null, "floors"));
                            Log.i("gg", tag + " ,Building: "+ name);
                            buildings.add(new Building(tag, name, nFloors));
                        }
                    } else {
                    }
                }
            } catch (XmlPullParserException p) {
            } catch (IOException e) {
            }

            return null;
        }

        protected void onPostExecute(String s) {
            if (!gettingFloors) {
                ContentValues cv = new ContentValues();
                for (int i = 0; i < buildings.size(); i++) {
                    String buildingCode = buildings.get(i).getCode();
                    cv.put(BuildingDatabaseHelper.KEY_BUILDING, buildingCode);
                    cv.put(BuildingDatabaseHelper.KEY_DESCRIPTION, buildings.get(i).getDescription());
                    cv.put(BuildingDatabaseHelper.KEY_FLOORS, buildings.get(i).getFloors());
                    database.insert(BuildingDatabaseHelper.name, "Null replacement value", cv);
                }
            } else {
                for (int i = 0; i < buildings.size(); i++) {
                    for (int j = 0; j < buildings.get(i).getFloors(); i++) {
                        q.execute(URL_ROOT + buildings.get(i).getCode() + "_" + (j+1) + ".xml");
                    }
                }
            }

            gettingFloors = true;
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

    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        database = db;

        db.execSQL("CREATE TABLE " + name + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING + " text, " + KEY_DESCRIPTION + " text, " + KEY_FLOORS + " INTEGER);");

        db.execSQL("CREATE TABLE " + floor_desc + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING_FLOOR + " text, " + KEY_DESCRIPTION + " text);");

        q = new Query();
        q.execute(URL_LIST);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + name);
        onCreate(db);
    }
}

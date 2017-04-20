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

/**
 *
 * BuildingDatabaseHelper
 *
 * This is the database helper class for the database being used throughout the app.
 *
 * @author Allen Hsu
 *
 */
public class BuildingDatabaseHelper extends SQLiteOpenHelper {

    /******************* START OF CLASS VARIABLES *******************/

    // database info
    public static String DATABASE_NAME = "ROOT";                        // the database's name
    public static int VERSION_NUM = 1;                                  // version of the database
    // buildings table keys
    public final static String KEY_ID = "_id";                          // the id key
    public final static String KEY_BUILDING = "BUILDING";               // the building key
    public final static String KEY_DESCRIPTION = "DESC";                // the description key
    public final static String KEY_FLOORS = "FLOORS";                   // the floor key
    public final static String KEY_BUILDING_FLOOR = "BUILDING_FLOOR";   // the building floor key

    // table names
    public static String TABLE_BUILDINGS = "buildings";                 // the table name for buildings
    public static String TABLE_VENDING = "VENDING_MACHINES";            // the table name for vending machines

    private String CLASS_NAME = "BuildingDatabaseHelper";               // the class name

    // the URL for the master list of buildings and vending machines
    private static String URL_ROOT = "http://algonquinstudents.ca/~hsu00016/android/master_list.xml";

    // other class variables
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<VendingMachine> vending_machines = new ArrayList<>();
    private Query q;
    private SQLiteDatabase database;

    /******************** END OF CLASS VARIABLES ********************/

    /**
     * The constructor.
     * @param ctx
     */
    public BuildingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     *
     */
    class Query extends AsyncTask<String, Integer, String> {

        /**
         * doInBackground is the main function in the AsyncTask, which runs various tasks in the
         *   background while the rest of the things are still running. In this case it is
         *   extracting all the buildings and vending machines from an XML file stored online.
         * @param args
         * @return null
         */
        protected String doInBackground(String... args) {
            InputStream in;
            try {
                // sets the URL to the first element in args, which is the URL for the XML file
                URL url = new URL(args[0]);

                // attempts to connect to the XML file
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

        /**
         * onPostExecute loads the buildings and the vending machines into the database after
         *   finishing extracting from the XML file stored online.
         * @param s
         */
        protected void onPostExecute(String s) {
            ContentValues cv = new ContentValues();

            // stores the buildings into the buildings table
            for (int i = 0; i < buildings.size(); i++) {
                cv.put(KEY_BUILDING, buildings.get(i).getCode());
                cv.put(KEY_DESCRIPTION, buildings.get(i).getDescription());
                cv.put(KEY_FLOORS, buildings.get(i).getFloors());
                database.insert(TABLE_BUILDINGS, "Null replacement value", cv);
            }

            // stores the vending machines into the vending machines table
            cv = new ContentValues();
            for (int i = 0; i < vending_machines.size(); i++) {
                Log.i("asdaddas", vending_machines.get(i).getBuildingFloor());
                cv.put(KEY_BUILDING_FLOOR, vending_machines.get(i).getBuildingFloor());
                cv.put(KEY_DESCRIPTION, vending_machines.get(i).getDescription());
                database.insert(TABLE_VENDING, "Null replacement value", cv);
            }
        }
    }

    /**
     * The building class is for storing the information extracted from the AsyncTask, then inserted
     *   into the database after the AsyncTask finishes extracting.
     */
    private class Building {
        String code, description;
        int floors;

        /**
         * The constructor.
         * @param code
         * @param description
         * @param floors
         */
        public Building(String code, String description, int floors) {
            this.code = code;
            this.description = description;
            this.floors = floors;
        }

        /**
         * The getter for the building code.
         * @return code
         */
        public String getCode() {
            return code;
        }

        /**
         * The getter for the description of the building.
         * @return description
         */
        public String getDescription() {
            return description;
        }

        /**
         * The getter for the number of floors.
         * @return floors
         */
        public int getFloors() {
            return floors;
        }
    }

    /**
     * The vending machine class is for storing the information extracted from the AsyncTask, then
     *   inserted into the database after the AsyncTask finishes extracting.
     */
    private class VendingMachine {
        String buildingFloor, description;

        /**
         * The constructor.
         * @param buildingFloor
         * @param description
         */
        public VendingMachine(String buildingFloor, String description) {
            this.buildingFloor = buildingFloor;
            this.description = description;
        }

        /**
         * The getter for the building floor.
         * @return building floor
         */
        public String getBuildingFloor() {
            return buildingFloor;
        }

        /**
         * The getter for the description of the vending machine location.
         * @return description
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * The onCreate function for the database.
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        database = db; // sets the class variable database to the db for outside scope usage.

        // creates the buildings table with the necessary attributes
        db.execSQL("CREATE TABLE " + TABLE_BUILDINGS + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING + " text, " + KEY_DESCRIPTION + " text, " + KEY_FLOORS + " INTEGER);");

        // creates the vending machine table with the necessary attributes
        db.execSQL("CREATE TABLE " + TABLE_VENDING + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING_FLOOR + " text, " + KEY_DESCRIPTION + " text);");

        q = new Query();

        // executes the doInBackground function with the master list URL
        q.execute(URL_ROOT);
    }

    /**
     * Checks if the database is behind on the version number, if so, drop all the tables and call
     *   the onCreate function again to reset the database.
     * @param db
     * @param oldVer
     * @param newVer
     */
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDING);
        onCreate(db);
    }
}
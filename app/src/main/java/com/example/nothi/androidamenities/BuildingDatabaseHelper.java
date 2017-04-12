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

/**
 * Created by allenhsu on 2017-03-29.
 */

public class BuildingDatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "ROOT";
    public static int VERSION_NUM = 5;
    public final static String KEY_ID = "_id";
    public final static String KEY_BUILDING = "BUILDING";
    public final static String KEY_DESCRIPTION = "DESC";
    public static String name = "buildings";
    private String CLASS_NAME = "BuildingDatabaseHelper";

    // urls for master database
    private static String URL_ROOT = "http://algonquinstudents.ca/~hsu00016/android/";
    private static String URL_LIST = URL_ROOT + "list_of_buildings.xml";
    private static String URL_ACCE = URL_ROOT + "ACCE_";
    private static String URL_B = URL_ROOT + "B_";
    private static String URL_C = URL_ROOT + "C_";
    private static String URL_E = URL_ROOT + "E_";
    private static String URL_T = URL_ROOT + "T_";

    private String buildings[] = new String[]{"ACCE", "B", "C", "E", "T"};
    private String fullname[] = new String[]{"Algonquin Centre for Construction Excellence",
            "School of Business", "Algonquin College Administrative Building",
            "Student Commons", "School of Advanced Technology"};

    public BuildingDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

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

                    String name = parser.getName();
                    if (name != "entry") {
                    }
                }
            } catch (XmlPullParserException p) {
            } catch (IOException e) {
            }

            return null;
        }
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        db.execSQL("CREATE TABLE " + name + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUILDING + " text, " + KEY_DESCRIPTION + " text);");

        ContentValues cv = new ContentValues();
        for (int i = 0; i < buildings.length; i++) {
            cv.put(BuildingDatabaseHelper.KEY_BUILDING, buildings[i]);
            cv.put(BuildingDatabaseHelper.KEY_DESCRIPTION, fullname[i]);
            db.insert(BuildingDatabaseHelper.name, "Null replacement value", cv);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + name);
        onCreate(db);
    }
}

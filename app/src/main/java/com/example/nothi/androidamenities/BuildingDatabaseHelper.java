package com.example.nothi.androidamenities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    private String buildings[] = new String[]{"ACCE", "B", "C", "E", "T"};
    private String fullname[] = new String[]{"Algonquin Centre for Construction Excellence",
            "School of Business", "Algonquin College Administrative Building",
            "Student Commons", "School of Advanced Technology"};

    public BuildingDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

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

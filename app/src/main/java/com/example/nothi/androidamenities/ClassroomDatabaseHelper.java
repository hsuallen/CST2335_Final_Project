package com.example.nothi.androidamenities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by allenhsu on 2017-03-29.
 */

public class ClassroomDatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "CLASSROOMS";
    public static int VERSION_NUM = 2;
    public final static String KEY_ID = "_id";
    public final static String KEY_ROOM = "ROOM";
    public final static String KEY_DESCRIPTION = "DESC";
    public static String name = "classrooms";
    private String CLASS_NAME = "ClassroomDatabaseHelper";

    public ClassroomDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

    public void onCreate(SQLiteDatabase db) {
        Log.i(CLASS_NAME, "Calling onCreate");
        db.execSQL("CREATE TABLE " + name + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ROOM + " text, " + KEY_DESCRIPTION + " text);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + name);
        onCreate(db);
    }
}

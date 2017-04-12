package com.example.nothi.androidamenities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ransom on 2/28/2017.
 */

public class MicroDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "microDatabase";
    private static int VERSION_NUM = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_BUILDING = "building";
    public static final String KEY_FLOOR = "floor";
    public static final String KEY_DESC = "description";
    public static final String TABLE_NAME = "micro_t";
    private static final String CREATE_TABLE_MICRO =
            "create table " + TABLE_NAME + "(" +
                    KEY_ID + " integer not null primary key autoincrement, " +
                    KEY_BUILDING + " text not null, " +
                    KEY_FLOOR + " integer not null, " +
                    KEY_DESC + " text not null" +
                    ");";

    public MicroDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MICRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}

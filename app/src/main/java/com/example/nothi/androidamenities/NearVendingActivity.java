package com.example.nothi.androidamenities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NearVendingActivity extends AppCompatActivity {
    protected final String ACTIVITY_NAME = "MainActivity";
    protected ArrayList<Classroom> classrooms = new ArrayList<>();
    protected ClassroomAdapter adapter;
    protected Cursor cursor;
    protected SQLiteDatabase db;

    protected class ClassroomAdapter extends ArrayAdapter<Classroom> {
        public ClassroomAdapter(Context ctx) { super(ctx, 0); }

        public int getCount() { return classrooms.size(); }
        public Classroom getItem(int pos) { return classrooms.get(pos); }
        public View getView(int pos, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = NearVendingActivity.this.getLayoutInflater();

            int layout = R.layout.classroom_row;
            View result = inflater.inflate(layout, null);

            TextView classroom = (TextView)result.findViewById(R.id.textView2);
            TextView description = (TextView)result.findViewById(R.id.textView3);
            classroom.setText(getItem(pos).getClassroom());
            description.setText(getItem(pos).getDescription());
            return result;
        }

        public long getItemId(int pos) {
            cursor.moveToPosition(pos);
            long id = cursor.getLong(cursor.getColumnIndex(ClassroomDatabaseHelper.KEY_ID));
            return id;
        }
    }

    private class Classroom {
        private String classroom, description;

        public Classroom(String classroom, String description) {
            this.classroom = classroom;
            this.description = description;
        }

        public void setClassroom(String classroom) { this.classroom = classroom; }
        public void setDescription(String description) { this.description = description; }
        public String getClassroom() { return this.classroom; }
        public String getDescription() { return this.description; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_vending);

        ClassroomDatabaseHelper c = new ClassroomDatabaseHelper(this);
        db = c.getWritableDatabase();

        try {
            cursor = db.query(ClassroomDatabaseHelper.name, new String[]{ClassroomDatabaseHelper.KEY_ROOM,
                            ClassroomDatabaseHelper.KEY_DESCRIPTION, ClassroomDatabaseHelper.KEY_ID},null, null,
                    null, null, null);
            cursor.moveToFirst();
            int columns = cursor.getColumnCount();

            while (!cursor.isAfterLast()) {
                String room = cursor.getString(cursor.getColumnIndex(ClassroomDatabaseHelper.KEY_ROOM));
                String desc = cursor.getString(cursor.getColumnIndex(ClassroomDatabaseHelper.KEY_DESCRIPTION));
                classrooms.add(new Classroom(room, desc));
                cursor.moveToNext();
            }

            Log.i(ACTIVITY_NAME, "Cursor's column count = " + columns);
            for (int i = 0; i < columns; i++) {
                Log.i(ACTIVITY_NAME, "Column " + i + "'s name is: " + cursor.getColumnName(i));
            }

            Button add = (Button)findViewById(R.id.button3);
            ListView list = (ListView)findViewById(R.id.listview);

            adapter = new ClassroomAdapter(this);
            list.setAdapter(adapter);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog();
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(ACTIVITY_NAME, "Something");
                }
            });
        } catch (Exception e) { Log.e("Exception: ", e.getMessage()); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_about, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_help:
                // TODO: add a snackbar here
                break;
            case R.id.action_home:
                finish();
                break;
        }
        return true;
    }

    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    protected void onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    protected void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }

    private void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialog = inflater.inflate(R.layout.allen_classroom_input, null);

        builder.setView(dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog d = (Dialog)dialog;
                EditText room = (EditText)d.findViewById(R.id.editText1);
                EditText desc = (EditText)d.findViewById(R.id.editText2);

                ContentValues cv = new ContentValues();

                String Room = room.getText().toString();
                String Desc = desc.getText().toString();
                classrooms.add(new Classroom(Room, Desc));
                cv.put(ClassroomDatabaseHelper.KEY_ROOM, Room);
                cv.put(ClassroomDatabaseHelper.KEY_DESCRIPTION, Desc);
                db.insert(ClassroomDatabaseHelper.name, "Null replacement value", cv);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }
}

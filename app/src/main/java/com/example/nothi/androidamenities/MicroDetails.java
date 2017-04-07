/*
 * Filename: MicroActivity.java
 * Author: Scott McClare
 * Course: CST2335 - Graphical Programming
 * Term Project
 * Date: April 20, 2017
 * Professor: Eric Torunski
 * Purpose: This Activity presents a detailed look at a particular microwave oven entry in the
 * MicroActivity list. It provides an option to delete the microwave from the list, as well as
 * "chat"-style messages from users who may wish to alert other students about problems with that
 * particular device.
 */

package com.example.nothi.androidamenities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class launches an Activity presenting a detailed look at a particular microwave oven entry
 * in the NearMicrowave list. It provides an option to delete the microwave from the list, as well
 * as "chat"-style messages from users who may wish to alert other students about problems with that
 * particular device.
 * @author    Scott McClare
 * @version   1.0.0 April 20, 2017
 * @since 1.8.0_112
 */
public class MicroDetails extends AppCompatActivity {

    /** The name of the current activity */
    private static final String ACTIVITY_NAME = "MicroDetails";
    /** The TextView containing the String microText. */
    TextView microDetail, msgsHeading;
    /** Button for deleting this particular microwave. */
    Button deleteMsgButton;
    /** Button for opening a message dialog box */
    Button chatButton;
    /**
     * A ListView containing "chat" messages. Presently hard-coded, but in a future iteration will
     * be downloaded from the Web from a site that simulates random chat messages.
     */
    ListView chatMsgs;
    /** The text of the selected entry. */
    String microText;
    /** A progress bar */
    ProgressBar progressBar;
    /** An ArrayList containing chat messages. */
    ArrayList<String> microChatMsgs;
    /** The adapter to display the ArrayList in the ListView. */
    ArrayAdapter<String> a;
    /**
     * The ID of the microwave item from NearMicrowave's ArrayList; used if the item is to be
     * deleted from the list.
     */
    long microId;
    /** URL of a PHP script that simulates chat messages. */
    String chatURL = "http://www.algonquinstudents.ca/~mccl0173/cst2335/chat.php";
    /** EditText containing a user name for chat messages */
    EditText chatName;
    /** EditText containing a chat message */
    EditText chatMsg;

    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_fragment);

        // Get the params from the Intent bundle
        b = this.getIntent().getExtras();
        microId = b.getLong("microID");
        microText = b.getString("microText");

        // show the fragment
        MicrowaveFragment micro = new MicrowaveFragment();
        micro.setArguments(b);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.microDetailsFrame, micro)
                .commit();
    }


    } // end of outer class MicroDetails

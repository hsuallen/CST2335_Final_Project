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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    /**
     * The TextView containing the String microText.
     */
    TextView microDetail, msgsHeading;
    /**
     * Button for deleting this particular microwave.
     */
    Button deleteMsgButton;
    /**
     * A ListView containing "chat" messages. Presently hard-coded, but in a future iteration will
     * be downloaded from the Web from a site that simulates random chat messages.
     */
    ListView chatMsgs;
    /**
     * The text of the selected entry.
     */
    String microText;
    /**
     * An ArrayList containing chat messages.
     */
    ArrayList<String> microChatMsgs;
    /**
     * The adapter to display the ArrayList in the ListView.
     */
    ArrayAdapter<String> a;
    /**
     * The ID of the microwave item from NearMicrowave's ArrayList; used if the item is to be
     * deleted from the list.
     */
    long microId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_details);

        // set the message text
        Intent i = getIntent(); // the Intent passed from NearMicrowave
        microId = i.getExtras().getLong("microId");
        microText = i.getExtras().getString("microText");
        microDetail = (TextView) findViewById(R.id.msgText);
        microDetail.setText(microText);

        // populate the chat window with some dummy messages
        chatMsgs = (ListView) findViewById(R.id.chatMsgs);
        microChatMsgs = new ArrayList<>();
        populateChat();
        a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, microChatMsgs);
        chatMsgs.setAdapter(a);

        // enable the Delete button
        deleteMsgButton = (Button) findViewById(R.id.microDelButton);
        deleteMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("microId", microId);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    /**
     * Placeholder method simulating chat messages; to be replaced in future iterations with
     * downloaded content from a chat simulator.
     */
    protected void populateChat() {
        microChatMsgs.add("One microwave has a blown fuse or something - long lineup. - @Scott");
        microChatMsgs.add("Is this thing working? It takes forever to heat soup. - @Wei");
        microChatMsgs.add("Too many people waiting, I'm going to the caf. - @Jacob");
    }
}

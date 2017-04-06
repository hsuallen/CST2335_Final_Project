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
        //a = new ArrayAdapter<String>(MicroDetails.this, android.R.layout.simple_list_item_1, microChatMsgs);

        // Set the progress bar to 0% and make it invisible
        progressBar = (ProgressBar) findViewById(R.id.chatProgress);
        progressBar.setProgress(0);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        // set up the ListView and ArrayList
        chatMsgs = (ListView) findViewById(R.id.chatMsgs);
        microChatMsgs = new ArrayList<>();

        // Run the AsyncTask that downloads chat messages
        ChatLog chatlog = new ChatLog();
        chatlog.execute(chatURL);

        // enable the Delete button
        deleteMsgButton = (Button) findViewById(R.id.microDelButton);
        deleteMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });

        // enable the "Send a Message" button
        chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeChatDialog();
            }
        });
    }

    // create an AlertDialog for deleting the microwave
    public void deleteDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle(R.string.delMicroTitle)
                .setMessage("This can't be undone.")
                .setPositiveButton(R.string.delMicroOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // OK was clicked; return to NearMicrowave and delete the microwave
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("microId", microId);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.delMicroCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel was clicked; do nothing
                    }
                 })
                .create()
                .show();
    }

    // create the message custom dialog
    public void makeChatDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_micro_chat, null);
        chatName = (EditText) v.findViewById(R.id.chatNameEdit);
        chatMsg = (EditText) v.findViewById(R.id.chatMsgEdit);

        b.setView(v)
                .setTitle("Send a Message")
                .setPositiveButton(R.string.microSendBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // add the message to the chat window
                        microChatMsgs.add(chatName.getText().toString());
                        microChatMsgs.add(chatMsg.getText().toString());
                        a.notifyDataSetChanged();
                        // notify the user with a Toast
                        Toast msgToast = Toast.makeText(v.getContext(), "Message sent!", Toast.LENGTH_SHORT);
                        msgToast.show();
                    }

                })
                .setNegativeButton(R.string.microCancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // just cancel
                    }
                })
                .create()
                .show();
    }


    /**
     * This inner class is an AsyncTask that grabs an XML file simulating "chat" messages from my
     * Web site and populates the chat ListView with them.
     * @author    Scott McClare
     * @version   1.0.0 April 20, 2017
     * @since 1.8.0_112
     */
    public class ChatLog extends AsyncTask<String, Integer, String> {

        /** Integer for filling in the progress bar as the data loads */
        int progress = 0;

        protected String doInBackground(String ... s) {

            try {
                // Open up a connection to the Web site with the chat simulator
                URL url = new URL(s[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Start the query
                conn.connect();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();
                // Pass the query to the readChat method
                readChat(parser);
            } catch (Exception ex) {
                Log.d(ACTIVITY_NAME, Log.getStackTraceString(ex));
            }

            return "done";
        }

        /**
         * Update the progress bar
         * @param p Progress percentage as passed from readChat()
         */
        protected void onProgressUpdate(Integer ... p) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress(p[0]);
        }

        /**
         * Hide the progress bar after the download is done, and populate the chat window with
         * messages
         * @param s The string passed from doInBackground() upon completion
         */
        protected void onPostExecute(String s) {
            if (s.equals("done")) {
                // hide the progress bar
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                // Show the messages
                a = new ArrayAdapter<String>(MicroDetails.this, android.R.layout.simple_list_item_1, microChatMsgs);
                chatMsgs.setAdapter(a);
            }
        }
        /**
         * Check the XML for the name and message attributes of each chat message, and add them to
         * the ArrayList
         * @param parser The XmlPullParser from doInBackground
         * @throws XmlPullParserException
         * @throws IOException
         * @throws InterruptedException
         */
        private void readChat(XmlPullParser parser)
        throws XmlPullParserException, IOException, InterruptedException {
            parser.require(XmlPullParser.START_TAG, null, null);
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tag = parser.getName();
                if (tag.equals("name")) {
                    microChatMsgs.add(parser.nextText());
                    publishProgress(progress += 9);
                }
                if (tag.equals("message")) {
                    microChatMsgs.add(parser.nextText());
                    publishProgress(progress += 9);
                }
                // update the progress bar
                Thread.sleep(100);
            }
            // Load completed - set the progress bar to 100%
            publishProgress(100);
        }

    } // end of inner class ChatLog

} // end of outer class MicroDetails

package com.example.nothi.androidamenities;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;
import static com.example.nothi.androidamenities.R.id.chatMsgs;

/**
 * Created by Ransom on 4/6/2017.
 */

public class MicrowaveFragment extends Fragment {

    final String ACTIVITY_NAME = "MicrowaveFragement";
    View chatWindow;
    Long microId;
    String microText;

    TextView microDetails;
    Button deleteButton, chatButton;

    /** A progress bar */
    ProgressBar progressBar;
    ListView chatMsgs;
    /** An ArrayList containing chat messages. */
    ArrayList<String> microChatMsgs;
    /** The adapter to display the ArrayList in the ListView. */
    ArrayAdapter<String> a;

    String chatURL = "http://www.algonquinstudents.ca/~mccl0173/cst2335/chat.php";
    /** EditText containing a user name for chat messages */
    EditText chatName;
    /** EditText containing a chat message */
    EditText chatMsg;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatWindow = inflater.inflate(R.layout.activity_micro_details, null);

        // set up the TextView with the microwave location
        microDetails = (TextView) chatWindow.findViewById(R.id.msgText);
        Bundle b = this.getArguments();
        microDetails.setText(b.getString("microText"));

        // set up the delete button
        deleteButton = (Button) chatWindow.findViewById(R.id.microDelButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // notify the user that it was done
                Snackbar.make(chatMsgs, R.string.delMicroSnackbar, Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        // set up the progress bar
        // Set the progress bar to 0% and make it invisible
        progressBar = (ProgressBar) chatWindow.findViewById(R.id.chatProgress);
        progressBar.setProgress(0);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        // set up the chat window
        chatMsgs = (ListView) chatWindow.findViewById(R.id.chatMsgs);
        microChatMsgs = new ArrayList<>();

        // set up the chat button
        chatButton = (Button) chatWindow.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeChatDialog();
                Toast.makeText(MicrowaveFragment.this.getActivity(),
                        "Message sent!", Toast.LENGTH_SHORT).show();
            }
        });

        ChatLog chatlog = new ChatLog();
        chatlog.execute(chatURL);

        return chatWindow;
    }

    // create an AlertDialog for deleting the microwave
    public void deleteDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(chatWindow.getContext());
        d.setTitle(R.string.delMicroTitle)
                .setMessage("This can't be undone.")
                .setPositiveButton(R.string.delMicroOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // OK was clicked; return to NearMicrowave and delete the microwave
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("microId", microId);
                        //setResult(RESULT_OK, returnIntent);
                        //finish();
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
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
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
           // if (s.equals("done")) {
                // hide the progress bar
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                // Show the messages
                a = new ArrayAdapter<String>(MicrowaveFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1, microChatMsgs);
                chatMsgs.setAdapter(a);
            //}
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

}

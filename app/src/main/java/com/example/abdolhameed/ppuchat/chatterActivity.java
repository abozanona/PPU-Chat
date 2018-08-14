package com.example.abdolhameed.ppuchat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class chatterActivity extends AppCompatActivity {

    public static String chattingWith;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter);

        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        h.postDelayed(new Runnable() {
            public void run() {
                //do something

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("action", "get_msg");
                data.put("from", friendsList.username);
                data.put("to", chattingWith);

                ServerHandler loginjson;
                loginjson = new ServerHandler(getApplicationContext(), data);
                if (loginjson.getData("status").equals("false")) {
                //    Toast.makeText(getApplicationContext(), loginjson.getJson(), Toast.LENGTH_LONG).show();
                    return;
                }
                ((TextView)findViewById(R.id.conver)).setText(loginjson.getData("msg"));
                h.postDelayed(this, delay);
            }
        }, delay);

        ((Button)findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=((EditText)findViewById(R.id.msgtext)).getText().toString();
                ((EditText)findViewById(R.id.msgtext)).setText("");
                TextView conv=(TextView)findViewById(R.id.conver);
                conv.setText(friendsList.username +":"+str+"\n"+conv.getText().toString());

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("action", "add_msg");
                data.put("from", friendsList.username);
                data.put("to", ((TextView)findViewById(R.id.chattingWith)).getText().toString());
                data.put("msg", str);

                ServerHandler loginjson;
                loginjson = new ServerHandler(getApplicationContext(), data);
                if (loginjson.getData("status").equals("false")) {
                    Toast.makeText(getApplicationContext(), "Unknown error occured", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }


    public void onResume(){
        super.onResume();
        ((TextView) findViewById(R.id.chattingWith)).setText(chattingWith);

    }
}

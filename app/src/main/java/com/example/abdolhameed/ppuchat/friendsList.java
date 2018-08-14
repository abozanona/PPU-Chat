package com.example.abdolhameed.ppuchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class friendsList extends AppCompatActivity {

    ArrayList<String> allFriends;
    ListView listView;
    public static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        getWindow().getDecorView().setBackgroundColor(Color.GRAY);

        listView=(ListView)findViewById(R.id.listview);
        findViewById(R.id.addfriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), addFriend.class);
                startActivity(i);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                Intent i=new Intent(getApplicationContext(), chatterActivity.class);
                chatterActivity.chattingWith=itemValue;
                startActivity(i);

            }

        });
    }




    @Override
    public void onResume(){
        super.onResume();
        allFriends=new ArrayList<String>();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("action", "getallfriends");
        data.put("myname", friendsList.username);
        data.put("username", username);

        ServerHandler loginjson;
        loginjson = new ServerHandler(getApplicationContext(), data);
        if (loginjson.getData("status").equals("false")) {
            Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_LONG);
            return;
        }

        //Toast.makeText(getApplicationContext(),loginjson.getData("friends"),Toast.LENGTH_LONG).show();

        try {
            String username;
            JSONArray array = null;
            array = new JSONArray(loginjson.getData("friends"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                username = row.getString("username");
                allFriends.add(username);
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Unexpaeted Error occured, try later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        ArrayAdapter<String> arrad=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,allFriends);
        listView.setAdapter(arrad);
    }
}

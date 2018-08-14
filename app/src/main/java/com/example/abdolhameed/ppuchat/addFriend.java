package com.example.abdolhameed.ppuchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class addFriend extends AppCompatActivity {
    ArrayList<String> allFriends;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        getWindow().getDecorView().setBackgroundColor(Color.GRAY);


        listView=(ListView)findViewById(R.id.people);
        hahaha();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                HashMap<String ,String> data=new HashMap<String ,String>();
                data.put("action", "addfriends");
                data.put("from", friendsList.username);
                data.put("to", itemValue);

                ServerHandler loginjson;
                loginjson= new ServerHandler(getApplicationContext(), data);
                Intent i=new Intent(getApplicationContext(), friendsList.class);
                startActivity(i);
            }

        });
    }

void hahaha(){
    ((EditText)findViewById(R.id.Field1)).addTextChangedListener(new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            //Toast.makeText(getApplicationContext(), "hhhhhhhhhhhh", Toast.LENGTH_LONG).show();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            allFriends=new ArrayList<String>();

            HashMap<String, String> data = new HashMap<String, String>();
            data.put("action", "getpeople");
            data.put("username", ((EditText)findViewById(R.id.Field1)).getText().toString());

            ServerHandler loginjson;
            loginjson = new ServerHandler(getApplicationContext(), data);
            if (loginjson.getData("status").equals("false")) {
                Toast.makeText(getApplicationContext(), "Something weird happened", Toast.LENGTH_LONG);
                return;
            }


            //Toast.makeText(getApplicationContext(),loginjson.getJson(), Toast.LENGTH_LONG).show();

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
    });
}

}

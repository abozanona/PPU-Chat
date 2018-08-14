package com.example.abdolhameed.ppuchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    String llogin,lpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llogin=((EditText)findViewById(R.id.lusername)).getText().toString();
                lpassword=((EditText)findViewById(R.id.lpassword)).getText().toString();


                if (llogin.equals("") || lpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("action", "login");
                data.put("username", llogin);
                data.put("password", lpassword);

                ServerHandler loginjson;
                loginjson = new ServerHandler(getApplicationContext(), data);
                if (loginjson.getData("status").equals("false")) {
                    Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Logged in, goooood!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), friendsList.class);
                startActivity(i);

                friendsList.username=llogin;
            }
        });
    }

}

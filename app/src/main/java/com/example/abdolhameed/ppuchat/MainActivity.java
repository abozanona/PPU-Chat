package com.example.abdolhameed.ppuchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String username, password, passwordagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=((EditText)findViewById(R.id.susername)).getText().toString();
                password=((EditText)findViewById(R.id.spassword)).getText().toString();
                passwordagain=((EditText)findViewById(R.id.spasswordagain)).getText().toString();

                if(username.equals("") || password.equals("") || passwordagain.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields can't be empty!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!passwordagain.equals(password)){
                    Toast.makeText(getApplicationContext(), "passwords mismatches",Toast.LENGTH_LONG).show();
                    return;
                }
                HashMap<String ,String> data=new HashMap<String ,String>();
                data.put("action", "signup");
                data.put("username", username);
                data.put("password", password);

                ServerHandler loginjson;
                loginjson= new ServerHandler(getApplicationContext(), data);
                if(loginjson.getData("status").equals("false")){
                    Toast.makeText(getApplicationContext(),"User already exist", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"User Registersd!You can login now!", Toast.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }
}

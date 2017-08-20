package com.example.sahilarora.fireapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    private EditText Pname;
    private Button Pbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Pname=(EditText) findViewById(R.id.profileNameField);
        Pbutton=(Button)findViewById(R.id.updateProfilebtn);


    }
}

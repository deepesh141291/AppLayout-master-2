package com.example.abhay.applayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        contact_elements con = new contact_elements();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact);

        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        final String[] custom_msg = new String[1];

        Button mButton;
        final EditText mEdit;

        mButton = (Button) findViewById(R.id.Button);
        mEdit = (EditText) findViewById(R.id.cont);
        con = dc.showContact();
        mEdit.setText((CharSequence) con.getContact());

    }
}

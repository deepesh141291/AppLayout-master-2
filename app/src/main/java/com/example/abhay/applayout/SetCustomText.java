package com.example.abhay.applayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetCustomText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final cus_msg_elements[] cus = {(cus_msg_elements) getApplicationContext()};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_custom_text);

        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        final String[] custom_msg = new String[1];

        Button mButton;
        final EditText mEdit;

        mButton = (Button) findViewById(R.id.Button);
        mEdit = (EditText) findViewById(R.id.custom_txt);
        cus[0] = dc.showMsg();
        mEdit.setText((CharSequence) cus[0].getMsg());


        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        custom_msg[0] = mEdit.getText().toString();
                        dc.addMsg(new cus_msg_elements(custom_msg[0]));
                        Toast.makeText(SetCustomText.this, "Message Set", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}

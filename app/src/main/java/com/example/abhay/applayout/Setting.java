package com.example.abhay.applayout;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by deepesh on 04/05/17.
 */

public class Setting extends AppCompatActivity {

    private Spinner spinner1;
    String s;
    Button button ;
    TextView name,number;
    public  static final int RequestPermissionCode  = 1 ;
    Intent intent ;
    Switch mySwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_setting);



        final DatabaseConnectivity dc = new DatabaseConnectivity(this);
        spinner1 = (Spinner) findViewById(R.id.planets_spinner);
        if(dc.showRADIUS().getRadius()!=null){
            if(Integer.valueOf(String.valueOf(dc.showRADIUS().getRadius()))!=1) {
                spinner1.setSelection(Integer.valueOf(String.valueOf(dc.showRADIUS().getRadius()))-1);
            }
        }
        System.out.println("STart mid"+spinner1.getSelectedItem().toString());
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s = parent.getItemAtPosition(position).toString();
                dc.addRADIUS(new saved_RADIUS(s));
                System.out.println(s);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final cus_msg_elements[] cus = {(cus_msg_elements) getApplicationContext()};
        final String[] custom_msg = new String[1];

        Button mButton;
        final EditText mEdit;

        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.custom_txt);
        cus[0] = dc.showMsg();
        mEdit.setText((CharSequence) cus[0].getMsg());


        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        custom_msg[0] = mEdit.getText().toString();
                        dc.addMsg(new cus_msg_elements(custom_msg[0]));
                        Toast.makeText(Setting.this, "Message Set", Toast.LENGTH_SHORT).show();
                    }
                });



        final String[] custom_msg1 = new String[1];

        Button mButton1;
        final TextView mEdit1;
        contact_elements con = new contact_elements();

        mEdit1 = (TextView) findViewById(R.id.cont);
        con = dc.showContact();
        mEdit1.setText((CharSequence) con.getContact());


        button = (Button)findViewById(R.id.button3);
        name = (TextView)findViewById(R.id.textView2);
        number = (TextView)findViewById(R.id.cont);
        con = dc.showContact();
        number.setText((CharSequence) con.getContact());
        name.setText((CharSequence) con.getContactname());

        EnableRuntimePermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);

            }
        });

        mySwitch = (Switch) findViewById(R.id.switch1);
        System.out.println(dc.showContact().getMsgflag());
        if(dc.showContact().getMsgflag()!=null) {
            System.out.println(dc.showContact().getMsgflag());
            if (dc.showContact().getMsgflag().contains("1")) {
                //dc.addContact(new contact_elements(dc.showContact().getContact(),dc.showContact().getContactname(),"1"));
                //mySwitch.setSwitchMinWidth();
                mySwitch.setChecked(true);
                System.out.println("set"+dc.showContact().getMsgflag());
                TextView contactname = (TextView) findViewById(R.id.textView2);
                contactname.setVisibility(View.VISIBLE);
                TextView contact = (TextView) findViewById(R.id.cont);
                contact.setVisibility(View.VISIBLE);
                TextView smstextlabel = (TextView) findViewById(R.id.sms_txt);
                smstextlabel.setVisibility(View.VISIBLE);
                TextView customtext = (TextView) findViewById(R.id.custom_txt);
                customtext.setVisibility(View.VISIBLE);
                Button contactbutton = (Button) findViewById(R.id.button3);
                contactbutton.setVisibility(View.VISIBLE);
                Button text = (Button) findViewById(R.id.button3);
                text.setVisibility(View.VISIBLE);
                Button settext = (Button) findViewById(R.id.button);
                settext.setVisibility(View.VISIBLE);
            }
            if(dc.showContact().getMsgflag().contains("0")){
                //dc.addContact(new contact_elements(dc.showContact().getContact(),dc.showContact().getContactname(),"0"));
                mySwitch.setChecked(false);
                System.out.println("unset"+dc.showContact().getMsgflag());
                TextView contactname = (TextView) findViewById(R.id.textView2);
                contactname.setVisibility(View.GONE);
                TextView contact = (TextView) findViewById(R.id.cont);
                contact.setVisibility(View.GONE);
                TextView smstextlabel = (TextView) findViewById(R.id.sms_txt);
                smstextlabel.setVisibility(View.GONE);
                TextView customtext = (TextView) findViewById(R.id.custom_txt);
                customtext.setVisibility(View.GONE);
                Button contactbutton = (Button) findViewById(R.id.button3);
                contactbutton.setVisibility(View.GONE);
                Button text = (Button) findViewById(R.id.button3);
                text.setVisibility(View.GONE);
                Button settext = (Button) findViewById(R.id.button);
                settext.setVisibility(View.GONE);
            }
        }
        //mySwitch.setOnCheckedChangeListener();
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    //switchStatus.setText("Switch is currently ON");
                    Toast.makeText(Setting.this, "checked", Toast.LENGTH_SHORT).show();
                    dc.addContact(new contact_elements(dc.showContact().getContact(),dc.showContact().getContactname(),"1","0"));
                    System.out.println(dc.showContact().getMsgflag());
                    TextView contactname = (TextView) findViewById(R.id.textView2);
                    contactname.setVisibility(View.VISIBLE);
                    TextView contact = (TextView) findViewById(R.id.cont);
                    contact.setVisibility(View.VISIBLE);
                    TextView smstextlabel = (TextView) findViewById(R.id.sms_txt);
                    smstextlabel.setVisibility(View.VISIBLE);
                    TextView customtext = (TextView) findViewById(R.id.custom_txt);
                    customtext.setVisibility(View.VISIBLE);
                    Button contactbutton = (Button) findViewById(R.id.button3);
                    contactbutton.setVisibility(View.VISIBLE);
                    Button text = (Button) findViewById(R.id.button3);
                    text.setVisibility(View.VISIBLE);
                    Button settext = (Button) findViewById(R.id.button);
                    settext.setVisibility(View.VISIBLE);

                }else{
                    //switchStatus.setText("Switch is currently OFF");
                    Toast.makeText(Setting.this, "not checked", Toast.LENGTH_SHORT).show();
                    dc.addContact(new contact_elements(dc.showContact().getContact(),dc.showContact().getContactname(),"0","0"));
                    System.out.println(dc.showContact().getMsgflag());
                    TextView contactname = (TextView) findViewById(R.id.textView2);
                    contactname.setVisibility(View.GONE);
                    TextView contact = (TextView) findViewById(R.id.cont);
                    contact.setVisibility(View.GONE);
                    TextView smstextlabel = (TextView) findViewById(R.id.sms_txt);
                    smstextlabel.setVisibility(View.GONE);
                    TextView customtext = (TextView) findViewById(R.id.custom_txt);
                    customtext.setVisibility(View.GONE);
                    Button contactbutton = (Button) findViewById(R.id.button3);
                    contactbutton.setVisibility(View.GONE);
                    Button text = (Button) findViewById(R.id.button3);
                    text.setVisibility(View.GONE);
                    Button settext = (Button) findViewById(R.id.button);
                    settext.setVisibility(View.GONE);
                }

            }
        });

        if(dc.showMODE().getMODE()==null) {
            RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
            rg.check(R.id.radio_driving);
        }
        else{
            if(dc.showMODE().getMODE()!=null){
                if(dc.showMODE().getMODE().contains("driving")) {
                    RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
                    rg.check(R.id.radio_driving);
                }
                if(dc.showMODE().getMODE().contains("train")){
                    RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
                    rg.check(R.id.radio_rail);
                }
                if(dc.showMODE().getMODE().contains("bus")){
                    RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
                    rg.check(R.id.radio_bus);
                }
            }
        }

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                name.setText(TempNameHolder);

                                number.setText(TempNumberHolder);

                                dc.addContact(new contact_elements(TempNumberHolder, TempNameHolder, "1","0"));

                            }
                        }

                    }
                }
                break;
        }
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus:
                if (checked) {
                    // Pirates are the best
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=bus&key=AIzaSyDnMgSPYJXXYVncGgqtH8YuEF-7rA8JiF4"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
            case R.id.radio_rail:
                if (checked) {
                    // Ninjas rule
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=train&key=AIzaSyDnMgSPYJXXYVncGgqtH8YuEF-7rA8JiF4"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
            case R.id.radio_driving:
                if (checked) {
                    // Ninjas rule
                    dc.addMODE(new saved_MODE("mode=driving"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
        }
    }


}

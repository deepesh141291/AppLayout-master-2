package com.example.abhay.applayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

/**
 * Created by deepesh on 23/04/17.
 */

public class SetRadiusActivity extends AppCompatActivity {

    private Spinner spinner1;
    String s;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_radius);
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


   }


}

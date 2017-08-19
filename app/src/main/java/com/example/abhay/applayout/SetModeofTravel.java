package com.example.abhay.applayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by deepesh on 23/04/17.
 */

public class SetModeofTravel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_distance);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus:
                if (checked) {
                    // Pirates are the best
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=bus&key=AIzaSyBLjNlZNJRHpgRRnByW17kAp7RHw7FtZRA"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
            case R.id.radio_rail:
                if (checked) {
                    // Ninjas rule
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=train&key=AIzaSyBLjNlZNJRHpgRRnByW17kAp7RHw7FtZRA"));
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

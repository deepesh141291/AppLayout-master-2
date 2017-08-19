//package testing.gps_service;
package com.example.abhay.applayout;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
//package com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhay.applayout.GPS_Service;
import com.example.abhay.applayout.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;



public class SetDestinationActivity extends FragmentActivity implements PlaceSelectionListener {

    private TextView mPlaceDetailsText;




    private TextView mPlaceAttribution;


        //if(alarmBroadcastReceiver)




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        //textView = (TextView) findViewById(R.id.textView);



        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);

        // Retrieve the TextViews that will display details about the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);

        Button btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                startActivity(new Intent(SetDestinationActivity.this, MainActivity.class));
                //Intent mainIntent = new Intent(MainActivity.this, SetDestinationActivity.class);
            }
        });


    }

    @Override
    public void onPlaceSelected(Place place) {

        final DatabaseConnectivity dc = new DatabaseConnectivity(this);
        //Log.i(TAG, "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.
        //place1 = place;
        //i = 1;
        mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));
        LatLng newlocation = place.getLatLng();
        String lat = String.valueOf(newlocation.latitude);
        String lng = String.valueOf(newlocation.longitude);

        dc.addDetails(new saved_data_elements((String) place.getName()+"c",lat,lng));
        dc.addDistance(new saved_distance("0.0","0.0","0.0"));
        if(dc.showMODE().getMODE()==null) {
            dc.addMODE(new saved_MODE("mode=driving"));
        }
        if(dc.showRADIUS().getRadius()==null) {
            dc.addRADIUS(new saved_RADIUS("1"));
        }
        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {
            mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
        } else {
            mPlaceAttribution.setText("");
        }
    }
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        //Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
        //websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }



    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }
}

package com.example.abhay.applayout;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by filipp on 6/16/2016.
 */
public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
            System.out.println("create fun");
            listener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    Intent i = new Intent("location_update");
                    i.putExtra("Longitude",location.getLongitude());
                    i.putExtra("Latitude",location.getLatitude());
                    sendBroadcast(i);
                    System.out.println("on loc fun");
                    Toast.makeText(GPS_Service.this, "Location changed :"+location.getLatitude(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,0,listener);

    }

    @Override
    public void onDestroy() {
        System.out.println("Stop service on destroy");
        Toast.makeText(GPS_Service.this, "Stop Loc fun", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}

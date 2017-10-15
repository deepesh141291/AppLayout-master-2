package deepesh.travel.abhay.applayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlaceSelectionListener {

    public Double dlat, dlong;
    public String clat, clong;
    String ALARM_SET = "ALARM_SET";
    int MY_PERMISSION_REQUEST_SMS = 1;
    int MY_PERMISSION_REQUEST_CALL = 1;
    static int alarm = 0;

    String SENT_SMS = "SMS_SENT";
    String DELIVERED = "SMS_Delivered";
    private LocationManager locationManager;
    PendingIntent sentPendingIntent, deliveredPendingIntent, alarmPendingIntent, alarmOffPendingIntent;
    BroadcastReceiver smsSentReciever, smsDeliveredReciever, alarmBroadcastReceiver, alarmOffBroadcastReceiver;
    static MediaPlayer mp;
    String ALARM_OFF = "ALARM_OFF";
    private LocationListener listener;
    TextView dist, dest, radius, remaining;
    private Spinner spinner1;
    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;
    String s;
    int i = 0;
    int j = 0, k = 0;
    String cont, smstext;
    saved_data_elements sde;
    contact_elements con;
    Double d = 1.0;
    saved_distance d1;
    String dis1 = "";
    ImageView mode;
    View myview;
    ProgressBar mProgress;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i = 0;
        j = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //database object
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(alarm ==1){
            mp.stop();
            alarm = 0;
            System.out.println("calling calling");
            send_sms();
        }



        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        String rad = sf.getString("Radius","1");
        //Toast.makeText(this, rad, Toast.LENGTH_SHORT).show();
        dc.addRADIUS(new saved_RADIUS(rad));
        System.out.println(rad);

        String SMS_text = sf.getString("sms_text","I am going to reach");
        dc.addMsg(new cus_msg_elements(SMS_text));



        String mod = sf.getString("mode","driving");
        switch(mod) {
            case "bus":
                {
                    // Pirates are the best
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=bus&key=AIzaSyASqE50Ap-zcLrCDljRzfk2RnyM9d1nBAQ"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
            case "train":
                {
                    // Ninjas rule
                    dc.addMODE(new saved_MODE("mode=transit&transit_mode=train&key=AIzaSyASqE50Ap-zcLrCDljRzfk2RnyM9d1nBAQ"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
            case "driving":
                {
                    // Ninjas rule
                    dc.addMODE(new saved_MODE("mode=driving"));
                    System.out.println(dc.showMODE().getMODE());
                    break;
                }
        }



        // Set Msg Flag if it is NULL
        if (dc.showContact().getMsgflag()== null) {
            dc.addContact(new contact_elements("click on Turn on Switch to set contact number", "click on Turn on Switch to set contact name", "0","1"));
            System.out.println("main activity" + dc.showContact().getMsgflag());
        }
        if(dc.showContact().getMsgflag()!=null){
            dc.addContact(new contact_elements(dc.showContact().getContact(),dc.showContact().getContactname(),dc.showContact().getMsgflag(),"1"));
            System.out.println("main activity" + dc.showContact().getContactname());
            System.out.println("main msg activity" + dc.showContact().getMsgflag());
            System.out.println("main msg activity" + dc.showContact().getMsgflag());
        }



        // Drawer Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation item class will open  based on NavigationItemSelectedListener item
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Alarm Pending
        //alarmPendingIntent = PendingIntent.getBroadcast(this, 1099, new Intent(ALARM_SET), 0);
        //sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SENT_SMS), 0);
        //deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        // Mobile Ads Initialization
        MobileAds.initialize(this, "ca-app-pub-3761913438322491~2414581767");

        // Go to app_bar view and build AdView
        View test1View = this.findViewById(R.id.app_bar);
        AdView adView =((AdView) findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        // Set MainActivity Text and Image View
        dest = (TextView) findViewById(R.id.dest);
        dist = (TextView) findViewById(R.id.dist);
        mode = (ImageView) findViewById(R.id.imageView5);
        radius = (TextView) findViewById(R.id.radius);
        remaining = (TextView) findViewById(R.id.remaining);
        //if(dc.showDistance().getDist()==null) {
            dist.setText("calculating \nDistance");
            remaining.setText("calculating \nRem Distance");
        //}
        /*else{
            dist.setText(dc.showDistance().getDist());
            System.out.println("set set");
            remaining.setText(dc.showDistance().getDist()+","+dc.showRADIUS().getRadius());
        }*/


        // Set Mode image based on Mode selected in setting
        if (dc.showMODE().getMODE() != null) {
            if (dc.showMODE().getMODE().contains("driving"))
                mode.setImageResource(R.drawable.car);
            if (dc.showMODE().getMODE().contains("train"))
                mode.setImageResource(R.drawable.train);
            if (dc.showMODE().getMODE().contains("bus"))
                mode.setImageResource(R.drawable.bus);

        }

        // Set Radius value
        if (dc.showRADIUS().getRadius() != null) {
            radius.setText(dc.showRADIUS().getRadius() + " km\nRadius");
        }


        //set Destination
        if(dc.showDetails().getDest()!=null){
            dest.setText((CharSequence) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1));
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Location Listener Functionality called when location changed
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                System.out.println(currentDateTimeString);


                // if Destination is not set then It will not call
                if(dc.showDetails().getDest()!=null) {

                    // Go string contains value either 'c' for alarm not set or 'u' for alarm set
                    String go = dc.showDetails().getDest().substring(dc.showDetails().getDest().length() - 1, dc.showDetails().getDest().length());
                    dist.setText(dc.showDistance().getDist()+" Km"+"\nDistance");
                    System.out.println(dist.getText());
                    System.out.println("value in dc"+dc.showDistance().getDist());
                    double d1 = Double.parseDouble(((dc.showDistance().getDist())))-Double.parseDouble(dc.showRADIUS().getRadius());
                    System.out.println(d1);
                    remaining.setText(Double.toString(d1)+" Km"+"\nRem Dist");
                    double d3 = Double.parseDouble(dc.showDistance().getDist());
                    double d4 = d1/d3;
                    d4 = d4*100;
                    int prog = (int) d4;
                    mProgress.setProgress(prog);
                    ((TextView) findViewById(R.id.textView1)).setText("   "+Integer.toString(prog)+"%"+"\n Remaining");

                    // if go equals 'c' then only it will call
                    // isNetworkAvailable() this shows when Internet available then it call
                    // ONLINE METHOD for DISTANCE CALCULATION

                    if (dc.showDetails().getDest() != null && isNetworkAvailable() && go.contentEquals("c")) {
                        // Set Destination
                        //check later
                        dest.setText((CharSequence) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1));

                        // String a and b gives lat and long of Destination.
                        String a = dc.showDetails().getLattitude();
                        String b = dc.showDetails().getLongitude();
                        String mode;
                        // if Mode value is null then it will set Driving as Default mode.
                        // check later
                        if (dc.showMODE().getMODE() != null) {
                            mode = dc.showMODE().getMODE();
                        } else {
                            mode = "mode=driving";
                        }
                        // calculate distance between destination and  current location using google map API.
                        String s = getDistance(location.getLatitude(), location.getLongitude(), Double.valueOf(a), Double.valueOf(b), mode);
                        System.out.println("location changed called");
                        System.out.println("value of s"+s);
                        // if distance comes Null then it call
                        if (s == null) {
                                s = getDistance(location.getLatitude(), location.getLongitude(), Double.valueOf(a), Double.valueOf(b), "mode=driving");
                            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        }

                        if (s != null) {
                            // Split distance ex 2,160 km into two parts integer 2160 and km
                            String[] sp = s.split("\\s+");
                            // check Alarm check value in rep string
                            String rep = dc.showDetails().getDest().substring(dc.showDetails().getDest().length() - 1, dc.showDetails().getDest().length());
                            // Remove ',' from Distance
                            String split = sp[0].replaceAll(",", "");
                            // Convert distance into double.
                            double l = Double.valueOf(split);
                            // if Distance in meter then divide it by 1000 to make it KM.
                            if (s.substring(s.length() - 1, s.length()).equals("m") && !s.substring(s.length() - 2, s.length() - 1).equals("k")) {
                                l = l / 1000;
                            }
                            // Save Distance in Database with current locations lat and long

                            System.out.println();
                            dc.addDistance(new saved_distance(String.valueOf(l), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                            //dc.addDetails(new saved_data_elements());
                            s = s + "\nDistance";
                            // Set Distance
                            System.out.println("dist set Alarm"+ s);
                            dist.setText(s);

                            int round = (int) ((l - Double.valueOf(dc.showRADIUS().getRadius()))*100.0);
                            double dv = round/100.0;
                            System.out.println("round"+dv);
                            // Set Remaining distance
                            System.out.println("rem dist set Alarm");
                            remaining.setText(String.valueOf(dv) + " km \nRem Distance");
                            d3 = Double.parseDouble(dc.showDistance().getDist());
                            d4 = dv/d3;
                            d4 = d4*100;
                            prog = (int) d4;
                            mProgress.setProgress(prog);
                            ((TextView) findViewById(R.id.textView1)).setText("   "+Integer.toString(prog)+"%"+"\n Remaining");


                            // If distance in meter then l should be 0 and Alarm should start
                            if (sp[1].contentEquals("m")) {
                                l = 0.0;
                            }
                            // Default radius or limit set as 1
                            double limit = 1.0;
                            // Set Limit if it is not null
                            if (dc.showRADIUS().getRadius() != null) {
                                limit = Double.valueOf(dc.showRADIUS().getRadius());
                            }
                            // Set Alarm when Alarm status is checked 'c' and distance is less than radius/limit
                            if (l <= limit && rep.contentEquals("c")) {
                                dist.setText(s);
                                remaining.setText(String.valueOf(dv) + " km \nRem Distance");

                                System.out.println("distText"+dist.getText());
                                set_alarm();
                                System.out.println("set alarm"+ dc.showDetails().getDest());
                                j = 1;
                                alarm = 1;
                                if(dc.showContact().getMsgflag().contains("0")){
                                    dc.addDetails(new saved_data_elements((String) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1) + "u", a, b));
                                }
                            }
                            // Send SMS when Alarm status is checked 'c', msg flag set and distance is less than radius/limit
                            if (l <= limit && rep.contentEquals("c") && dc.showContact().getMsgflag().contains("1")) {
                                // send sms
                                //send_sms();
                                k = 1;
                                System.out.println("send SMS"+ dc.showDetails().getDest());
                                //Set Alarm off image
                                ImageView img1 = (ImageView) findViewById(R.id.imageView6);
                                img1.setImageResource(R.drawable.alrmoff);
                                // Set Alarm status as unchecked 'u'
                                dc.addDetails(new saved_data_elements((String) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1) + "u", a, b));
                                System.out.println("send SMS"+ dc.showDetails().getDest());
                            }
                        }
                    }

                    // !isNetworkAvailable() this shows when Internet is not available then it call
                    // OFFLINE METHOD for DISTANCE CALCULATION

                    if (!isNetworkAvailable() && dc.showDistance() != null && dc.showDistance().getLongitude() != "0.0" && go.contentEquals("c")) {

                        // Set destination
                        dest.setText((CharSequence) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1));

                        String a = dc.showDetails().getLattitude();
                        String b = dc.showDetails().getLongitude();


                        float[] dist1 = new float[1];
                        // Find distance between last current location and current location
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.valueOf(dc.showDistance().getLattitude()), Double.valueOf(dc.showDistance().getLongitude()), dist1);
                        double d = dist1[0];
                        d = d / 1000;
                        System.out.println(Double.valueOf(dc.showDistance().getDist()) - d);

                        // Total Distance - (last current location and current location) > 0
                        int compare_val = Double.compare(Double.valueOf(dc.showDistance().getDist()) - d, 0.0);
                        System.out.println(compare_val);
                        if (compare_val > 0) {
                            double diff = Double.valueOf(dc.showDistance().getDist()) - d;
                            double l = diff;
                            l = l * 100;
                            int i = (int) l;
                            l = (double) i / 100;
                            dc.addDistance(new saved_distance(String.valueOf(diff), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                            dist.setText(String.valueOf(l + " km\nDistance"));
                            int round = (int) ((l - Double.valueOf(dc.showRADIUS().getRadius()))*100.0);
                            double dv = round/100.0;
                            System.out.println("round"+dv);
                            //double round = round(r);
                            remaining.setText(String.valueOf(dv) + " km \nRem Distance");
                            d3 = Double.parseDouble(dc.showDistance().getDist());
                            d4 = dv/d3;
                            d4 = d4*100;
                            prog = (int) d4;
                            mProgress.setProgress(prog);
                            ((TextView) findViewById(R.id.textView1)).setText("   "+Integer.toString(prog)+"%"+"\n Remaining");
                            String rep = dc.showDetails().getDest().substring(dc.showDetails().getDest().length() - 1, dc.showDetails().getDest().length());
                            double limit = 1.0;
                            System.out.println(Double.valueOf(dc.showRADIUS().getRadius()));
                            if (dc.showRADIUS().getRadius() != null) {
                                limit = Double.valueOf(dc.showRADIUS().getRadius());
                            }
                            if (diff <= limit && rep.contentEquals("c")) {
                                set_alarm();
                                j = 1;
                                alarm = 1;
                                if(dc.showContact().getMsgflag().contains("0")){
                                    dc.addDetails(new saved_data_elements((String) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1) + "u", a, b));
                                }
                            }
                            if (diff <= limit && rep.contentEquals("c")) {
                                //send_sms();
                                System.out.println("send SMS"+ dc.showDetails().getDest());
                                k = 1;
                                ImageView img1 = (ImageView) findViewById(R.id.imageView6);
                                img1.setImageResource(R.drawable.alrmoff);
                                dc.addDetails(new saved_data_elements((String) dc.showDetails().getDest().substring(0, dc.showDetails().getDest().length() - 1) + "u", a, b));
                                System.out.println("send SMS"+ dc.showDetails().getDest());
                            }
                        }
                    }
                }
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
                startActivity(i);
            }
        };

        // Allow SMS permission and Allow Location permission with Location request updates
        configure_button();
        System.out.println(dc.showContact().getMsgflag());

        // Check msgFLAG if its 1 then show MSG layout
        if(dc.showContact().getMsgflag()!=null){
            if(dc.showContact().getMsgflag().contains("1")){
                LinearLayout sms = (LinearLayout) findViewById(R.id.sms_status);
                sms.setVisibility(VISIBLE);
            }
        }

        // check msgflag if its 0 then Remove MSG layout
        if(dc.showContact().getMsgflag().contains("0")){
            LinearLayout sms = (LinearLayout) findViewById(R.id.sms_status);
            sms.setVisibility(GONE);
        }

        // If Destination is set then set view
        if(dc.showDetails().getDest()!=null) {
            String rep = dc.showDetails().getDest().substring(dc.showDetails().getDest().length() - 1, dc.showDetails().getDest().length());
            if (rep.contentEquals("u")) {
                System.out.println("calling uncheck");
                ImageView img1 = (ImageView) findViewById(R.id.imageView6);
                img1.setImageResource(R.drawable.alrmoff);
                LinearLayout lin = (LinearLayout) findViewById(R.id.start);
                lin.setVisibility(GONE);
                LinearLayout lin1 = (LinearLayout) findViewById(R.id.description);
                lin1.setVisibility(GONE);
                if(dc.showContact().getMsgflag().contains("0")){
                    LinearLayout sms = (LinearLayout) findViewById(R.id.sms_status);
                    sms.setVisibility(GONE);
                }
                TextView edit = (TextView) findViewById(R.id.editText);
                edit.setText("You Have Reached"+" "+dc.showDetails().getDest()+"\nSet Another Destination");
                edit.setVisibility(VISIBLE);
            }
            if (rep.contentEquals("c")) {
                System.out.println("calling check");
                ImageView img1 = (ImageView) findViewById(R.id.imageView6);
                img1.setImageResource(R.drawable.alarmon);
                LinearLayout lin = (LinearLayout) findViewById(R.id.start);
                lin.setVisibility(VISIBLE);
                LinearLayout lin1 = (LinearLayout) findViewById(R.id.description);
                lin1.setVisibility(VISIBLE);
                TextView edit = (TextView) findViewById(R.id.editText);
                edit.setVisibility(GONE);
            }
        }
        if(dc.showDetails().getDest()==null) {
            ImageView img1 = (ImageView) findViewById(R.id.imageView6);
            img1.setImageResource(R.drawable.alrmoff);
            LinearLayout lin = (LinearLayout) findViewById(R.id.start);
            lin.setVisibility(GONE);
            LinearLayout lin1 = (LinearLayout) findViewById(R.id.description);
            lin1.setVisibility(GONE);
            if(dc.showContact().getMsgflag().contains("0")){
                LinearLayout sms = (LinearLayout) findViewById(R.id.sms_status);
                sms.setVisibility(GONE);
            }
            TextView edit = (TextView) findViewById(R.id.editText);
            edit.setText("Set Destination to get a wake up Alarm call when you are going to reach Destination");
            edit.setVisibility(VISIBLE);
        }


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(MainActivity.this);
        ImageView img = (ImageView) findViewById(R.id.imageView10);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Toast.makeText(MainActivity.this, "Go to Setting for disable", Toast.LENGTH_SHORT).show();
            }
        });

        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);

// as 60 is max, we specified in the xml layout, 30 will be its half ?



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){

        System.out.println("permission 1");
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                System.out.println("permission 2");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            System.out.println("permission 3");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);}





        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }



    @Override
        public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.info) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=deepesh.travel.abhay.applayout&rdid=deepesh.travel.abhay.applayout \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if(id == R.id.setting){
            startActivity(new Intent(this, SettingTest.class));
        }
        else if(id==R.id.rate){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2, final String mode) {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     System.out.println("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat1 + "," + lon1 + "&destinations=" + lat2 + "," + lon2 +"&"+mode+"&sensor=false&units=metric");
                     URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat1 + "," + lon1 + "&destinations=" + lat2 + "," + lon2 +"&"+mode+"&sensor=false&units=metric");
                     final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                     conn.setRequestMethod("GET");
                     InputStream in = new BufferedInputStream(conn.getInputStream());
                     response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                     System.out.println(response[0]);
                     System.out.println(mode);

                    JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("rows");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("elements");
                    JSONObject steps = legs.getJSONObject(0);

                    JSONObject distance = steps.getJSONObject("distance");
                    System.out.println(distance);
                    parsedDistance[0] = distance.getString("text");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        return parsedDistance[0];
    }

    @Override
    public void onPlaceSelected(Place place) {

        final DatabaseConnectivity dc = new DatabaseConnectivity(this);
        //Log.i(TAG, "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.
        //place1 = place;
        //i = 1;
        LatLng newlocation = place.getLatLng();
        String lat = String.valueOf(newlocation.latitude);
        String lng = String.valueOf(newlocation.longitude);
        Toast.makeText(this, "Destination Selected", Toast.LENGTH_SHORT).show();

        dc.addDetails(new saved_data_elements((String) place.getName()+"c",lat,lng));
        dc.addDistance(new saved_distance("0.0","0.0","0.0"));


        LinearLayout lin = (LinearLayout) findViewById(R.id.start);
        lin.setVisibility(VISIBLE);
        LinearLayout lin1 = (LinearLayout) findViewById(R.id.description);
        lin1.setVisibility(VISIBLE);
        ImageView img1 = (ImageView) findViewById(R.id.imageView6);
        img1.setImageResource(R.drawable.alarmon);
        if(dc.showContact().getMsgflag().contains("1")){
            LinearLayout sms = (LinearLayout) findViewById(R.id.sms_status);
            sms.setVisibility(VISIBLE);
        }
        if(dc.showMODE().getMODE()==null) {
            dc.addMODE(new saved_MODE("mode=driving"));
        }
        if(dc.showRADIUS().getRadius()==null) {
            dc.addRADIUS(new saved_RADIUS("1"));
        }
        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {

        } else {

        }
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        smsSentReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Sent!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No Service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        smsDeliveredReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };
    }

    protected void onPause() {
        super.onPause();
    }


    public void send_sms() {
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        SmsManager sms = SmsManager.getDefault();
        if (dc.showContact().getContact() != null && dc.showMsg().getMsg() != null && dc.showContact().getMsgflag().contains("1")) {
            sms.sendTextMessage(dc.showContact().getContact(), null, dc.showMsg().getMsg(), sentPendingIntent, deliveredPendingIntent);
            Toast.makeText(this, "send sms", Toast.LENGTH_SHORT).show();

        }
    }

    public void set_alarm() {
        // Sets alarm for system time + 3 s and generates a notification
        //AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarm.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 30000, alarmPendingIntent);

        System.out.println("set alarm called");

        Uri alarm_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(this, alarm_uri);


        Long alarmTime = new GregorianCalendar().getTimeInMillis();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+100, PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        mp.start();
        System.out.println("Deepsh");

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package deepesh.travel.abhay.applayout;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingTest extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    static Intent intent ;
    static int i = 0;
    static int j=0;
    static int con_permission = 0;
    static GeneralPreferenceFragment g;
    String contact_name,contact_number;
    final DatabaseConnectivity dc= new DatabaseConnectivity(this);
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                System.out.println(preference.getKey());
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if(preference instanceof Preference){
                preference.setSummary(stringValue);
            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnableRuntimePermission();
        //i=0;
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sf.edit();
        /*if(dc.showContact().getContact()!=null){
            if(dc.showContact().getContact().contains("click on Turn on Switch to set contact number")){
                editor.putString("cname", "click on Turn on Switch to set contact name");
                editor.commit();
                editor.putString("cnumber", "click on Turn on Switch to set contact number");
                editor.commit();
            }
        }*/

        if(dc.showContact().getContactname()!=null) {
            if (dc.showContact().getContact().contains("click on Turn on Switch to set contact number")) {
                editor.putString("cname", dc.showContact().getContactname());
                editor.commit();
                editor.putString("cnumber", dc.showContact().getContact());
                editor.commit();
            }
            System.out.println("inside non null");
        }

        g =new GeneralPreferenceFragment();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String value = bundle.getString("key", "3");
            if (value.contains("1")) {
                System.out.println("key" + value);
                startContact();
            }
            if (value.contains("2")) {
                System.out.println("key" + value);
                editor.putString("cname", "click on Turn on Switch to set contact name");
                editor.commit();
                editor.putString("cnumber", "click on Turn on Switch to set contact number");
                editor.commit();
                dc.addContact(new contact_elements("click on Turn on Switch to set contact number", "click on Turn on Switch to set contact name", "0", "0"));
                getFragmentManager().beginTransaction().replace(android.R.id.content,
                        g).commit();
            }
        }
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                g).commit();
    }



    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //super.get
            final Activity s =super.getActivity();
            final DatabaseConnectivity dc = new DatabaseConnectivity(s);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            //startContact();
            System.out.println("start genreal"+j);
            Preference cname1 = (Preference) findPreference("cname");
            Preference cnumber1 = (Preference) findPreference("cnumber");
            cname1.setDefaultValue("click on Turn on Switch to set contact name");
            cnumber1.setDefaultValue("click on Turn on Switch to set contact number");
            cname1.setEnabled(false);
            cnumber1.setEnabled(false);

            final Preference Tutorial = (Preference) findPreference("Tutorial");
            Tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(GeneralPreferenceFragment.this.getActivity(), Tutorial.class);
                    startActivity(i);
                    return true;
                }

            });
            // Open Privacy policy class when click on privacy policy
            final Preference Privacy = (Preference) findPreference("Privacy");
            Privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(GeneralPreferenceFragment.this.getActivity(), privacyPolicy.class);
                    startActivity(i);
                    return true;
                }

            });

            final Preference Share = (Preference) findPreference("Share");
            Share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String sAux = "\nLet me recommend you this application\n\n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=deepesh.travel.abhay.applayout \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }
                    return true;
                }

            });


            final SwitchPreference somePreference = (SwitchPreference) findPreference("sms");
            if(dc.showContact().getContactname()!=null){
                if(dc.showContact().getContact().contains("click on Turn on Switch to set contact number")){
                    Preference cname = (Preference) findPreference("cname");
                    Preference cnumber = (Preference) findPreference("cnumber");
                    Preference smstext = (Preference) findPreference("sms_text");
                    cname.setEnabled(false);
                    cnumber.setEnabled(false);
                    smstext.setEnabled(false);
                    somePreference.setChecked(false);
                    System.out.println("click"+dc.showContact().getContactname());
                }
            }
            if(dc.showContact().getContactname()!=null){
                if(!dc.showContact().getContact().contains("click on Turn on Switch to set contact number")){
                    Preference cname = (Preference) findPreference("cname");
                    Preference cnumber = (Preference) findPreference("cnumber");
                    Preference smstext = (Preference) findPreference("sms_text");
                    cname.setEnabled(true);
                    cnumber.setEnabled(true);
                    smstext.setEnabled(true);
                    somePreference.setChecked(true);
                }
            }

            somePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    boolean checked = somePreference.isChecked();
                    if(con_permission == 1) {
                        if (checked) {
                            i = 1;
                            Intent intent = new Intent(GeneralPreferenceFragment.this.getActivity(), SettingTest.class);
                            intent.putExtra("key", "1");
                            startActivity(intent);
                        }
                        if (!checked) {
                            i = 0;
                            Intent intent = new Intent(GeneralPreferenceFragment.this.getActivity(), SettingTest.class);
                            intent.putExtra("key", "2");
                            startActivity(intent);
                        }
                    }
                    else{
                        Intent intent = new Intent(GeneralPreferenceFragment.this.getActivity(), SettingTest.class);
                        startActivity(intent);
                    }
                    return true;
                }

            });
            bindPreferenceSummaryToValue(findPreference("cname"));
            bindPreferenceSummaryToValue(findPreference("cnumber"));
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("key","3");
        startActivity(i);
    }
    public void startContact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 7);
    }
    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);
        final DatabaseConnectivity dc = new DatabaseConnectivity(this);

        switch (RequestCode) {

            case (7):
                if (ResultCode == RESULT_OK) {

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

                                //name.setText(TempNameHolder);

                                contact_name = TempNameHolder;
                                contact_number = TempNumberHolder;
                                dc.addContact(new contact_elements(TempNumberHolder, TempNameHolder, "1","0"));

                                //newIntent();
                                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
                                SharedPreferences.Editor editor = sf.edit();
                                System.out.println("contact called");
                                if(dc.showContact().getContact()!=null) {
                                    editor.putString("cname", dc.showContact().getContactname());
                                    editor.commit();
                                    editor.putString("cnumber", dc.showContact().getContact());
                                    editor.commit();
                                }
                            }
                        }

                    }
                    j=0;
                }
                else{
                    j=1;
                    System.out.println("Default called");
                    dc.addContact(new contact_elements("click on Turn on Switch to set contact number", "click on Turn on Switch to set contact name", "0","0"));
                    SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sf.edit();
                    if(dc.showContact().getContact()!=null) {
                        editor.putString("cname", dc.showContact().getContactname());
                        editor.commit();
                        editor.putString("cnumber", dc.showContact().getContact());
                        editor.commit();
                    }
                }
                //Intent intent = new Intent(this, SettingTest.class);
                //intent.putExtra("key","2");
                getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();
                break;
        }

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS))
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.READ_CONTACTS}, Setting.RequestPermissionCode);




        } else {

            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.READ_CONTACTS}, Setting.RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case Setting.RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    con_permission = 1;
                    //Toast.makeText(this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}

package deepesh.travel.abhay.applayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnectivity extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "NEW_DBS";
    // Contacts table name
    private static final String TABLE_NAME_1 = "DEST_DATA";
    private static final String TABLE_NAME_2 = "CUSTOM_MSG";
    private static final String TABLE_NAME_3 = "CONTACT";
    private static final String TABLE_NAME_4 = "DISTANCE";
    private static final String TABLE_NAME_5 = "RADIUS";
    private static final String TABLE_NAME_6 = "MODE";
    private static final String CONTACTNAME= "contactname";
    private static final String MSGFLAG= "msgflag";
    private static final String CONFLAG= "conFLag";
    private static final String OLD_LAT= "Oldlat";
    private static final String OLD_LON = "Oldlon";
    private static final String DISTANCE = "distance";
    private static final String RAD = "Radius";
    private static final String MODEOFTRAVEL = "mode_of_travel";
    // Shops Table Columns names
    private static final String DEST = "destination";
    private static final String CONTACT = "contact";
    private static final String CUS_MSG = "custom_msg";
    private static final String LAT = "lattitude";
    private static final String LONG = "longitude";


    public DatabaseConnectivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String SAVED_DATA = "CREATE TABLE " + TABLE_NAME_1 + "("
                + DEST + " VARCHAR(20)," + LAT + " VARCHAR(20) ," + LONG + " VARCHAR(20) )";

        String CUSTOM_MSG = "CREATE TABLE " + TABLE_NAME_2 + "("
                + CUS_MSG + " VARCHAR(20)" + ")";

        String CONT = "CREATE TABLE " + TABLE_NAME_3 + "("
                + CONTACT + " VARCHAR(20)," + CONTACTNAME + " VARCHAR(20) ," + MSGFLAG + " VARCHAR(20) ," + CONFLAG + " VARCHAR(20)  )";

        String DIST = "CREATE TABLE " + TABLE_NAME_4 + "("
                + DISTANCE + " VARCHAR(20)," + OLD_LAT + " VARCHAR(20) ," + OLD_LON + " VARCHAR(20) )";

        String RADIUS = "CREATE TABLE " + TABLE_NAME_5 + "("
                + RAD + " VARCHAR(20)" + ")";

        String MODE = "CREATE TABLE " + TABLE_NAME_6 + "("
                + MODEOFTRAVEL + " VARCHAR(20)" + ")";

        db.execSQL(SAVED_DATA);
        db.execSQL(CUSTOM_MSG);
        db.execSQL(CONT);
        db.execSQL(DIST);
        db.execSQL(RADIUS);
        db.execSQL(MODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    SQLiteDatabase db;
    //db functions for Table_2

    public void addMsg(cus_msg_elements ele) {

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_2, null, null);
        ContentValues values = new ContentValues();
        values.put(CUS_MSG, ele.getMsg());

        db.insert(TABLE_NAME_2, null, values);
        db.close(); // Closing database connection
    }

    public cus_msg_elements showMsg() {
        Cursor cursor = null;
        try {
            cus_msg_elements ele = new cus_msg_elements();
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
            if (cursor.moveToFirst()) {
                ele = new cus_msg_elements(cursor.getString(0));
            }
            return ele;
        }
        finally {
            cursor.close();
        }
    }


    //db functions for Table_1

    public void addDetails(saved_data_elements ele) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_1, null, null);
        ContentValues values = new ContentValues();
        values.put(DEST, ele.getDest());
        values.put(LAT, ele.getLattitude());
        values.put(LONG, ele.getLongitude());

        db.insert(TABLE_NAME_1, null, values);
        db.close();
    }

    public saved_data_elements showDetails() {
        Cursor cursor = null;
        try {
            saved_data_elements ele = new saved_data_elements();
            db = this.getReadableDatabase();
            cursor = db.rawQuery("Select * from " + TABLE_NAME_1, null);
            if (cursor.moveToFirst()) {
                ele.setDest(cursor.getString(0));
                ele.setLattitude(cursor.getString(1));
                ele.setLongitude(cursor.getString(2));
            }
            return ele;
        }
        finally {
            cursor.close();
        }

    }

    //db functions for table 3

    public void addContact(contact_elements ele) {

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_3, null, null);
        ContentValues values = new ContentValues();
        values.put(CONTACT, ele.getContact());
        values.put(CONTACTNAME, ele.getContactname());
        values.put(MSGFLAG, ele.getMsgflag());
        values.put(CONFLAG, ele.getConflag());

        db.insert(TABLE_NAME_3, null, values);
        db.close();
    }

    public contact_elements showContact() {Cursor cursor = null;
        try {
            contact_elements ele = new contact_elements();
            db = this.getReadableDatabase();
            cursor = db.rawQuery("Select * from " + TABLE_NAME_3, null);
            if (cursor.moveToFirst()) {
                ele.setContact(cursor.getString(0));
                ele.setContactname(cursor.getString(1));
                ele.setMsgflag(cursor.getString(2));
                ele.setConflag(cursor.getString(3));
            }
            return ele;
        }
        finally {
            cursor.close();
        }
    }

    // db for table4
    public void addDistance(saved_distance ele) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_4, null, null);
        ContentValues values = new ContentValues();
        values.put(DISTANCE, ele.getDist());
        values.put(OLD_LAT, ele.getLattitude());
        values.put(OLD_LON, ele.getLongitude());

        db.insert(TABLE_NAME_4, null, values);
        db.close();
    }

    public saved_distance showDistance() {
        Cursor cursor = null;
        try {
            saved_distance ele = new saved_distance();
            db = this.getReadableDatabase();
            cursor = db.rawQuery("Select * from " + TABLE_NAME_4, null);
            if (cursor.moveToFirst()) {
                ele.setDist(cursor.getString(0));
                ele.setLattitude(cursor.getString(1));
                ele.setLongitude(cursor.getString(2));
            }
            return ele;
        }
        finally {
            cursor.close();
        }

    }
    public void addRADIUS(saved_RADIUS ele) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_5, null, null);
        ContentValues values = new ContentValues();
        values.put(RAD, ele.getRadius());

        db.insert(TABLE_NAME_5, null, values);
        db.close();
    }

    public saved_RADIUS showRADIUS() {
        Cursor cursor = null;
        try {
            saved_RADIUS ele = new saved_RADIUS();
            db = this.getReadableDatabase();
            cursor = db.rawQuery("Select * from " + TABLE_NAME_5, null);
            if (cursor.moveToFirst()) {
                ele.setRadius(cursor.getString(0));
            }
            cursor.close();
            return ele;
        }
        finally {
            cursor.close();
        }
    }

    public void addMODE(saved_MODE ele) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_6, null, null);
        ContentValues values = new ContentValues();
        values.put(MODEOFTRAVEL, ele.getMODE());

        db.insert(TABLE_NAME_6, null, values);
        db.close();
    }

    public saved_MODE showMODE() {
        saved_MODE ele = new saved_MODE();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME_6, null);
        if (cursor.moveToFirst()) {
            ele.setMODE(cursor.getString(0));
        }
        return ele;
    }

}

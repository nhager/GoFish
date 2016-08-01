package com.example.nh612u.gofish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by nh612u on 7/26/2016.
 * Used to modify and create the DB
 * Can also be used to drop tables
 */
public class DBHelper  extends SQLiteOpenHelper {


    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_FIRST = "firstname";
        public static final String COLUMN_NAME_LAST = "lastname";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ZIP = "zip";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ROLE = "role";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_FIRST + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_LAST + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ZIP + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_STATE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ROLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_EMAIL + TEXT_TYPE +

            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GoFish.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
       // onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public long addDataUser(SQLiteDatabase db, String first, String last, String address,
                            String city, String state, String Zip, String phone,
                            String email, String pass, String role){

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_FIRST, first);
        values.put(FeedEntry.COLUMN_NAME_LAST, last);
        values.put(FeedEntry.COLUMN_NAME_ADDRESS, address);
        values.put(FeedEntry.COLUMN_NAME_CITY, city);
        values.put(FeedEntry.COLUMN_NAME_PHONE, phone);
        values.put(FeedEntry.COLUMN_NAME_STATE, state);
        values.put(FeedEntry.COLUMN_NAME_ZIP, Zip);
        values.put(FeedEntry.COLUMN_NAME_ROLE, role);
        values.put(FeedEntry.COLUMN_NAME_PASSWORD, pass);
        values.put(FeedEntry.COLUMN_NAME_EMAIL, email);
        String select = "SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_FIRST
                + "=" + "'" +first +"' " + "AND " + FeedEntry.COLUMN_NAME_LAST + "=" + "'" + last +
                "'";
        Cursor c = db.rawQuery(select, null);
        if(c.getCount() != 0){
            return -1;
        }
// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);
        c.close();
        select = "SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_FIRST
                + "=" + "'" +first +"' " + "AND " + FeedEntry.COLUMN_NAME_LAST + "=" + "'" + last +
                "'";
        c = db.rawQuery(select, null);
        c.moveToFirst();
        Log.wtf("ID Number", c.getString(c.getColumnIndex(FeedEntry._ID)));
        return newRowId;
    }

    public Cursor getData(SQLiteDatabase db, String statement){
        Cursor c = db.rawQuery(statement, null);
        c.moveToFirst();
        Log.wtf("Returned tuplets", Integer.toString(c.getCount()));
        return c;
    }
    public int deleteUser(SQLiteDatabase db, String first, String last){
        String select = "DELETE FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_FIRST
                + "=" + "'" +first +"' " + "AND " + FeedEntry.COLUMN_NAME_LAST + "=" + "'" + last +
                "'";
        db.rawQuery(select, null).moveToFirst();
        Log.wtf("Testing: ", select);
        select = "SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_FIRST
                + "=" + "'" +first +"' " + "AND " + FeedEntry.COLUMN_NAME_LAST + "=" + "'" + last +
                "'";
        Log.wtf("Testing: ", select);
        Cursor c = db.rawQuery(select, null);
        if(c.getCount() == 0){
            c.close();
            return 1;
        }else{
            c.close();
            return -1;
        }

    }

}

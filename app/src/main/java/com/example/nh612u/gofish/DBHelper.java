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
 */
public class DBHelper  extends SQLiteOpenHelper {


    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = "userid ";
        public static final String COLUMN_NAME_FIRST = "firstname";
        public static final String COLUMN_NAME_LAST = "lastname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_FIRST + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_LAST + TEXT_TYPE + COMMA_SEP +
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
    public int addData(SQLiteDatabase db, String ID, String first, String last, String email){

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_USER_ID, ID);
        values.put(FeedEntry.COLUMN_NAME_FIRST, first);
        values.put(FeedEntry.COLUMN_NAME_LAST, last);
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
        return 1;
    }

    public String getData(SQLiteDatabase db){
        String sortOrder =
                FeedEntry.COLUMN_NAME_USER_ID + " DESC";
        String[] args = {"nate"};
        String select = "SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COLUMN_NAME_FIRST
                + "=" + "'nate'";
        Cursor c = db.rawQuery(select, null);
        c.moveToFirst();
        Log.wtf("Returned tuplets", Integer.toString(c.getCount()));
        return c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_LAST)); //c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_FIRST));
    }

}

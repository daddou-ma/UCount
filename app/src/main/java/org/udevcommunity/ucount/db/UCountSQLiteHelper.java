package org.udevcommunity.ucount.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import com.csvreader.*;

import java.io.File;
import java.io.FileWriter;


import java.sql.Time;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Taym on 07/11/2015.
 */
public class UCountSQLiteHelper extends SQLiteOpenHelper {

    static Context context = null;
    private static final int DATABASE_VERSION = 1;  // Database Version
    private static final String DATABASE_NAME = "UCount";     // Database Name

    public UCountSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL statement to create ucount table
        String CREATE_COUNT_TABLE = "CREATE TABLE ucount ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Action INTEGER, " +
                "Time TIME DEFAULT CURRENT_DATE, " +
                "Count INTEGER )";

        // create ucount table
        db.execSQL(CREATE_COUNT_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table(ucount) table if existed
        db.execSQL("DROP TABLE IF EXISTS ucount");

        // create new ucount table
        this.onCreate(db);

    }

    //---------------------------------------------------------------------


    /*
    add,delete,update,get function
    */
// Ucount table name
    private static final String TABLE_UCount = "ucount";

    // ucount Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ACTION = "action";
    private static final String KEY_TIME = "time";
    private static final String KEY_COUNT = "count";

    private static final String[] COLUMNS = {KEY_ID, KEY_ACTION, KEY_TIME, KEY_COUNT};


    // Add new Record to the database as object
    public void addInfo(EventAction info) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, Integer.toString(info.getAction())); // get mode
        values.put(KEY_TIME, info.getTime().toString()); // get Day as string
        values.put(KEY_COUNT, Integer.toString(info.getCount())); // get number

        // 3. insert
        db.insert(TABLE_UCount, null, values);

        // 4. close
        db.close();
    }


    // Get All Infos
    public List<EventAction> getAllInfos() {
        List<EventAction> infos = new LinkedList<EventAction>();

        // 1. build the query
        String query = "SELECT  * FROM ucount ";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build DayInfo and add it to list
        EventAction info = null;
        if (cursor.moveToFirst()) {
            do {
                info = new EventAction();
                info.setId(Integer.parseInt(cursor.getString(0)));
                info.setAction(Integer.parseInt(cursor.getString(1)));
                info.setTime(Time.valueOf(cursor.getString(2)));
                info.setCount(Integer.parseInt(cursor.getString(3)));

                // Add info to infos
                infos.add(info);
            } while (cursor.moveToNext());
        }

        // return infos
        return infos;
    }

    public EventAction getLastline() {
        // 1. build the query
        String query = "SELECT * FROM ucount ORDER BY id DESC LIMIT 1 ";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build DayInfo and add it to list
        EventAction info = info = new EventAction(0, 0, new Time(System.currentTimeMillis()), 0);
        if (cursor.moveToFirst()) {
            info.setId(Integer.parseInt(cursor.getString(0)));
            info.setAction(Integer.parseInt(cursor.getString(1)));
            info.setTime(Time.valueOf(cursor.getString(2)));
            info.setCount(Integer.parseInt(cursor.getString(3)));
        }

        // return info
        return info;
    }

    // Deleting the database
    public void delete_db() {

        context.deleteDatabase(DATABASE_NAME);
    }

    // Export the database into "Ucount_Database.csv" in th SDCard
    public void expoortDB() {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Ucount_Database.csv");
        try {
            file.createNewFile();
            CsvWriter csvwrite = new CsvWriter(new FileWriter(file), ';');
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM ucount", null);

            for(int i=0; i < 4; i++)
                csvwrite.write(curCSV.getColumnNames()[i]);

            csvwrite.endRecord();
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
                for(int i=0; i < 4; i++)
                {
                    csvwrite.write(arrStr[i],false);
                }
                csvwrite.endRecord();
            }
            csvwrite.close();

            curCSV.close();

        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

    }
}



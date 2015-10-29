/** Analytics Mode
 * DataBaseOpenHelper.java
 *
 * Containing the main activity (main class)
 *
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 */

package org.udevcomminty.utasbihcharts.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * UTasbih
 *
 * Analytics Activity
 *
 * @package : org.udevcommunity.utasbihcharts.database
 * @author :  UDevCommunity <Contact@UDevCommunity.com>
 * @license :  دبر راسك بالكود
 * @link : https://github.com/ztickm/UTasbih
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper{

    //database name,version and only one table
    final static String NAME = "tasbih_db";
    final static Integer VERSION = 1;
    public final static String TABLE_NAME = "tasabih";

    //database columns
    public final static String _ID = "_id";
    public final static String CURRENT_DATE = "current_date";
    public final static String TASBIH_MODE = "mode";
    public final static String TASBIH_COUNT = "tasbih_count";

    //create table query
    final private  String CREATE_CMD =

            "CREATE TABLE "+TABLE_NAME+" (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CURRENT_DATE+" TEXT NOT NULL,"
                    + TASBIH_MODE + " INTEGER NOT NULL, "
                    + TASBIH_COUNT+"  INTEGER )";

    final private Context mContext;

    //data base open helper constructor
    public DataBaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    //create DB and TABLE tasbih
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_CMD);

    }

    // update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //TODO
    }

    /**
     * getData
     *
     *  read data from database by tasbih mode
     *
     *
     * @param : tasbihmode, field
     * @expectedException : none.
     * @return Cursor: database cursor after executing query
     * @link  https://github.com/ztickm/UTasbih
     */
    public Cursor getData(int value, String field){

        SQLiteDatabase db = this.getReadableDatabase();
        //select query based on tasbihmode value
        Cursor res =  db.rawQuery( "select * from " +TABLE_NAME+" where "+field+"='"+value+"'", null );
        return res;
    }

    //delete table

    /**
     * deleteDatabase
     *
     * delete database Table
     *
     *
     * @param : none
     * @expectedException : none.
     * @return void
     * @link  https://github.com/ztickm/UTasbih
     */
    void deleteDatabase() {
        mContext.deleteDatabase(TABLE_NAME);
    }


    /**
     * closeDataBase
     *
     *  close database after executing queries
     *
     *
     * @param : tasbihmode, field
     * @expectedException : none.
     * @return void
     * @link  https://github.com/ztickm/UTasbih
     */
    public void closeDataBase(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.close();

    }

}

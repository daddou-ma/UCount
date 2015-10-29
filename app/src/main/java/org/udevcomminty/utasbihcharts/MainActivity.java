/** Analytics mode
 * MainActivity.java
 *
 * Containing the main activity (main class)
 *
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 */

package org.udevcomminty.utasbihcharts;

import org.udevcomminty.utasbihcharts.DataBase.DataBaseOpenHelper;
import org.udevcomminty.utasbihcharts.Graphs.AnalyticsActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;


/**
 * UTasbih
 *
 * Main Activity
 *
 * @package :
 * @author :  UDevCommunity <Contact@UDevCommunity.com>
 * @license :  دبر راسك بالكود
 * @link : https://github.com/ztickm/UTasbih
 */

public class MainActivity extends ActionBarActivity {

    //Database helper
    private SQLiteDatabase mDB = null;
    private DataBaseOpenHelper mDbHelper;

    //Different tasbih modes
    private final static int MODE_SALAT = 1;
    private final static int MODE_FREE1 = 2;
    private final static int MODE_FREE2 = 3;
    private final static int MODE_FREE3 = 4;
    private final static int MODE_FREE4 = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a new DataBaseHelper
        mDbHelper = new DataBaseOpenHelper(this);

        // get the underlying database for writing
        mDB = mDbHelper.getWritableDatabase();

        // start with an empty database
        clearAll();

        //insert 30 random entries for each mode
        for (int i=0;i<30;i++) {

            insertTasbih(MODE_SALAT);
            insertTasbih(MODE_FREE1);
            insertTasbih(MODE_FREE2);
            insertTasbih(MODE_FREE3);
            insertTasbih(MODE_FREE4);
        }

        //use the button to open the Analytics activity
        Button viewAnalytics = (Button) findViewById(R.id.viewAnalyticsButton);
        viewAnalytics.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                             //move to the AnalyticsActivity
                                                 Intent analyticsActivity = new Intent(MainActivity.this, AnalyticsActivity.class);
                                                 startActivity(analyticsActivity);
                                             }
                                         }
        );
    }

    //insert several tasabih in both modes
    /**
     * insertTasbih.
     *
     *
     *
     * @param : tasbihmode.
     * @expectedException : none.
     * @return void.
     * @link  https://github.com/ztickm/UTasbih
     */
    private void insertTasbih(int tasbihmode) {

        int randInt;

        //get current date & time
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String TodayDate = df.format(new java.util.Date());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //generate random number for tasbih count
        if (tasbihmode == 1)  randInt = new Random().nextInt(5) + 1;
        else   randInt = new Random().nextInt(10) + 1;

        //set the content to store in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDbHelper.CURRENT_DATE, TodayDate);
        contentValues.put(mDbHelper.TASBIH_MODE, tasbihmode);
        contentValues.put(mDbHelper.TASBIH_COUNT, randInt);

        //insert values in the database
        mDB.insert(mDbHelper.TABLE_NAME, null, contentValues);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Delete all database entries
    /**
     * clearAll
     *
     * delete items from database.
     *
     * @param : none.
     * @expectedException : none.
     * @return void.
     * @link  https://github.com/ztickm/UTasbih
     */
    private void clearAll() {

        mDB.delete(mDbHelper.TABLE_NAME, null, null);
    }
}

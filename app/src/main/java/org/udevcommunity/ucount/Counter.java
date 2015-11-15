/**
 * Counter.java
 *
 * Counter Activity
 *
 * @author :   UDevCommunity <Contact@UDevCommunity.com>
 */

package org.udevcommunity.ucount;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Vibrator;

import org.udevcommunity.ucount.db.EventAction;
import org.udevcommunity.ucount.db.UCountSQLiteHelper;

import java.sql.Time;

/**
 * Counter
 *
 * Counter Activity
 *
 * @package :
 * @author :  UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
public class Counter extends Activity
{
    int mode;
    int counter = 0;                // counter used to stock the number of tasbih
    TextView counterView = null;       // TextView of the counter 'used to display the number in counter'
    LinearLayout principalLayout = null;   // principal layout 'used to set the color when rich 33,66,99,100 etc ...
    Vibrator vibr_tasbih = null;  // create vibr_tasbih objet from vibrator class

    public final String PREF_NAME = "UCountPref";

    UCountSQLiteHelper database = null;  // Database

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        // getting Views from the resources
        counterView = (TextView) findViewById(R.id.counter);
        principalLayout = (LinearLayout) findViewById(R.id.layoutCounter);
        vibr_tasbih = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        database = new UCountSQLiteHelper(this);


        // initialize the activity according to the mode
        initActivity();

        // onTouch listener for the Incrementing or decrementing
        //increment (left to right)  decrementing (right to left)
        principalLayout.setOnTouchListener(new View.OnTouchListener() {

            private int min_distance = 150;
            private float downX, downY, upX, upY;
            View v;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                this.v = v;
                switch (event.getAction()) { // Check vertical and horizontal touches
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        //HORIZONTAL SCROLL
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > min_distance) {
                                // left or right
                                if (deltaX < 0) {
                                    incCounterBasic(+1);
                                    return true;
                                }
                                if (deltaX > 0) {
                                    incCounterBasic(-1);
                                    return true;
                                }
                            } else {
                                //not long enough swipe...
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }


    // If we exit the activity
    @Override
    public void onStop()
    {
        super.onStop();

    }

    public void initActivity()
    {
        this.counterView.setText(Integer.toString(this.database.getLastline().getCount()));
        counter =this.database.getLastline().getCount();
    }

    /**
     * incCounterBasic.
     *
     * incCounterBasic.
     *
     * @param : none.
     * @expectedException : none.
     * @return void.
     * @link  https://github.com/ztickm/UTasbih
     */

    //
    public void incCounterBasic(int action)
    {
        if (!(this.counter == 0 && action == -1)) {
            if ((this.counter % 100) == 0 && this.counter > 0) {
                vibrate(500);
            }
            if (action == 1) {
                // Incrementing the counter
                this.counter++;
                database.addInfo(new EventAction(0, action, new Time(System.currentTimeMillis()), counter)); // add 1 to database
            } else {
                this.counter--;
                database.addInfo(new EventAction(0, action, new Time(System.currentTimeMillis()), counter)); // sub 1 to database
            }
            // Set the new value of counter to the Textview "counterView"
            this.counterView.setText(Integer.toString(this.counter));
        }
    }
    void vibrate(int delay)
    {
        SharedPreferences settings;
        settings = getSharedPreferences(PREF_NAME, 0);
        if(settings.getBoolean("vibration", false))
        {
            vibr_tasbih.vibrate(delay);
        }
    }
}

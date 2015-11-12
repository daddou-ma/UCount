/**
 * MainActivity.java
 *
 * Containing the main activity (main class)
 *
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 */
package org.udevcommunity.ucount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.udevcommunity.ucount.db.*;

import java.util.List;

/**
 * MainActivity
 *
 * The main activity of the application
 *
 * @package :
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
public class MainActivity extends AppCompatActivity
{
    UCountSQLiteHelper database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Go = (Button) findViewById(R.id.Go); // a test button to go to Counter activity
        Button Setting = (Button) findViewById(R.id.button); // a test button to go to Counter activity
        TextView textView = (TextView) findViewById(R.id.textView);

        database = new UCountSQLiteHelper(this);

        // Getting data from the DataBase as object (will be used for the graph)
        List<EventAction> table = database.getAllInfos();
        for (int i = 0; i < table.size(); i++)  textView.setText(textView.getText() + table.get(i).toString());

        // listener for the test Button 'Go' to Open Counter Activity
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening the Counter Activity
                Intent counterActivity = new Intent(MainActivity.this, Counter.class);
                startActivity(counterActivity);
            }
        });

        // listener for the test Button to Open Settings Activity
        Setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent settingsActivity = new Intent(MainActivity.this, Setting.class);
                startActivity(settingsActivity);
            }
        });
    }

}

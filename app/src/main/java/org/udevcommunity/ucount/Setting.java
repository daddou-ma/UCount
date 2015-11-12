package org.udevcommunity.ucount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


import org.udevcommunity.ucount.db.*;

/**
 * Created by Taym on 09/11/2015.
 */
public class Setting extends Activity {

    Switch vibration;
    Button delete =null;
    Button ixport =null;
    UCountSQLiteHelper mydatabase = null;

    public final String PREF_NAME = "UCountPref";

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        delete = (Button) findViewById(R.id.delete);
        ixport = (Button) findViewById(R.id.ixport);
        vibration = (Switch) findViewById(R.id.vibration);

        mydatabase = new UCountSQLiteHelper(this);

        settings = getSharedPreferences(PREF_NAME, 0);
        editor = settings.edit();

        boolean vibrate = true;
        try {
            vibrate = settings.getBoolean("vibration", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        vibration.setChecked(vibrate);

        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    editor.putBoolean("vibration", true);
                    editor.commit();
                }
                else
                {
                    editor.putBoolean("vibration", false);
                    editor.commit();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                /* Alert Dialog Code Start*/
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Event information"); //Set Alert dialog title here
                alert.setMessage("Warning : you want realy to delete database ?"); //Message here

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        try{
                            mydatabase.delete_db();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
       /* Alert Dialog Code End*/
            }// End of onClick(View v)

        });

        ixport.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Exporte the database
                mydatabase.expoortDB();
            }
        });

    }
}

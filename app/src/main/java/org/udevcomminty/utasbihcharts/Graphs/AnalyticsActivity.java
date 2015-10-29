/** Analytics mode
 * AnalyticsActivity.java
 *
 * Displaying both graphs for each tasbih mode
 *
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 */

package org.udevcomminty.utasbihcharts.Graphs;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.udevcomminty.utasbihcharts.DataBase.DataBaseOpenHelper;
import org.udevcomminty.utasbihcharts.R;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * UTasbih
 *
 * Analytics Activity
 *
 * @package : org.udevcommunity.utasbihcharts.graphs
 * @author :  UDevCommunity <Contact@UDevCommunity.com>
 * @license :  دبر راسك بالكود
 * @link : https://github.com/ztickm/UTasbih
 */

public class AnalyticsActivity  extends ActionBarActivity {

    //DataBase helper
    private DataBaseOpenHelper mDbHelper;

    //tasbih modes
    private final static int MODE_SALAT = 1;
    private final static int MODE_FREE1 = 2;
    private final static int MODE_FREE2 = 3;
    private final static int MODE_FREE3 = 4;
    private final static int MODE_FREE4 = 5;

    private final static int DATASET_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics);

        //read data from database (count), each mode in a 2D Arraylist:
        //Date & Count
        ArrayList<ArrayList<String>> salahList = getTasbihList(MODE_SALAT,DataBaseOpenHelper.TASBIH_MODE);
        ArrayList<ArrayList<String>> freemode1List = getTasbihList(MODE_FREE1,DataBaseOpenHelper.TASBIH_MODE);
        ArrayList<ArrayList<String>> freemode2List = getTasbihList(MODE_FREE2,DataBaseOpenHelper.TASBIH_MODE);
        ArrayList<ArrayList<String>> freemode3List = getTasbihList(MODE_FREE3,DataBaseOpenHelper.TASBIH_MODE);
        ArrayList<ArrayList<String>> freemode4List = getTasbihList(MODE_FREE4,DataBaseOpenHelper.TASBIH_MODE);

        //define a BarChart for Salat tasbih Mode
        BarChart salatTasbihChart  = (BarChart) findViewById(R.id.salatTasbihChart);

        // set the Xaxis Values and the chart data : xAxis date, data: tasbih count
        BarData salatData = new BarData(getXAxisValues(salahList.get(0)),getDataSet(salahList.get(1)) );
        salatTasbihChart.setData(salatData);
        salatTasbihChart.setDescription("تسابيح الصلاة");

        //refresh BarChart
        salatTasbihChart.invalidate();

        //define a LineChart for Free tasbih mode
        LineChart freeModeChart = (LineChart) findViewById(R.id.freeModeTasbihChart);
        //set the Lines for each mode
        freeModeChart.setData(getLineData(freemode1List.get(1),freemode2List.get(1),freemode3List.get(1),freemode4List.get(1),
                freemode1List.get(0)));
        freeModeChart.invalidate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * getTasbihList
     *
     *  read data from database by tasbih mode
     *
     *
     * @param : tasbihmode, field
     * @expectedException : none.
     * @return 2D ArrayList: containing the date and count for each tasbih action
     * @link  https://github.com/ztickm/UTasbih
     */
    private ArrayList<ArrayList<String>> getTasbihList (int tasbihmode,String field){

        //Data List size: 2 columns
        int listSize = 2;

        //index of columns to use with the database cursor
        int DATE_INDEX = 1;
        int COUNT_INDEX = 3;

        //init the 2D ArrayList
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>(listSize);
        for(int i = 0; i < listSize; i++)  {
            list.add(new ArrayList<String>());
        }

        //get Data from Database
        mDbHelper = new DataBaseOpenHelper(this);
        //get dataBase Cursor, make a query
        Cursor cursor = mDbHelper.getData(tasbihmode,field);

        //while cursor move get date and count of tasbih
        // put them in the list
        if (cursor.moveToFirst())
        {
            do
            {
                list.get(0).add(cursor.getString(DATE_INDEX));
                list.get(1).add(cursor.getString(COUNT_INDEX));
            }
            while (cursor.moveToNext());
        }
        //close cursor
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        //close database
        mDbHelper.closeDataBase();
        return list;
    }

    /**
    * getDataSet
    *
    *  return a list of tasbih counts and
    *
    *
    * @param : salahList
    * @expectedException : none.
    * @return ArrayList: containing the dataSet for BarChart
    * @link  https://github.com/ztickm/UTasbih
    */

    private ArrayList<BarDataSet> getDataSet(ArrayList<String> salahList) {

        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int i=0;i<salahList.size();i++) {

            //Convert String values of the List to flaot
            String tasbihValue = salahList.get(i).toString();
            Float tasbihEntry= Float.parseFloat(tasbihValue);
            BarEntry entry = new BarEntry(tasbihEntry,i);
            valueSet1.add(entry);

        }

       //create BarChart data set with the values extracted from the tasbih list
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "تسابيح");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }
    /**
     * getXAxisValues
     *
     *  return a list of dates as Xvalues for BarChart
     *
     *
     * @param : xValues
     * @expectedException : none.
     * @return ArrayList: containing the dataSet xValues for BarChart
     * @link  https://github.com/ztickm/UTasbih
     */
    private ArrayList<String> getXAxisValues(ArrayList<String> xValues) {

        ArrayList<String> xAxis = new ArrayList<>();
        for (int i=0;i<xValues.size();i++) {
            xAxis.add(xValues.get(i));
        }
        return xAxis;
    }
    /**
     * getXAxisValues
     *
     *  return a list of dates as Xvalues for BarChart
     *
     *
     * @param : 4 lists of counts for each mode and 1 list for dates
     * @expectedException : none.
     * @return LineData: line chart curves
     * @link  https://github.com/ztickm/UTasbih
     */

    private LineData getLineData(ArrayList<String> freemode1List, ArrayList<String> freemode2List,
            ArrayList<String> freemode3List, ArrayList<String> freemode4List,ArrayList<String> date){

        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp3 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp4 = new ArrayList<Entry>();


        ArrayList<String> xVals = new ArrayList<String>();
        //Free modes lists can have different size, for now we assume the 4 modes
        // have equal lists size
        for (int i=0;i<freemode1List.size();i++) {

            //convert each item of list to string then to float
            String freemode1Value = freemode1List.get(i).toString();
            String freemode2Value = freemode2List.get(i).toString();
            String freemode3Value = freemode3List.get(i).toString();
            String freemode4Value = freemode4List.get(i).toString();

            Float freeMode1Entry= Float.parseFloat(freemode1Value);
            Float freeMode2Entry= Float.parseFloat(freemode2Value);
            Float freeMode3Entry= Float.parseFloat(freemode3Value);
            Float freeMode4Entry= Float.parseFloat(freemode4Value);

            //Make a new entry and assigned to a Xaxis value (i)
            Entry freeMode1Point = new Entry(freeMode1Entry, i);
            Entry freeMode2Point = new Entry(freeMode2Entry, i);
            Entry freeMode3Point = new Entry(freeMode3Entry, i);
            Entry freeMode4Point = new Entry(freeMode4Entry, i);

            //add each point to its specific line mode
            valsComp1.add(freeMode1Point);
            valsComp2.add(freeMode2Point);
            valsComp3.add(freeMode3Point);
            valsComp4.add(freeMode4Point);

            //xVals for date
            xVals.add(date.get(i).toString());
        }


        //set a line data set for each mode by the lists created above
        LineDataSet setComp1 = new LineDataSet(valsComp1, "free Mode 1");
        LineDataSet setComp2 = new LineDataSet(valsComp2, "free Mode 2");
        LineDataSet setComp3 = new LineDataSet(valsComp3, "free Mode 3");
        LineDataSet setComp4 = new LineDataSet(valsComp4, "free Mode 4");


        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp3.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp4.setAxisDependency(YAxis.AxisDependency.LEFT);

        //set a color for each mode
        setComp1.setColor(Color.BLACK);
        setComp2.setColor(Color.BLUE);
        setComp3.setColor(Color.RED);
        setComp4.setColor(Color.YELLOW);

        //create a list of data sets
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        //add each Line set to the list
        dataSets.add(setComp1);
        dataSets.add(setComp2);
        dataSets.add(setComp3);
        dataSets.add(setComp4);

        //create a lines data and return it
        LineData data = new LineData(xVals, dataSets);
        return data;
    }


}

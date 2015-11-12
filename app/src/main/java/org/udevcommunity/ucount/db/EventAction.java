package org.udevcommunity.ucount.db;

import java.sql.Time;

/**
 * Created by Taym on 07/11/2015.
 */
public class EventAction {
    private int id; // id
    private int Action; // action done (1 or -1)
    private Time time; // the time
    private int Count; // count of people

    public String toString()
    {
        return Integer.toString(getId()) + "/" + Integer.toString(getAction()) + "/" + getTime().toString() + "/" + Integer.toString(getCount()) + "\n";
    }

    // Constuctors
    public EventAction()
    {

    }
    public EventAction(int id, int action, Time time, int number)
    {
        this.id = id;
        this.Action = action;
        this.time = time;
        this.Count = number;
    }

    // Setters
    public void setId(int id)
    {
        this.id = id;
    }
    public void setAction(int action)
    {
        this.Action = action;
    }
    public void setTime(Time time)
    {
        this.time = time;
    }
    public void setCount(int count)
    {
        this.Count = count;
    }

    // Getters
    public int getId()
    {
        return this.id;
    }
    public int getAction()
    {
        return this.Action;
    }
    public Time getTime()
    {
        return this.time;
    }
    public int getCount()
    {
        return this.Count;
    }
}

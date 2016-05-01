package com.home.sewright22.bg_control.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by steve on 1/22/2016.
 */
public class JournalEntry
{
    private int _id;
    private Bolus _bolus;
    private Date _timeStamp;
    private int _carbCount;
    private int _foodID;
    private String foodName;

    public int get_bolusID()
    {
        return _bolusID;
    }

    public void set_bolusID(int _bolusID)
    {
        this._bolusID = _bolusID;
    }

    private int _bolusID;

    public JournalEntry()
    {
        _timeStamp = Calendar.getInstance().getTime();
    }

    public void set_carbCount(int cc)
    {
        _carbCount = cc;
    }

    public void set_timeStamp(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        _timeStamp = new Date();
        try
        {
            _timeStamp = dateFormat.parse(date);
        }
        catch(ParseException pe)
        {
            pe.printStackTrace();
        }
    }

    public void set_id(int _id)
    {
        this._id = _id;
    }

    public int get_id()
    {
        return _id;
    }

    public int getCarbs()
    {
        return _carbCount;
    }

    public Date getStartTime()
    {
        return _timeStamp;
    }

    @Override
    public String toString()
    {
        StringBuilder retVal = new StringBuilder();
        retVal.append(foodName);
        retVal.append(System.getProperty("line.separator"));
        retVal.append(DateFormat.getTimeInstance().format(_timeStamp));
        /*retVal.append("BOL: ");
        retVal.append(InitialBolus);
        retVal.append(" SBG: ");
        retVal.append(StartingBG);*/

        return retVal.toString();
    }

    public int get_foodID()
    {
        return _foodID;
    }

    public void set_foodID(int _foodID)
    {
        this._foodID = _foodID;
    }

    public String getFoodName()
    {
        return foodName;
    }

    public void setFoodName(String foodName)
    {
        this.foodName = foodName;
    }
}

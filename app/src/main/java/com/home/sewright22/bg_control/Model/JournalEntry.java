package com.home.sewright22.bg_control.Model;

import android.os.Parcel;
import android.os.Parcelable;

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
    private int StartingBG;
    private int FinalBG;
    private double InitialBolus;
    private double ExtendedBolus;
    private Date Time;
    private String Food;
    private int CarbCount;
    private int Bolus_Type;
    private int Bolus_Time;
    private int _time_elapsed_in_minutes;
    private int foodID;

    public JournalEntry()
    {
        Time = Calendar.getInstance().getTime();
        Food = "";
    }

    public void setStartingBG(int startingBG)
    {
        StartingBG = startingBG;
    }

    public void setFinalBG(int finalBG)
    {
        FinalBG = finalBG;

        if(finalBG > 0)
        {
            long timeInMillis = Calendar.getInstance().getTimeInMillis() - Time.getTime();
            long timeInSeconds = timeInMillis/1000;
            _time_elapsed_in_minutes = (int)(timeInSeconds/60);
        }
    }

    public void setInitialBolus(double initialBolus)
    {
        InitialBolus = initialBolus;
    }

    public void setFood(String food)
    {
        Food = food;
    }

    public void setCarbCount(int cc)
    {
        CarbCount = cc;
    }

    public double getExtendedBolus()
    {
        return ExtendedBolus;
    }

    public void setExtendedBolus(double extendedBolus)
    {
        ExtendedBolus = extendedBolus;
    }

    public void setBolus_Type(int bolus_Type)
    {
        Bolus_Type = bolus_Type;
    }

    public void setBolus_Time(int bolus_Time)
    {
        Bolus_Time = bolus_Time;
    }

    public void setTime(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Time = new Date();
        try
        {
            Time = dateFormat.parse(date);
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

    public int getBolus_Type()
    {
        return Bolus_Type;
    }

    public int getBolus_Time()
    {
        return Bolus_Time;
    }

    public String getFood()
    {
        return Food;
    }

    public int getCarbs()
    {
        return CarbCount;
    }

    public int getStartingBG()
    {
        return StartingBG;
    }

    public double getInitialBolus()
    {
        return InitialBolus;
    }

    public int getFinalBG()
    {
        return FinalBG;
    }

    public Date getStartTime()
    {
        return Time;
    }

    public int get_time_elapsed_in_minutes()
    {
        return _time_elapsed_in_minutes;
    }

    @Override
    public String toString()
    {
        StringBuilder retVal = new StringBuilder();
        //retVal.append(DateFormat.getTimeInstance().format(Time));
        retVal.append(Food);
        retVal.append(System.getProperty("line.separator"));
        retVal.append("BOL: ");
        retVal.append(InitialBolus);
        retVal.append(" SBG: ");
        retVal.append(StartingBG);

        return retVal.toString();
    }

    public void update(JournalEntry in)
    {
        this.Food = (in.getFood());
        this.CarbCount = in.getCarbs();
        this.Bolus_Type = in.getBolus_Type();
        this.InitialBolus = in.getInitialBolus();
        this.ExtendedBolus = in.getExtendedBolus();
        this.Bolus_Time = in.getBolus_Time();
        this.StartingBG = in.getStartingBG();
        this.FinalBG = in.getFinalBG();
    }

    public int getFoodID()
    {
        return foodID;
    }

    public void setFoodID(int foodID)
    {
        this.foodID = foodID;
    }
}

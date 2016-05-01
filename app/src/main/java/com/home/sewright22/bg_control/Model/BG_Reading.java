package com.home.sewright22.bg_control.Model;

/**
 * Created by steve on 4/23/2016.
 */
public class BG_Reading
{
    private double value;
    private String slopeName;
    private int minutesSinceStart;

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public String getSlopeName()
    {
        return slopeName;
    }

    public void setSlopeName(String slopeName)
    {
        this.slopeName = slopeName;
    }

    public int getMinutesSinceStart()
    {
        return minutesSinceStart;
    }

    public void setMinutesSinceStart(int minutesSinceStart)
    {
        this.minutesSinceStart = minutesSinceStart;
    }
}

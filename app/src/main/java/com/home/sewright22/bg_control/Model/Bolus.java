package com.home.sewright22.bg_control.Model;

/**
 * Created by steve on 4/30/2016.
 */
public class Bolus
{
    private double _amount;
    private int _percent_up_front;
    private int _percent_over_time;
    private int _length_of_time;

    public double get_amount()
    {
        return _amount;
    }

    public void set_amount(double _amount)
    {
        this._amount = _amount;
    }

    public int get_percent_up_front()
    {
        return _percent_up_front;
    }

    public void set_percent_up_front(int _percent_up_front)
    {
        this._percent_up_front = _percent_up_front;
    }

    public int get_percent_over_time()
    {
        return _percent_over_time;
    }

    public void set_percent_over_time(int _percent_over_time)
    {
        this._percent_over_time = _percent_over_time;
    }

    public int get_length_of_time()
    {
        return _length_of_time;
    }

    public void set_length_of_time(int _length_of_time)
    {
        this._length_of_time = _length_of_time;
    }
}

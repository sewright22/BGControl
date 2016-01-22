package com.home.sewright22.bg_control;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by steve on 1/22/2016.
 */
public class JournalEntry implements Parcelable{
    private int StartingBG;
    private int FinalBG;
    private double InitialBolus;
    private Date Time;
    private String Food;
    private int CarbCount;

    public JournalEntry(){}
    public JournalEntry(Parcel in){
        this.Food = (in.readString());
        this.Time = (Date)in.readValue(Date.class.getClassLoader());
        this.CarbCount = in.readInt();
        this.InitialBolus = in.readDouble();
        this.StartingBG = in.readInt();
        this.FinalBG = in.readInt();
    }

    public void setStartingBG(int startingBG) {
        StartingBG = startingBG;
    }

    public void setFinalBG(int finalBG) {
        FinalBG = finalBG;
    }

    public void setInitialBolus(double initialBolus) {
        InitialBolus = initialBolus;
    }

    public void setFood(String food)
    {
        Time = Calendar.getInstance().getTime();
        Food = food;
    }

    public void setCarbCount(int cc){
        CarbCount = cc;
    }

    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        //retVal.append(DateFormat.getTimeInstance().format(Time));
        retVal.append(" Food: ");
        retVal.append(Food);

        return retVal.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Food);
        dest.writeValue(Time);
        dest.writeInt(CarbCount);
        dest.writeDouble(InitialBolus);
        dest.writeInt(StartingBG);
        dest.writeInt(FinalBG);
    }

    public static final Parcelable.Creator<JournalEntry> CREATOR = new Parcelable.Creator<JournalEntry>() {

        public JournalEntry createFromParcel(Parcel in) {
            return new JournalEntry(in);
        }

        public JournalEntry[] newArray(int size) {
            return new JournalEntry[size];
        }
    };


    public String getFood() {
        return Food;
    }

    public int getCarbs() {
        return CarbCount;
    }

    public int getStartingBG() {
        return StartingBG;
    }

    public double getInitialBolus() {
        return InitialBolus;
    }

    public int getFinalBG() {
        return FinalBG;
    }

    public Date getStartTime() {
        return Time;
    }
}

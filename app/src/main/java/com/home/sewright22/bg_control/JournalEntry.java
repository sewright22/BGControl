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
    private double ExtendedBolus;
    private Date Time;
    private String Food;
    private int CarbCount;
    private int Bolus_Type;
    private int Bolus_Time;

    public JournalEntry()
    {
        Time = Calendar.getInstance().getTime();
        Food = "";
    }
    public JournalEntry(Parcel in){
        this.Food = (in.readString());
        this.Time = (Date)in.readValue(Date.class.getClassLoader());
        this.CarbCount = in.readInt();
        this.Bolus_Type = in.readInt();
        this.InitialBolus = in.readDouble();
        this.ExtendedBolus = in.readDouble();
        this.Bolus_Time = in.readInt();
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

    public double getExtendedBolus() {
        return ExtendedBolus;
    }

    public void setExtendedBolus(double extendedBolus) {
        ExtendedBolus = extendedBolus;
    }

    public int getBolus_Type() {
        return Bolus_Type;
    }

    public void setBolus_Type(int bolus_Type) {
        Bolus_Type = bolus_Type;
    }

    public int getBolus_Time() {
        return Bolus_Time;
    }

    public void setBolus_Time(int bolus_Time) {
        Bolus_Time = bolus_Time;
    }

    @Override
    public String toString() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Food);
        dest.writeValue(Time);
        dest.writeInt(CarbCount);
        dest.writeInt(Bolus_Type);
        dest.writeDouble(InitialBolus);
        dest.writeDouble(ExtendedBolus);
        dest.writeInt(Bolus_Time);
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
}

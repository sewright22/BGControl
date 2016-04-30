package com.home.sewright22.bg_control.Contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.home.sewright22.bg_control.Model.Bolus;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.Model.JournalEntryList;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by steve on 1/24/2016.
 */
public class JournalEntryDbHelper extends SQLiteOpenHelper
{
    public static abstract class JournalEntryTable implements BaseColumns
    {
        public static final String TABLE_NAME = "journal_entry";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_BOLUS_ID = "bolus_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CARB_COUNT = "carb_count";
        public static final String COLUMN_NAME_IS_ACTIVE = "is_active";
    }

    public static abstract class BolusTable implements BaseColumns
    {
        public static final String TABLE_NAME = "bolus";
        public static final String COLUMN_NAME_BOLUS_AMOUNT = "bolus_amount";
        public static final String COLUMN_NAME_PERCENT_UP_FRONT = "percent_up_front";
        public static final String COLUMN_NAME_PERCENT_OVER_TIME = "percent_over_time";
        public static final String COLUMN_NAME_LENGTH_OF_TIME = "length_of_time";
    }

    public static abstract class BG_EstimateTable implements BaseColumns
    {
        public static final String TABLE_NAME = "bg_estimate";
        public static final String COLUMN_NAME_JOURNAL_ENTRY_ID = "journal_entry_id";
        public static final String COLUMN_NAME_GLUCOSE_READING = "bg_estimate";
        public static final String COLUMN_NAME_READING_TIME = "reading_time";
    }

    public static abstract class FoodTable implements BaseColumns
    {
        public static final String TABLE_NAME = "food";
        public static final String COLUMN_NAME_NAME = "name";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            JournalEntryTable.TABLE_NAME + " (" +
            JournalEntryTable._ID + " INTEGER PRIMARY KEY," +
            JournalEntryTable.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_ID + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_CARB_COUNT + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_BG_ESTIMATES = "CREATE TABLE " +
            BG_EstimateTable.TABLE_NAME + " (" +
            BG_EstimateTable._ID + " INTEGER PRIMARY KEY," +
            BG_EstimateTable.COLUMN_NAME_JOURNAL_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_GLUCOSE_READING + INTEGER_TYPE + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_READING_TIME + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_FOODS = "CREATE TABLE " +
            FoodTable.TABLE_NAME + " (" +
            FoodTable._ID + " INTEGER PRIMARY KEY," +
            FoodTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_DATE + TEXT_TYPE + " )";

    private static final String SQL_CREATE_BOLUS = "CREATE TABLE " +
            BolusTable.TABLE_NAME + " (" +
            BolusTable._ID + " INTEGER PRIMARY KEY," +
            BolusTable.COLUMN_NAME_BOLUS_AMOUNT + TEXT_TYPE + COMMA_SEP +
            BolusTable.COLUMN_NAME_PERCENT_UP_FRONT + INTEGER_TYPE + COMMA_SEP +
            BolusTable.COLUMN_NAME_PERCENT_OVER_TIME + INTEGER_TYPE + COMMA_SEP +
            BolusTable.COLUMN_NAME_LENGTH_OF_TIME + INTEGER_TYPE + " )";



    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + JournalEntryTable.TABLE_NAME;
    private static final String SQL_DELETE_BG_ESTIMATES = "DROP TABLE IF EXISTS " + BG_EstimateTable.TABLE_NAME;
    private static final String SQL_DELETE_FOODS = "DROP TABLE IF EXISTS " + FoodTable.TABLE_NAME;
    private static final String SQL_DELETE_BOLUS = "DROP TABLE IF EXISTS " + BolusTable.TABLE_NAME;

    private static final String SQL_ENTRY_COLUMNS = "" +
            JournalEntryTable._ID + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_FOOD_ID + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_ID + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_DATE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_CARB_COUNT + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_IS_ACTIVE;

    private static final String SQL_BG_READING_COLUMNS = "" +
            BG_EstimateTable._ID + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_READING_TIME + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_GLUCOSE_READING;

    private static final String SQL_SELECT_JOIN = "SELECT " + SQL_ENTRY_COLUMNS +
                                                  " FROM " + JournalEntryTable.TABLE_NAME;

    private static final String SQL_WHERE_ENTRY_IS_ACTIVE = "WHERE " + JournalEntryTable.COLUMN_NAME_IS_ACTIVE + " = 1";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "JournalEntry.db";

    public JournalEntryDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        dropTables(db);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void createTables()
    {
        createTables(getWritableDatabase());
    }

    public void createTables(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_BG_ESTIMATES);
        db.execSQL(SQL_CREATE_FOODS);
        db.execSQL(SQL_CREATE_BOLUS);
    }

    public void dropTables()
    {
        dropTables(getWritableDatabase());
    }

    public void dropTables(SQLiteDatabase db)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_BG_ESTIMATES);
        db.execSQL(SQL_DELETE_FOODS);
        db.execSQL(SQL_DELETE_BOLUS);
    }

    public long insertJournalEntry(JournalEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JournalEntryTable.COLUMN_NAME_DATE, entry.getStartTime().toString());
        contentValues.put(JournalEntryTable.COLUMN_NAME_FOOD_ID, entry.get_foodID());
        contentValues.put(JournalEntryTable.COLUMN_NAME_BOLUS_ID, entry.get_bolusID());
        contentValues.put(JournalEntryTable.COLUMN_NAME_CARB_COUNT, entry.getCarbs());
        contentValues.put(JournalEntryTable.COLUMN_NAME_IS_ACTIVE, 1);
        long retVal = db.insert(JournalEntryTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    public long insertBG_Reading(int journalEntryID, int reading, int dateTime)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BG_EstimateTable.COLUMN_NAME_JOURNAL_ENTRY_ID, journalEntryID);
        contentValues.put(BG_EstimateTable.COLUMN_NAME_GLUCOSE_READING, reading);
        contentValues.put(BG_EstimateTable.COLUMN_NAME_READING_TIME, dateTime);
        long retVal = db.insert(BG_EstimateTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    public long insertFood(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FoodTable.COLUMN_NAME_NAME, name);
        long retVal = db.insert(FoodTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    public long insertBolus(double amount, double lengthOfTime, int percentUpFront, int percentOverTime)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BolusTable.COLUMN_NAME_BOLUS_AMOUNT, amount);
        contentValues.put(BolusTable.COLUMN_NAME_LENGTH_OF_TIME, lengthOfTime);
        contentValues.put(BolusTable.COLUMN_NAME_PERCENT_OVER_TIME, percentOverTime);
        contentValues.put(BolusTable.COLUMN_NAME_PERCENT_UP_FRONT, percentUpFront);
        long retVal = db.insert(BolusTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    private String getCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date Time = new Date();
        try
        {
            Time = dateFormat.parse(Calendar.getInstance().getTime().toString());
        }
        catch (ParseException pe)
        {
            pe.printStackTrace();
        }

        return Time.toString();
    }

    public JournalEntryList getAllEntries()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SQL_SELECT_JOIN + " WHERE " + JournalEntryTable.COLUMN_NAME_IS_ACTIVE + " = 1" ;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        JournalEntryList entryList = new JournalEntryList();

        while (cursor.isAfterLast() == false)
        {
            entryList.insertJournalEntry(getNextJournalEntry(cursor));
        }

        return entryList;
    }

    public JournalEntryList getActiveEntries()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SQL_SELECT_JOIN + SQL_WHERE_ENTRY_IS_ACTIVE;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        JournalEntryList entryList = new JournalEntryList();

        while (cursor.isAfterLast() == false)
        {
            entryList.insertJournalEntry(getNextJournalEntry(cursor));
        }

        return entryList;
    }

    public ArrayList<DataPoint> getBG_Readings(int entryID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + SQL_BG_READING_COLUMNS + " " +
                       "FROM " + BG_EstimateTable.TABLE_NAME + " " +
                       "WHERE " + BG_EstimateTable.COLUMN_NAME_JOURNAL_ENTRY_ID + " = " + entryID + " " +
                       "ORDER BY " + BG_EstimateTable.COLUMN_NAME_READING_TIME + " ASC";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        ArrayList<DataPoint> points = new ArrayList<DataPoint>();

        while (cursor.isAfterLast() == false)
        {
            points.add(new DataPoint(cursor.getInt(1), cursor.getInt(2)));
            cursor.moveToNext();
        }

        return points;
    }

    public void setJournalEntryAsInactive(int entryID)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JournalEntryTable.COLUMN_NAME_IS_ACTIVE, 0);
        db.update(JournalEntryTable.TABLE_NAME, contentValues, JournalEntryTable._ID + "=" + entryID, null);
    }

    public JournalEntry getSpecificEntry(int pk)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SQL_SELECT_JOIN + " WHERE _ID=" + pk;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        JournalEntry entry = null;

        if(cursor.isAfterLast() == false)
        {
            entry = getNextJournalEntry(cursor);
        }

        return entry;
    }

    public String getFoodName(int foodID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + FoodTable.COLUMN_NAME_NAME +
                       " FROM " + FoodTable.TABLE_NAME +
                       " WHERE _ID=" + foodID;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        String foodName = "";

        if(cursor.isAfterLast() == false)
        {
            foodName = cursor.getString(0);
        }

        return foodName;
    }

    public int getFoodId(String foodName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + "_id" + " " +
                "FROM " + FoodTable.TABLE_NAME + " " +
                "WHERE " + FoodTable.COLUMN_NAME_NAME + " = '" + foodName +"'";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        int id = -1;
        if(cursor.isAfterLast() == false)
        {
            id = cursor.getInt(0);
        }

        return id;
    }

    public Bolus getBolus(int bolusID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                BolusTable.COLUMN_NAME_BOLUS_AMOUNT + COMMA_SEP +
                BolusTable.COLUMN_NAME_PERCENT_UP_FRONT + COMMA_SEP +
                BolusTable.COLUMN_NAME_PERCENT_OVER_TIME + COMMA_SEP +
                BolusTable.COLUMN_NAME_LENGTH_OF_TIME +
                " FROM " + BolusTable.TABLE_NAME +
                " WHERE _ID=" + bolusID;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        Bolus bolus = new Bolus();

        if(cursor.isAfterLast() == false)
        {
            bolus.set_amount(cursor.getDouble(0));
            bolus.set_percent_up_front(cursor.getInt(1));
            bolus.set_percent_over_time(cursor.getInt(2));
            bolus.set_length_of_time(cursor.getInt(3));
        }

        return bolus;
    }

    public ArrayList<String> getAllFoods()
    {
        ArrayList<String> foodList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT " + FoodTable.COLUMN_NAME_NAME + " " +
                       "FROM " + FoodTable.TABLE_NAME + " " +
                       "ORDER BY " + FoodTable.COLUMN_NAME_NAME;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false)
        {
            foodList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return foodList;
    }

    public JournalEntry getNextJournalEntry(Cursor cursor)
    {
        JournalEntry entry = new JournalEntry();


        entry.set_id(cursor.getInt(0));
        entry.set_foodID(cursor.getInt(1));
        entry.set_bolusID(cursor.getInt(2));
        String date = cursor.getString(3);
        entry.set_timeStamp(date);

        entry.set_carbCount(cursor.getInt(4));
            cursor.moveToNext();

        return entry;
    }
}

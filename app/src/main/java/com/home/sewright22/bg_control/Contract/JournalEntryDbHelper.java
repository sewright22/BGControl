package com.home.sewright22.bg_control.Contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.Model.JournalEntryList;

import java.sql.Time;
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
        public static final String COLUMN_NAME_FOOD_ID= "food_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CARB_COUNT = "carb_count";
        public static final String COLUMN_NAME_STARTING_BG = "starting_bg";
        public static final String COLUMN_NAME_BOLUS_TYPE = "bolus_type";
        public static final String COLUMN_NAME_INITIAL_BOLUS = "initial_bolus";
        public static final String COLUMN_NAME_EXTENDED_BOLUS = "extended_bolus";
        public static final String COLUMN_NAME_BOLUS_TIME = "bolus_time";
        public static final String COLUMN_NAME_TIME_ELAPSED = "time_elapsed";
    }

    public static abstract class BG_EstimateTable implements BaseColumns
    {
        public static final String TABLE_NAME = "estimate_bg";
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
            JournalEntryTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_CARB_COUNT + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_STARTING_BG + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TYPE + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_INITIAL_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_EXTENDED_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TIME + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_BG_ESTIMATES = "CREATE TABLE " +
            BG_EstimateTable.TABLE_NAME + " (" +
            BG_EstimateTable._ID + " INTEGER PRIMARY KEY," +
            BG_EstimateTable.COLUMN_NAME_JOURNAL_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_GLUCOSE_READING + TEXT_TYPE + COMMA_SEP +
            BG_EstimateTable.COLUMN_NAME_READING_TIME + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_FOODS = "CREATE TABLE " +
            FoodTable.TABLE_NAME + " (" +
            FoodTable._ID + " INTEGER PRIMARY KEY," +
            FoodTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_DATE + TEXT_TYPE + " )";



    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + JournalEntryTable.TABLE_NAME;
    private static final String SQL_DELETE_BG_ESTIMATES = "DROP TABLE IF EXISTS " + BG_EstimateTable.TABLE_NAME;
    private static final String SQL_DELETE_FOODS = "DROP TABLE IF EXISTS " + FoodTable.TABLE_NAME;

    private static final String SQL_ENTRY_COLUMNS = "e." + JournalEntryTable._ID + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_FOOD_ID + COMMA_SEP +
            FoodTable.COLUMN_NAME_NAME + COMMA_SEP + "e." +
            JournalEntryTable.COLUMN_NAME_DATE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_CARB_COUNT + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_STARTING_BG + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_INITIAL_BOLUS + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_EXTENDED_BOLUS + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TIME;

    private static final String SQL_SELECT_JOIN = "SELECT " + SQL_ENTRY_COLUMNS +
                                                  " FROM " + JournalEntryTable.TABLE_NAME + " e "+
                                                  "JOIN " + FoodTable.TABLE_NAME + " f ON e." + JournalEntryTable.COLUMN_NAME_FOOD_ID + "=f." + FoodTable._ID + " ";

    private static final String SQL_WHERE_DATE_IS_TODAY = "WHERE " + "e." + JournalEntryTable.COLUMN_NAME_DATE +
            " = (select max("+JournalEntryTable.COLUMN_NAME_DATE+
            ") from "+JournalEntryTable.TABLE_NAME+" WHERE "+
            "e." + JournalEntryTable.COLUMN_NAME_DATE + " >= DATE('now') )";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "JournalEntry.db";

    public JournalEntryDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_BG_ESTIMATES);
        db.execSQL(SQL_CREATE_FOODS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_BG_ESTIMATES);
        db.execSQL(SQL_DELETE_FOODS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertJournalEntry(JournalEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JournalEntryTable.COLUMN_NAME_DATE, entry.getStartTime().toString());
        contentValues.put(JournalEntryTable.COLUMN_NAME_FOOD_ID, entry.getFoodID());
        contentValues.put(JournalEntryTable.COLUMN_NAME_CARB_COUNT, entry.getCarbs());
        contentValues.put(JournalEntryTable.COLUMN_NAME_STARTING_BG, entry.getStartingBG());
        contentValues.put(JournalEntryTable.COLUMN_NAME_BOLUS_TYPE, entry.getBolus_Type());
        contentValues.put(JournalEntryTable.COLUMN_NAME_INITIAL_BOLUS, entry.getInitialBolus());
        contentValues.put(JournalEntryTable.COLUMN_NAME_EXTENDED_BOLUS, entry.getExtendedBolus());
        contentValues.put(JournalEntryTable.COLUMN_NAME_BOLUS_TIME, entry.getBolus_Time());
        long retVal = db.insert(JournalEntryTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    public long insertBG_Reading(int journalEntryID, double reading, long dateTime)
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

    public Cursor getAllEntries()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT" + SQL_ENTRY_COLUMNS + " FROM " + JournalEntryTable.TABLE_NAME, null);
        return res;
    }

    public JournalEntryList getActiveEntries()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SQL_SELECT_JOIN;// + SQL_WHERE_DATE_IS_TODAY;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        JournalEntryList entryList = new JournalEntryList();

        while (cursor.isAfterLast() == false)
        {
            entryList.insertJournalEntry(getNextJournalEntry(cursor));
        }

        return entryList;
    }

    public JournalEntry getSpecificEntry(int pk)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SQL_SELECT_JOIN + " WHERE e._ID=" + pk;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        JournalEntry entry = null;

        if(cursor.isAfterLast() == false)
        {
            entry = getNextJournalEntry(cursor);
        }

        return entry;
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
        entry.setFoodID(cursor.getInt(1));
        entry.setFood(cursor.getString(2));
            String date = cursor.getString(3);
            entry.setTime(date);

            entry.setCarbCount(cursor.getInt(4));
            entry.setStartingBG(cursor.getInt(5));
            entry.setBolus_Type(cursor.getInt(6));
            entry.setInitialBolus(cursor.getDouble(7));
            entry.setExtendedBolus(cursor.getDouble(8));
            entry.setBolus_Time(cursor.getInt(9));
            cursor.moveToNext();

        return entry;
    }
}

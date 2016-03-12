package com.home.sewright22.bg_control.Contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.home.sewright22.bg_control.Model.JournalEntry;

/**
 * Created by steve on 1/24/2016.
 */
public class JournalEntryDbHelper extends SQLiteOpenHelper
{
    public static abstract class JournalEntryTable implements BaseColumns
    {
        public static final String TABLE_NAME = "journal_entry";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_FOOD = "food";
        public static final String COLUMN_NAME_CARB_COUNT = "carb_count";
        public static final String COLUMN_NAME_STARTING_BG = "starting_bg";
        public static final String COLUMN_NAME_BOLUS_TYPE = "bolus_type";
        public static final String COLUMN_NAME_INITIAL_BOLUS = "initial_bolus";
        public static final String COLUMN_NAME_EXTENDED_BOLUS = "extended_bolus";
        public static final String COLUMN_NAME_BOLUS_TIME = "bolus_time";
        public static final String COLUMN_NAME_FINAL_BG = "final_bg";
        public static final String COLUMN_NAME_TIME_ELAPSED = "time_elapsed";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            JournalEntryTable.TABLE_NAME + " (" +
            JournalEntryTable._ID + " INTEGER PRIMARY KEY," +
            JournalEntryTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_FOOD + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_CARB_COUNT + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_STARTING_BG + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TYPE + INTEGER_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_INITIAL_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_EXTENDED_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntryTable.COLUMN_NAME_BOLUS_TIME + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + JournalEntryTable.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "JournalEntry.db";

    public JournalEntryDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
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
        contentValues.put(JournalEntryTable.COLUMN_NAME_FOOD, entry.getFood());
        contentValues.put(JournalEntryTable.COLUMN_NAME_CARB_COUNT, entry.getCarbs());
        contentValues.put(JournalEntryTable.COLUMN_NAME_STARTING_BG, entry.getStartingBG());
        contentValues.put(JournalEntryTable.COLUMN_NAME_BOLUS_TYPE, entry.getBolus_Type());
        contentValues.put(JournalEntryTable.COLUMN_NAME_INITIAL_BOLUS, entry.getInitialBolus());
        contentValues.put(JournalEntryTable.COLUMN_NAME_EXTENDED_BOLUS, entry.getExtendedBolus());
        contentValues.put(JournalEntryTable.COLUMN_NAME_BOLUS_TIME, entry.getBolus_Time());
        long retVal = db.insert(JournalEntryTable.TABLE_NAME, null, contentValues);
        return retVal;
    }

    public Cursor getAllEntries()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + JournalEntryTable.TABLE_NAME, null);
        return res;
    }

    public JournalEntry getSpecificEntry(int pk)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + JournalEntryTable.TABLE_NAME + " WHERE _ID=" + pk, null);

        cursor.moveToFirst();

        JournalEntry entry = new JournalEntry();

        if (cursor.isAfterLast() == false)
        {

            entry.set_id(cursor.getInt(0));
            String date = cursor.getString(1);
            entry.setTime(date);
            entry.setFood(cursor.getString(2));
            entry.setCarbCount(cursor.getInt(3));
            entry.setStartingBG(cursor.getInt(4));
            entry.setBolus_Type(cursor.getInt(5));
            entry.setInitialBolus(cursor.getDouble(6));
            entry.setExtendedBolus(cursor.getDouble(7));
            entry.setBolus_Time(cursor.getInt(8));
            cursor.moveToNext();
        }

        return entry;
    }
}

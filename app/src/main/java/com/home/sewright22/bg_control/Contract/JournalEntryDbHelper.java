package com.home.sewright22.bg_control.Contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by steve on 1/24/2016.
 */
public class JournalEntryDbHelper extends SQLiteOpenHelper
{
    public static final String TABLE_NAME = "journal_entry";
    public static final String COLUMN_NAME_FOOD = "food";
    public static final String COLUMN_NAME_CARB_COUNT = "carb_count";
    public static final String COLUMN_NAME_STARTING_BG = "starting_bg";
    public static final String COLUMN_NAME_BOLUS_TYPE = "bolus_type";
    public static final String COLUMN_NAME_INITIAL_BOLUS = "initial_bolus";
    public static final String COLUMN_NAME_EXTENDED_BOLUS = "extended_bolus";
    public static final String COLUMN_NAME_BOLUS_TIME = "bolus_time";
    public static final String COLUMN_NAME_FINAL_BG = "final_bg";
    public static final String COLUMN_NAME_TIME_ELAPSED = "time_elapsed";

    public static abstract class JournalEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "journal_entry";
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
            JournalEntry.TABLE_NAME + " (" +
            JournalEntry._ID + " INTEGER PRIMARY KEY," +
            JournalEntry.COLUMN_NAME_FOOD + TEXT_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_CARB_COUNT + INTEGER_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_STARTING_BG + INTEGER_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_BOLUS_TYPE + INTEGER_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_INITIAL_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_EXTENDED_BOLUS + TEXT_TYPE + COMMA_SEP +
            JournalEntry.COLUMN_NAME_BOLUS_TIME + DATE_TYPE + COMMA_SEP + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "JournalEntry.db";

    public JournalEntryDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
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
}

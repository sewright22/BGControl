package com.home.sewright22.bg_control.Contract;

import android.provider.BaseColumns;

/**
 * Created by steve on 1/23/2016.
 */
public final class FeedReaderContract
{
    public FeedReaderContract(){}

    public static abstract class JournalEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "journal_entry";
        public static final String COLUMN_NAME_JOURNAL_ENTRY_ID = "journal_entry_id";
        public static final String COLUMN_NAME_FOOD = "food";
        public static final String COLUMN_NAME_CARB_COUNT = "carb_count";
        public static final String COLUMN_NAME_STARTING_BG = "starting_bg";
        public static final String COLUMN_NAME_BOLUS_TYPE = "bolus_type";
        public static final String COLUMN_NAME_INITIAL_BOLUS = "initial_bolus";
        public static final String COLUMN_NAME_EXTENDED_BOLUS = "extended_bolus";
        public static final String COLUMN_NAME_BOLUS_TIME = "bolus_time";
        public static final String COLUMN_NAME_FINAL_BG = "final_bg";
    }
}

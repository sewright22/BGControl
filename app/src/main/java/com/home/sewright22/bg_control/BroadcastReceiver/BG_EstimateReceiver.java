package com.home.sewright22.bg_control.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.home.sewright22.bg_control.Activity.MainActivity;
import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.Model.JournalEntryList;

import java.util.Date;

/**
 * Created by steve on 4/23/2016.
 */
public class BG_EstimateReceiver extends BroadcastReceiver
{
    private JournalEntryDbHelper mDbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDbHelper = new JournalEntryDbHelper(context);

        final double bgEstimate = intent.getDoubleExtra("com.eveningoutpost.dexdrip.Extras.BgEstimate", 0);
        long timeOfReading = intent.getLongExtra("com.eveningoutpost.dexdrip.Extras.Time", new Date().getTime());
        String slopeName = intent.getStringExtra("com.eveningoutpost.dexdrip.Extras.BgSlopeName");


        JournalEntryList activeEntries = mDbHelper.getAllEntries();

        for (JournalEntry entry: activeEntries)
        {
            long startTime = entry.getStartTime().getTime();
            long timeDifferenceInMilliseconds = timeOfReading - startTime;
            long timeDifferenceInSeconds = timeDifferenceInMilliseconds / 1000;
            long timeDifferenceInMinutes = timeDifferenceInSeconds / 60;

            if(timeDifferenceInMinutes < 240)
            {
                mDbHelper.insertBG_Reading(entry.get_id(), (int) bgEstimate, (int) timeDifferenceInMinutes, slopeName);
            }
            else
            {
                mDbHelper.setJournalEntryAsInactive(entry.get_id());
            }
        }
    }
}

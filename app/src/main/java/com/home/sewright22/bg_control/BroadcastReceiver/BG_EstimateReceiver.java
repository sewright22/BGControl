package com.home.sewright22.bg_control.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.home.sewright22.bg_control.Activity.MainActivity;
import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;

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

        // TODO Auto-generated method stub
        final double bgEstimate = intent.getDoubleExtra("com.eveningoutpost.dexdrip.Extras.BgEstimate", 0);
        long datetime = intent.getLongExtra("com.eveningoutpost.dexdrip.Extras.Time", new Date().getTime());
        //Toast.makeText(MainActivity.this, "Data Received from External App", Toast.LENGTH_SHORT).show();

    }
}

package com.home.sewright22.bg_control.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;

public class NotificationPublisher extends BroadcastReceiver
{
    private JournalEntryDbHelper mDbHelper;

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent)
    {
        mDbHelper = new JournalEntryDbHelper(context);

        int id = intent.getIntExtra("entryID", 0);

        mDbHelper.setJournalEntryAsInactive(id);
    }
}

package com.home.sewright22.bg_control.Activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.Model.JournalEntryList;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.NotificationPublisher;
import com.home.sewright22.bg_control.R;

public class MainActivity extends AppCompatActivity
{
    private JournalEntryDbHelper mDbHelper;
    private JournalEntryList journalEntries = new JournalEntryList();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.list);
        setSupportActionBar(toolbar);
        mDbHelper = new JournalEntryDbHelper(this);

        UpdateDisplayedJournal();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent parcelIntent = new Intent(MainActivity.this, JournalEntryDetailsActivity.class);

                JournalEntry itemValue = new JournalEntry();

                parcelIntent.putExtra("item", itemValue);

                startActivityForResult(parcelIntent, journalEntries.getCount() + 1);
            }
        });

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                Intent parcelIntent = new Intent(MainActivity.this, JournalEntryDetailsActivity.class);

                int itemPosition = position;

                ListView listView = (ListView) findViewById(R.id.list);

                // ListView Clicked item value
                JournalEntry itemValue = (JournalEntry) listView.getItemAtPosition(itemPosition);

                parcelIntent.putExtra("item", itemValue);

                startActivityForResult(parcelIntent, itemPosition);
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            JournalEntry entry = (JournalEntry) data.getExtras().getParcelable("item");

            if (requestCode + 1 <= journalEntries.getCount())
            {
                journalEntries.insertJournalEntry(entry, requestCode);
            }
            else
            {
                mDbHelper.insertJournalEntry(entry);
                journalEntries.insertJournalEntry(entry);
            }

            createBloodSugarReminder(entry);

            UpdateDisplayedJournal();
        }
    }

    private void createBloodSugarReminder(JournalEntry entry)
    {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(entry.toString())
                        .setContentText("Please enter your current BG.");

        Intent parcelIntent = new Intent(MainActivity.this, JournalEntryDetailsActivity.class);
        parcelIntent.putExtra("item", entry);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        parcelIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        scheduleNotification(mBuilder.build(), 10);
    }

    private void scheduleNotification(Notification notification, int delayInSeconds)
    {

        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 001);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayInSeconds * 1000;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private void insertTestRecord()
    {
        JournalEntry first = new JournalEntry();
        first.setFood("Cake");
        first.setCarbCount(24);
        first.setStartingBG(123);
       first.setInitialBolus(2);
        first.setBolus_Type(R.integer.bolus_instant);
        JournalEntryDbHelper mDbHelper = new JournalEntryDbHelper(this);
        mDbHelper.insertJournalEntry(first);
    }


    private void UpdateDisplayedJournal()
    {
        ListView listView = (ListView) findViewById(R.id.list);
        journalEntries.clear();
        //insertTestRecord();
        Cursor cursor = mDbHelper.getAllEntries();
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false)
        {
            JournalEntry entry = new JournalEntry();
            String date = cursor.getString(1);
            entry.setTime(date);
            entry.setFood(cursor.getString(2));
            entry.setCarbCount(cursor.getInt(3));
            entry.setStartingBG(cursor.getInt(4));
            entry.setBolus_Type(cursor.getInt(5));
            entry.setInitialBolus(cursor.getDouble(6));
            entry.setExtendedBolus(cursor.getDouble(7));
            entry.setBolus_Time(cursor.getInt(8));
            journalEntries.insertJournalEntry(entry);
            cursor.moveToNext();
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<JournalEntry> adapter = new ArrayAdapter<JournalEntry>(this,
                                                                            android.R.layout.simple_list_item_2,
                                                                            android.R.id.text1,
                                                                            journalEntries.getJournalEntries());

        listView.setAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.home.sewright22.bg_control/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.home.sewright22.bg_control/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


package com.home.sewright22.bg_control.Activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.Model.JournalEntryList;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.BroadcastReceiver.NotificationPublisher;
import com.home.sewright22.bg_control.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnClickListener
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
        fab.setOnClickListener(this);

        listView.setOnItemClickListener(new JournalEntryListClickListener());



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
            int entryID =  data.getExtras().getInt("entryID");

            //createBloodSugarReminder(entryID);

            UpdateDisplayedJournal();
        }
    }

    private void createBloodSugarReminder(int entryID)
    {
        scheduleNotification(entryID, 4);
    }

    private void scheduleNotification(int entryID, int delayInHours)
    {
        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        notificationIntent.putExtra("entryID", entryID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayInHours * 60 * 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private void insertTestRecord()
    {
        JournalEntry first = new JournalEntry();
        first.set_foodID(1);
        first.set_bolusID(1);
        first.set_carbCount(34);
        //mDbHelper = new JournalEntryDbHelper(this);
        mDbHelper.insertFood("Bacon");
        mDbHelper.insertBolus(2.4, 1, 60, 40);
        mDbHelper.insertJournalEntry(first);

        mDbHelper.insertBG_Reading(1, 85, 0, "");
        mDbHelper.insertBG_Reading(1,88,5, "");
        mDbHelper.insertBG_Reading(1,92,10, "");
        mDbHelper.insertBG_Reading(1,99,15, "");
        mDbHelper.insertBG_Reading(1, 110, 20, "");
        mDbHelper.insertBG_Reading(1, 115, 25, "");
        mDbHelper.insertBG_Reading(1, 119, 30, "");
        mDbHelper.insertBG_Reading(1, 123, 35, "");
        mDbHelper.insertBG_Reading(1, 130, 40, "");
        mDbHelper.insertBG_Reading(1, 142, 45, "");
        mDbHelper.insertBG_Reading(1,149,50, "");
        mDbHelper.insertBG_Reading(1,158,55, "");
        mDbHelper.insertBG_Reading(1,162,60, "");
        mDbHelper.insertBG_Reading(1,160,65, "");
        mDbHelper.insertBG_Reading(1,156,70, "");
        mDbHelper.insertBG_Reading(1,153,75, "");
        mDbHelper.insertBG_Reading(1,155,80, "");
        mDbHelper.insertBG_Reading(1,152,85, "");
        mDbHelper.insertBG_Reading(1,148,90, "");
        mDbHelper.insertBG_Reading(1,144,95, "");
        mDbHelper.insertBG_Reading(1,135,100, "");
        mDbHelper.insertBG_Reading(1,130,105, "");
        mDbHelper.insertBG_Reading(1,118,110, "");
        mDbHelper.insertBG_Reading(1,110,115, "");
        mDbHelper.insertBG_Reading(1,100,120, "");
        mDbHelper.insertBG_Reading(1,100,125, "");
        mDbHelper.insertBG_Reading(1,98,130, "");
        mDbHelper.insertBG_Reading(1,100,135, "");
        mDbHelper.insertBG_Reading(1,95,140, "");
        mDbHelper.insertBG_Reading(1,90,145, "");
        mDbHelper.insertBG_Reading(1,90,150, "");
        mDbHelper.insertBG_Reading(1,89,155, "");
        mDbHelper.insertBG_Reading(1,88,160, "");
        mDbHelper.insertBG_Reading(1,87,165, "");
        mDbHelper.insertBG_Reading(1,86,170, "");
        mDbHelper.insertBG_Reading(1,85,175, "");
        mDbHelper.insertBG_Reading(1,84,180, "");
        mDbHelper.insertBG_Reading(1,83,185, "");
        mDbHelper.insertBG_Reading(1,82,190, "");
        mDbHelper.insertBG_Reading(1,80,195, "");
        mDbHelper.insertBG_Reading(1,89,200, "");
        mDbHelper.insertBG_Reading(1,88,205, "");
        mDbHelper.insertBG_Reading(1,87,210, "");
        mDbHelper.insertBG_Reading(1,86,215, "");
        mDbHelper.insertBG_Reading(1,85,220, "");
        mDbHelper.insertBG_Reading(1,84,225, "");
        mDbHelper.insertBG_Reading(1,83,230, "");
        mDbHelper.insertBG_Reading(1,82,235, "");
        mDbHelper.insertBG_Reading(1,80,240, "");
    }


    private void UpdateDisplayedJournal()
    {
        mDbHelper.dropTables();
        mDbHelper.createTables();
        insertTestRecord();

        ListView listView = (ListView) findViewById(R.id.list);

        journalEntries = mDbHelper.getAllEntries();

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

    @Override
    public void onClick(View v)
    {
        int viewID = v.getId();

        switch (viewID)
        {
            case R.id.fab:
                Intent parcelIntent = new Intent(MainActivity.this, CreateNewJournalEntryActivity.class);

                JournalEntry itemValue = new JournalEntry();

                parcelIntent.putExtra("item", itemValue.get_id());

                startActivityForResult(parcelIntent, journalEntries.getCount() + 1);
                break;
            default:
                break;
        }
    }

    private class JournalEntryListClickListener implements OnItemClickListener
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

            int tempID = itemValue.get_id();
            parcelIntent.putExtra("id", tempID);

            startActivityForResult(parcelIntent, itemPosition);
        }
    }
}


package com.home.sewright22.bg_control;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private JournalEntryList journalEntries = new JournalEntryList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.list);
        setSupportActionBar(toolbar);

        JournalEntry first = new JournalEntry();
        first.setFood("Cake");
        first.setCarbCount(24);
        first.setStartingBG(123);
        first.setInitialBolus(2);
        journalEntries.addJournalEntry(first);
        UpdateDisplayedJournal();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent parcelIntent = new Intent(MainActivity.this, JournalEntryDetailsActivity.class);

                JournalEntry itemValue = new JournalEntry();

                parcelIntent.putExtra("item", itemValue);

                startActivityForResult(parcelIntent, 1);
            }
        });

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent parcelIntent = new Intent(MainActivity.this, JournalEntryDetailsActivity.class);

                int itemPosition = position;

                ListView listView = (ListView) findViewById(R.id.list);

                // ListView Clicked item value
                JournalEntry itemValue = (JournalEntry) listView.getItemAtPosition(position);

                parcelIntent.putExtra("item", itemValue);

                startActivityForResult(parcelIntent, 1);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void UpdateDisplayedJournal() {
        ListView listView = (ListView) findViewById(R.id.list);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<JournalEntry> adapter = new ArrayAdapter<JournalEntry>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, journalEntries.getJournalEntries());

        listView.setAdapter(adapter);
        //final TextView textViewToChange = (TextView) findViewById(R.id.log);
        //textViewToChange.setText(text.toString());
    }
}

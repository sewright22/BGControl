package com.home.sewright22.bg_control.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.FoodRetrieval.CarbDbXmlParser;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;

public class CreateNewJournalEntryActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener
{
    private JournalEntryDbHelper mDbHelper;
    private JournalEntry entry;
    private String m_text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_journal_entry);
        mDbHelper = new JournalEntryDbHelper(this);
        entry = new JournalEntry();
        TextView time = (TextView) findViewById(R.id.text_new_start_time);
        Spinner food = (Spinner) findViewById(R.id.text_new_food);
        EditText text_carbs = (EditText) findViewById(R.id.text_new_carbs);
        //EditText text_bg = (EditText) findViewById(R.id.text_new_starting_bg);
        Button btn_addFood = (Button) findViewById(R.id.btn_new_add_food);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_spinner_item, mDbHelper.getAllFoods());
        food.setAdapter(adapter);
        food.setOnItemSelectedListener(new OnFoodItemSelectedListener());
        //food.setInputType(InputType.TYPE_CLASS_TEXT);
        text_carbs.setInputType(InputType.TYPE_CLASS_NUMBER);
        //text_bg.setInputType(InputType.TYPE_CLASS_NUMBER);

        time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));

        RadioGroup radGrp = (RadioGroup) findViewById(R.id.rad_grp);

        btn_addFood.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewJournalEntryActivity.this);
                builder.setTitle("Title");


// Set up the input
                final EditText input = new EditText(CreateNewJournalEntryActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JournalEntryDbHelper mDbHelper = new JournalEntryDbHelper(CreateNewJournalEntryActivity.this);
                        mDbHelper.insertFood(input.getText().toString());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateNewJournalEntryActivity.this,
                                                                                android.R.layout.simple_spinner_item, mDbHelper.getAllFoods());

                        Spinner food = (Spinner) findViewById(R.id.text_new_food);
                        food.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        radGrp.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:
                saveData();
                long newID = mDbHelper.insertJournalEntry(entry);
                Intent parcelIntent = new Intent(CreateNewJournalEntryActivity.this, MainActivity.class);
                parcelIntent.putExtra("entryID", (int)newID);
                setResult(Activity.RESULT_OK, parcelIntent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //BolusType check changed.
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        int id = checkedId;
        EditText initial = (EditText) findViewById(R.id.text_new_inital_bolus);
        EditText bolus_time = (EditText) findViewById(R.id.text_new_bolus_time);
        EditText percentUpFront = (EditText) findViewById(R.id.text_percent_up_front);
        EditText percentOverTime = (EditText) findViewById(R.id.text_new_percent_over_time);
        switch (id)
        {
            case -1:
                break;
            case R.id.rad_instant:
                initial.setVisibility(View.VISIBLE);
                bolus_time.setVisibility(View.GONE);
                percentUpFront.setVisibility(View.GONE);
                percentOverTime.setVisibility(View.GONE);
                //entry.setBolus_Type(R.integer.bolus_instant);
                break;
            case R.id.rad_extended:
                initial.setVisibility(View.GONE);
                bolus_time.setVisibility(View.VISIBLE);
                percentUpFront.setVisibility(View.VISIBLE);
                percentOverTime.setVisibility(View.VISIBLE);
                //entry.setBolus_Type(R.integer.bolus_extended);
                break;
            default:
                break;
        }
    }

    private void saveData()
    {
        try
        {
            TextView time = (TextView) findViewById(R.id.text_new_start_time);
            Spinner food = (Spinner) findViewById(R.id.text_new_food);
            EditText text_carbs = (EditText) findViewById(R.id.text_new_carbs);
            EditText text_inital_bolus = (EditText) findViewById(R.id.text_new_inital_bolus);
            EditText text_bolus_time = (EditText) findViewById(R.id.text_new_bolus_time);

            entry.set_foodID(mDbHelper.getFoodId(food.getSelectedItem().toString()));
            entry.set_carbCount(Integer.parseInt(text_carbs.getText().toString()));
        }
        catch (Exception e)
        {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    public class WebsiteRetriever extends AsyncTask<String, Void, String>
    {
        private Exception exception;

        protected String doInBackground(String... urlString)
        {
            StringBuffer chaine = new StringBuffer("");
            try
            {
                URL url = new URL(urlString[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null)
                {
                    chaine.append(line);
                }

            }
            catch (IOException e)
            {
                // writing exception to log
                e.printStackTrace();
            }

            return chaine.toString();
        }

        protected void onPostExecute(String feed)
        {
            String retVal;
            EditText text_carbs = (EditText) findViewById(R.id.text_carbs);
            InputStream stream = new ByteArrayInputStream(feed.getBytes(StandardCharsets.UTF_8));
            CarbDbXmlParser parser = new CarbDbXmlParser();
            try
            {
                retVal = parser.parse(stream);
                text_carbs.setText(retVal.toString());
            }
            catch (XmlPullParserException e)
            {
            }
            catch (IOException e)
            {
            }
        }
    }

    private class OnFoodItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            String food = (String)parent.getItemAtPosition(position);
            String test = food.toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    }
}

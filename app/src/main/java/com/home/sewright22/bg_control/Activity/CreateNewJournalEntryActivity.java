package com.home.sewright22.bg_control.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.FoodRetrieval.CarbDbXmlParser;
import com.home.sewright22.bg_control.FoodRetrieval.UrlBuilder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_journal_entry);
        mDbHelper = new JournalEntryDbHelper(this);
        entry = (JournalEntry) getIntent().getExtras().getParcelable("item");
        TextView time = (TextView) findViewById(R.id.text_start_time);
        EditText food = (EditText) findViewById(R.id.text_food);
        EditText text_carbs = (EditText) findViewById(R.id.text_carbs);
        EditText text_bg = (EditText) findViewById(R.id.text_starting_bg);
        EditText text_inital_bolus = (EditText) findViewById(R.id.text_inital_bolus);
        EditText text_finalBG = (EditText) findViewById(R.id.text_final_bg);
        EditText text_extended_bolus = (EditText) findViewById(R.id.text_extended_bolus);
        EditText text_bolus_time = (EditText) findViewById(R.id.text_bolus_time);

        food.setInputType(InputType.TYPE_CLASS_TEXT);
        text_carbs.setInputType(InputType.TYPE_CLASS_NUMBER);
        text_bg.setInputType(InputType.TYPE_CLASS_NUMBER);
        text_finalBG.setInputType(InputType.TYPE_CLASS_NUMBER);
        text_finalBG.setVisibility(View.VISIBLE);

        time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));
        food.setText(entry.getFood());

        if (entry.getCarbs() > 0)
        {
            text_carbs.setText(String.valueOf(entry.getCarbs()));
            text_carbs.setEnabled(false);
        }

        if (entry.getStartingBG() > 0)
        {
            text_bg.setText(String.valueOf(entry.getStartingBG()));
            text_bg.setEnabled(false);
        }

        if (entry.getInitialBolus() > 0)
        {
            text_inital_bolus.setText(String.valueOf(entry.getInitialBolus()));
            text_inital_bolus.setEnabled(false);
        }

        if (entry.getExtendedBolus() > 0)
        {
            text_extended_bolus.setText(String.valueOf(entry.getExtendedBolus()));
            text_extended_bolus.setEnabled(false);
        }

        if (entry.getBolus_Time() > 0)
        {
            text_bolus_time.setText(String.valueOf(entry.getBolus_Time()));
            text_bolus_time.setEnabled(false);
        }

        if (entry.getFinalBG() > 0)
        {
            text_finalBG.setText(String.valueOf(entry.getFinalBG()));
            text_finalBG.setEnabled(false);
        }


        RadioGroup radGrp = (RadioGroup) findViewById(R.id.rad_grp);

        radGrp.setOnCheckedChangeListener(this);

        if (entry.getBolus_Type() == R.integer.bolus_instant)
        {
            RadioButton initial = (RadioButton) findViewById(R.id.rad_instant);
            initial.setChecked(true);
        }
        else if (entry.getBolus_Type() == R.integer.bolus_extended)
        {
            RadioButton extended = (RadioButton) findViewById(R.id.rad_square);
            extended.setChecked(true);
        }
        else if (entry.getBolus_Type() == R.integer.bolus_dual_wave)
        {
            RadioButton dual = (RadioButton) findViewById(R.id.rad_dual_wave);
            dual.setChecked(true);
        }

        //time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));
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
                mDbHelper.insertJournalEntry(entry);
                Intent parcelIntent = new Intent(CreateNewJournalEntryActivity.this, MainActivity.class);
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
        EditText initial = (EditText) findViewById(R.id.text_inital_bolus);
        EditText extend = (EditText) findViewById(R.id.text_extended_bolus);
        EditText bolus_time = (EditText) findViewById(R.id.text_bolus_time);
        switch (id)
        {
            case -1:
                break;
            case R.id.rad_instant:
                initial.setVisibility(View.VISIBLE);
                extend.setVisibility(View.GONE);
                bolus_time.setVisibility(View.GONE);
                entry.setBolus_Type(R.integer.bolus_instant);
                break;
            case R.id.rad_square:
                initial.setVisibility(View.GONE);
                extend.setVisibility(View.VISIBLE);
                bolus_time.setVisibility(View.VISIBLE);
                entry.setBolus_Type(R.integer.bolus_extended);
                break;
            case R.id.rad_dual_wave:
                initial.setVisibility(View.VISIBLE);
                extend.setVisibility(View.VISIBLE);
                bolus_time.setVisibility(View.VISIBLE);
                entry.setBolus_Type(R.integer.bolus_dual_wave);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                EditText text_food = (EditText) findViewById(R.id.text_food);
                String ndbno = data.getExtras().getString("ndbno");
                String food = data.getExtras().getString("food");

                text_food.setText(food);

                String urlString = UrlBuilder.buildCarbSearchUrl(ndbno);
                new WebsiteRetriever().execute(urlString);
            }
        }
    }

    public void searchFoodClicked(View view)
    {
        EditText food = (EditText) findViewById(R.id.text_food);


        Intent intent = new Intent(CreateNewJournalEntryActivity.this, FoodListResultActivity.class);
        intent.putExtra("SearchString", food.getText().toString());
        startActivityForResult(intent, 1);
    }

    private void saveData()
    {
        try
        {
            TextView time = (TextView) findViewById(R.id.text_start_time);
            EditText food = (EditText) findViewById(R.id.text_food);
            EditText text_carbs = (EditText) findViewById(R.id.text_carbs);
            EditText text_bg = (EditText) findViewById(R.id.text_starting_bg);
            EditText text_inital_bolus = (EditText) findViewById(R.id.text_inital_bolus);
            EditText text_extended_bolus = (EditText) findViewById(R.id.text_extended_bolus);
            EditText text_bolus_time = (EditText) findViewById(R.id.text_bolus_time);
            EditText text_finalBG = (EditText) findViewById(R.id.text_final_bg);


            entry.setFood(food.getText().toString());
            entry.setCarbCount(Integer.parseInt(text_carbs.getText().toString()));
            entry.setStartingBG(Integer.parseInt(text_bg.getText().toString()));

            if (entry.getBolus_Type() == R.integer.bolus_instant ||
                    entry.getBolus_Type() == R.integer.bolus_dual_wave)
            {
                entry.setInitialBolus(Double.parseDouble(text_inital_bolus.getText().toString()));
            }

            if (entry.getBolus_Type() == R.integer.bolus_extended ||
                    entry.getBolus_Type() == R.integer.bolus_dual_wave)
            {
                entry.setExtendedBolus(Double.parseDouble(text_extended_bolus.getText().toString()));
                entry.setBolus_Time(Integer.parseInt(text_bolus_time.getText().toString()));
            }

            if (text_finalBG.getText().length() == 0)
            {
                entry.setFinalBG(0);
            }
            else
            {
                entry.setFinalBG(Integer.parseInt(text_finalBG.getText().toString()));
            }
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
}

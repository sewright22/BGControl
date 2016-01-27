package com.home.sewright22.bg_control.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.home.sewright22.bg_control.FoodRetrieval.FoodDbXmlParser;
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
import java.util.List;

public class FoodListResultActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_result);
        String food = getIntent().getExtras().getString("SearchString");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_food_results);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int itemPosition = position;

                ListView listView = (ListView) findViewById(R.id.list_food_results);

                FoodDbXmlParser.FoodSearchResult itemValue = (FoodDbXmlParser.FoodSearchResult) listView.getItemAtPosition(itemPosition);

                Intent returnIntent = new Intent(FoodListResultActivity.this, JournalEntryDetailsActivity.class);
                returnIntent.putExtra("ndbno", itemValue.ndbno);
                returnIntent.putExtra("food", itemValue.name);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        String urlString = UrlBuilder.buildFoodSearchUrl(food);
        new WebsiteRetriever().execute(urlString);
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
            List<FoodDbXmlParser.FoodSearchResult> list;
            ListView listView = (ListView) findViewById(R.id.list_food_results);
            InputStream stream = new ByteArrayInputStream(feed.getBytes(StandardCharsets.UTF_8));
            FoodDbXmlParser parser = new FoodDbXmlParser();
            try
            {
                list = parser.parse(stream);
                ArrayAdapter<FoodDbXmlParser.FoodSearchResult> adapter = new ArrayAdapter<FoodDbXmlParser.FoodSearchResult>(FoodListResultActivity.this,
                                                                                                                            android.R.layout.simple_list_item_2,
                                                                                                                            android.R.id.text1,
                                                                                                                            list);

                listView.setAdapter(adapter);
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

package com.home.sewright22.bg_control.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.FoodRetrieval.CarbDbXmlParser;
import com.home.sewright22.bg_control.FoodRetrieval.UrlBuilder;
import com.home.sewright22.bg_control.Model.Bolus;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

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
import java.util.ArrayList;

public class JournalEntryDetailsActivity extends AppCompatActivity
{
    private JournalEntryDbHelper mDbHelper;
    private JournalEntry entry;
    private Bolus _bolus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry_details);
        mDbHelper = new JournalEntryDbHelper(this);
        int entryID = getIntent().getExtras().getInt("id");
        TextView time = (TextView) findViewById(R.id.text_start_time);
        TextView food = (TextView) findViewById(R.id.text_food);
        TextView text_carbs = (TextView) findViewById(R.id.text_carbs);
        TextView text_starting_bg = (TextView) findViewById(R.id.text_starting_bg);

        TextView text_bolus_amount = (TextView) findViewById(R.id.text_detail_bolus_amount);
        TextView text_percent_up_front = (TextView) findViewById(R.id.text_detail_percent_up_front);
        TextView text_percent_over_time = (TextView) findViewById(R.id.text_detail_percent_over_time);
        TextView text_length_of_time = (TextView) findViewById(R.id.text_detail_length_of_time);

        entry = mDbHelper.getSpecificEntry(entryID);
        _bolus = mDbHelper.getBolus(entry.get_bolusID());
        time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));
        food.setText("Meal: " + mDbHelper.getFoodName(entry.get_foodID()));
        text_carbs.setText("Carb Count: " + entry.getCarbs());
        text_bolus_amount.setText("Bolus Amount: " + _bolus.get_amount() + " unit(s)");
        text_percent_up_front.setText("Percent Up Front: " + _bolus.get_percent_up_front() + "%");
        text_percent_over_time.setText("Percent Over Time: " + _bolus.get_percent_over_time() + "%");
        text_length_of_time.setText("Length of Time: " + _bolus.get_length_of_time() + " hour(s)");

        GraphView point_graph = (GraphView) findViewById(R.id.graph);

        ArrayList<DataPoint> points = mDbHelper.getBG_Readings(entryID);

        DataPoint[] pointArray = new DataPoint[points.size()];

        PointsGraphSeries<DataPoint> point_series = new PointsGraphSeries<DataPoint>(points.toArray(pointArray));

        point_graph.addSeries(point_series);
        point_series.setShape(PointsGraphSeries.Shape.POINT);
        point_series.setColor(Color.RED);
        point_series.setSize(10);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(point_graph);


        int labelNum = (int)(point_series.getHighestValueX()/9);

        String[] xAxisLabelArray = new String[9];
        xAxisLabelArray[0] = "0";
        xAxisLabelArray[1] = "" + labelNum * 1;
        xAxisLabelArray[2] = "" + labelNum * 2;
        xAxisLabelArray[3] = "" + labelNum * 3;
        xAxisLabelArray[4] = "" + labelNum * 4;
        xAxisLabelArray[5] = "" + labelNum * 5;
        xAxisLabelArray[6] = "" + labelNum * 6;
        xAxisLabelArray[7] = "" + labelNum * 7;
        xAxisLabelArray[8] = "" + labelNum * 8;

        if(points.size() > 0)
        {
            text_starting_bg.setText("Starting BG: " + (int) points.get(0).getY());
        }
        else
        {
            text_starting_bg.setText("Starting BG: Haven't received and readings yet.");
        }


        //staticLabelsFormatter.setHorizontalLabels(xAxisLabelArray);
        point_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }
}

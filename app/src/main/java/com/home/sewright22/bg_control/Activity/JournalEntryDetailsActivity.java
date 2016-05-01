package com.home.sewright22.bg_control.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import com.home.sewright22.bg_control.Contract.JournalEntryDbHelper;
import com.home.sewright22.bg_control.Model.BG_Reading;
import com.home.sewright22.bg_control.Model.Bolus;
import com.home.sewright22.bg_control.Model.JournalEntry;
import com.home.sewright22.bg_control.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.util.ArrayList;

public class JournalEntryDetailsActivity extends AppCompatActivity
{
    private JournalEntryDbHelper mDbHelper;
    private JournalEntry entry;
    private Bolus _bolus;
    private ArrayList<DataPoint> bgReadings;
    private ArrayList<DataPoint> activeInsulinReadings;
    private PointsGraphSeries<DataPoint> pointSeriesBG;
    private PointsGraphSeries<DataPoint> pointSeriesActiveInsulin;

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
        TextView text_peaked_duration = (TextView) findViewById(R.id.text_detail_peaked_duration);

        entry = mDbHelper.getSpecificEntry(entryID);
        _bolus = mDbHelper.getBolus(entry.get_bolusID());
        time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));
        food.setText("Meal: " + mDbHelper.getFoodName(entry.get_foodID()));
        text_carbs.setText("Carb Count: " + entry.getCarbs());
        text_bolus_amount.setText("Bolus Amount: " + _bolus.get_amount() + " unit(s)");
        text_percent_up_front.setText("Percent Up Front: " + _bolus.get_percent_up_front() + "%");
        text_percent_over_time.setText("Percent Over Time: " + _bolus.get_percent_over_time() + "%");
        text_length_of_time.setText("Length of Time: " + _bolus.get_length_of_time() + " hour(s)");

        createBG_Graph();
        createActiveInsulinGraph();

        if(bgReadings.size() > 0)
        {
            text_starting_bg.setText("Starting BG: " + (int) bgReadings.get(0).getY());
        }
        else
        {
            text_starting_bg.setText("Starting BG: Haven't received and readings yet.");
        }

        if(mDbHelper.getLatestReadingForEntry(entryID).getSlopeName().equals(""))
        {
            BG_Reading peakBG = mDbHelper.getPeakReadingForEntry(entryID);
            text_peaked_duration.setText("You peaked at " + peakBG.getValue()  + " after " + peakBG.getMinutesSinceStart() + " minutes.");
        }
        else
        {
            text_peaked_duration.setText("You don't seem to have peaked from this meal yet.");
        }

    }

    @NonNull
    private void createBG_Graph()
    {
        GraphView point_graph = (GraphView) findViewById(R.id.graph);

        bgReadings = mDbHelper.getBG_Readings(entry.get_id());

        DataPoint[] pointArray = new DataPoint[bgReadings.size()];

        pointSeriesBG = new PointsGraphSeries<DataPoint>(bgReadings.toArray(pointArray));
        point_graph.addSeries(pointSeriesBG);
        pointSeriesBG.setShape(PointsGraphSeries.Shape.POINT);
        pointSeriesBG.setColor(Color.RED);
        pointSeriesBG.setSize(10);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(point_graph);
        point_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private void createActiveInsulinGraph()
    {
        GraphView point_graph = (GraphView) findViewById(R.id.graph_active_insulin);

        activeInsulinReadings = new ArrayList<DataPoint>();
        DataPoint[] pointArray = new DataPoint[bgReadings.size()];

        double bolusAmount = _bolus.get_amount();
        double percentagePerMinute = 0.00416666666666;

        for (DataPoint point :
                bgReadings)
        {

            double activeInsulin = (bolusAmount - (bolusAmount*percentagePerMinute*point.getX()));
            activeInsulinReadings.add(new DataPoint(point.getX(), activeInsulin));
        }

        pointSeriesActiveInsulin = new PointsGraphSeries<DataPoint>(activeInsulinReadings.toArray(pointArray));
        point_graph.addSeries(pointSeriesActiveInsulin);
        pointSeriesActiveInsulin.setShape(PointsGraphSeries.Shape.POINT);
        pointSeriesActiveInsulin.setColor(Color.RED);
        pointSeriesActiveInsulin.setSize(10);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(point_graph);
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

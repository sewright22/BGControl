package com.home.sewright22.bg_control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;

public class JournalEntryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry_details);
        JournalEntry entry = (JournalEntry)getIntent().getExtras().getParcelable("item");
        TextView time = (TextView)findViewById(R.id.text_start_time);
        EditText food = (EditText)findViewById(R.id.text_food);
        EditText text_carbs = (EditText)findViewById(R.id.text_carbs);
        EditText text_bg = (EditText)findViewById(R.id.text_starting_bg);
        EditText text_inital_bolus = (EditText)findViewById(R.id.text_inital_bolus);
        EditText text_finalBG = (EditText)findViewById(R.id.text_final_bg);

        food.setInputType(InputType.TYPE_CLASS_TEXT);
        text_carbs.setInputType(InputType.TYPE_CLASS_NUMBER);
        text_bg.setInputType(InputType.TYPE_CLASS_NUMBER);
        text_finalBG.setInputType(InputType.TYPE_CLASS_NUMBER);;

        food.setText(entry.getFood());
        text_carbs.setText(String.valueOf(entry.getCarbs()));
        text_bg.setText(String.valueOf(entry.getStartingBG()));
        text_inital_bolus.setText(String.valueOf(entry.getInitialBolus()));
        text_finalBG.setText(String.valueOf(entry.getFinalBG()));
        time.setText(DateFormat.getTimeInstance().format(entry.getStartTime()));
    }

}

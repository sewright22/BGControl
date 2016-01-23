package com.home.sewright22.bg_control;

import java.util.ArrayList;

/**
 * Created by steve on 1/22/2016.
 */
public class JournalEntryList {
    private ArrayList<JournalEntry> journalEntries = new ArrayList<JournalEntry>();
    private ArrayList<String> values = new ArrayList<String>();

    public void addJournalEntry(JournalEntry journalEntry)
    {
        journalEntries.add(journalEntry);
    }

    public void addJournalEntry(JournalEntry journalEntry, int pos)
    {
        journalEntries.remove(pos);
        journalEntries.add(journalEntry);
    }

    public ArrayList<String> getValues() {
        for (JournalEntry entry:journalEntries
                )
        {
            values.add(entry.toString());
        }

        return values;
    }

    public ArrayList<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public int getCount()
    {
        return journalEntries.size();
    }
}

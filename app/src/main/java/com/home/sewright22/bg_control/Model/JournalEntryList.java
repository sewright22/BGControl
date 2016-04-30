package com.home.sewright22.bg_control.Model;

import com.home.sewright22.bg_control.Model.JournalEntry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by steve on 1/22/2016.
 */
public class JournalEntryList implements Iterable<JournalEntry>
{
    private ArrayList<JournalEntry> journalEntries = new ArrayList<JournalEntry>();
    private ArrayList<String> values = new ArrayList<String>();

    public void insertJournalEntry(JournalEntry journalEntry)
    {
        journalEntries.add(journalEntry);
    }

    public void insertJournalEntry(JournalEntry journalEntry, int pos)
    {
        //journalEntries.get(pos).update(journalEntry);
    }

    public void clear()
    {
        journalEntries.clear();
    }

    public ArrayList<String> getValues()
    {
        for (JournalEntry entry : journalEntries)
        {
            values.add(entry.toString());
        }

        return values;
    }

    public ArrayList<JournalEntry> getJournalEntries()
    {
        return journalEntries;
    }

    public int getCount()
    {
        return journalEntries.size();
    }

    @Override
    public Iterator<JournalEntry> iterator()
    {
        return journalEntries.iterator();
    }
}

package com.home.sewright22.bg_control.FoodRetrieval;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 1/26/2016.
 */
public class FoodDbXmlParser
{
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        }
        finally
        {
            in.close();
        }
    }

    private List<FoodSearchResult> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "list");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item"))
            {
                entries.add(readFoodSearchResult(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return entries;
    }

    public static class FoodSearchResult
    {
        public final String name;
        public final String ndbno;

        private FoodSearchResult(String name, String ndbno)
        {
            this.name = name;
            this.ndbno = ndbno;
        }

        public String toString()
        {
            return name;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private FoodSearchResult readFoodSearchResult(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String name = null;
        String ndbno = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("name"))
            {
                name = readFoodName(parser);
            }
            else if (tagName.equals("ndbno"))
            {
                ndbno = readNdbno(parser);

            }
            else
            {
                skip(parser);
            }
        }
        return new FoodSearchResult(name, ndbno);
    }

    private String readFoodName(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return title;
    }

    private String readNdbno(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "ndbno");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ndbno");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

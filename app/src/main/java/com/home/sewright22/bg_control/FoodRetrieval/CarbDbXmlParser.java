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
public class CarbDbXmlParser
{
    // We don't use namespaces
    private static final String ns = null;

    public String parse(InputStream in) throws XmlPullParserException, IOException
    {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readReport(parser);
    }

    private String readReport(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String retVal = "";

        parser.require(XmlPullParser.START_TAG, ns, "report");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("foods"))
            {
                retVal = readFoods(parser);
            }
            else
            {
                skip(parser);
            }
        }
        return retVal;
    }

    private String readFoods(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String retVal = "";

        parser.require(XmlPullParser.START_TAG, ns, "foods");

        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("food"))
            {
                retVal = readFood(parser);
            }
            else
            {
                skip(parser);
            }
        }

        return retVal;
    }

    private String readFood(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String retVal = "";

        parser.require(XmlPullParser.START_TAG, ns, "food");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("nutrients"))
            {
                retVal = readNutrients(parser);
            }
            else
            {
                skip(parser);
            }
        }
        return retVal;
    }

    private String readNutrients(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String retVal = "";

        parser.require(XmlPullParser.START_TAG, ns, "nutrients");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("nutrient"))
            {
                retVal = readNutrient(parser);
                return retVal;
            }
            else
            {
                skip(parser);
            }
        }
        return retVal;
    }

    private String readNutrient(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "nutrient");
        String carbs = parser.getAttributeValue(3);
        return carbs;
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

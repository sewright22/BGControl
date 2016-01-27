package com.home.sewright22.bg_control.FoodRetrieval;

/**
 * Created by steve on 1/25/2016.
 */
public class UrlBuilder
{
    private static final String API_KEY = "&api_key=UbxExne9TyVsE3AiIyjzgoX9feAd6NYaAXCdNdP8";
    private static final String URL_FOOD_START = "http://api.nal.usda.gov/ndb/search/?format=xml";
    private static final String URL_CARB_START = "http://api.nal.usda.gov/ndb/nutrients/?";
    private static final String SEARCH_FOOD = "&q=";
    private static final String SEARCH_MAX = "&max=10";
    private static final String SEARCH_OFFSET = "&offset=0";
    private static final String SEARCH_FORMAT = "&format=xml";
    private static final String SEARCH_NDBNO = "ndbno=";
    private static final String SEARCH_NUTRIENT = "&nutrients=205";

    public static String buildFoodSearchUrl(String food)
    {
        return  URL_FOOD_START +
                SEARCH_FOOD +
                food +
                SEARCH_MAX +
                SEARCH_OFFSET +
                API_KEY;
    }

    public static String buildCarbSearchUrl(String id)
    {
        return  URL_CARB_START +
                SEARCH_NDBNO +
                id +
                SEARCH_FORMAT +
                API_KEY +
                SEARCH_NUTRIENT;
    }
}

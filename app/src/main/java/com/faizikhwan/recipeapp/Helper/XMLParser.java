package com.faizikhwan.recipeapp.Helper;

import android.content.Context;

import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.Model.RecipeType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParser {

    public static ArrayList<RecipeType> parseXMLRecipeTypes(Context context, String filename) {
        ArrayList<RecipeType> recipetypes = new ArrayList<>();
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open(filename);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            RecipeType currentPlayer = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String eltName;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();

                        if ("recipetype".equals(eltName)) {
                            currentPlayer = new RecipeType();
                            recipetypes.add(currentPlayer);
                        } else if (currentPlayer != null) {
                            if ("type".equals(eltName)) {
                                currentPlayer.setType(parser.nextText());
                            }
                        }
                        break;
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipetypes;
    }

    public static ArrayList<Recipe> parseXMLRecipes(Context context, String filename) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open(filename);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            Recipe currentPlayer = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String eltName;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();

                        if ("recipe".equals(eltName)) {
                            currentPlayer = new Recipe();
                            recipes.add(currentPlayer);
                        } else if (currentPlayer != null) {
                            if ("id".equals(eltName)) {
                                currentPlayer.setId(Integer.parseInt(parser.nextText()));
                            } else if ("title".equals(eltName)) {
                                currentPlayer.setTitle(parser.nextText());
                            } else if ("ingredient".equals(eltName)) {
                                currentPlayer.setIngredient(parser.nextText());
                            } else if ("step".equals(eltName)) {
                                currentPlayer.setStep(parser.nextText());
                            } else if ("type".equals(eltName)) {
                                currentPlayer.setType(parser.nextText());
                            }
                        }
                        break;
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }

}

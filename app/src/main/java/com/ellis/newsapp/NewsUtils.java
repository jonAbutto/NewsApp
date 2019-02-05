/**
 * news helper class to help in
 * making http request and creatng
 * JSONObjects
 */

package com.ellis.newsapp;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsUtils {
    //log tag for the {@link NewsUtils} class
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();
    //sleep time, frced delay before the news articles are displayed on the screen
    private static final int SLEEP_TIME = 1500;
    //response code 200- okay
    private static final int RESPONSE_CODE_OK = 200;
    //max read time out
    private static final int READ_TIME_OUT = 10000;
    //max connection time out
    private static final int CONNECTION_TIME_OUT = 15000;
    //create an empty class constructor

    public NewsUtils() {

    }

    //perform a query  to the Guardian Api and @return a list of {@NewsCustomClass} objects
    public static List<NewsCustomClass> fetchNewsFeeds(String requestFeeds) {
        //force  background thread to delay for a specified duration
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//   create url object
        URL url = createUrl(requestFeeds);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making http request ", e);
        }
        List<NewsCustomClass> news = extractFeaturedFromJson(jsonResponse);
        return news;
    }

    //create a  new url object from the given String url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem parsing the URL. ", e);
        }
        return url;
    }

    /**
     * making http request
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = " ";
        //if the url is emty then return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIME_OUT);
            urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if the request was succeessful, response code 200 wil be displayed
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                inputStream = urlConnection.getErrorStream();
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, " Problem Retrieving the earthquake JSON results. ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * convert the {@link InputStream } into a stream which contains the whole
     * JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        //checkingif there is inputs in the InputStream
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            //output the content of the line if its not null
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * return a liat of {@link NewsCustomClass} objects that has built up
     * from parsing the given jsin response
     */
    @Nullable
    private static List<NewsCustomClass> extractFeaturedFromJson(String newsJson) {
        //if json String is empty or null then return early
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        //empty ArrayList for holding the news items
        List<NewsCustomClass> newsItems = new ArrayList<>();
        //try catch block to handle exceptions that may
        //arise when creating JSONObjects

        try {
            //create JSONObject from the news api url
            JSONObject baseObject = new JSONObject(newsJson);

            JSONObject newsResponse = baseObject.getJSONObject("response");
            //get the jsonArray items
            JSONArray resultsArray = newsResponse.getJSONArray("results");
            //looping through te news items

            for (int i = 0; i < resultsArray.length(); i++) {
                // getting the JSONObject and all its attributes
                JSONObject allNewsFeeds = resultsArray.getJSONObject(i);
                //find the key for the author
                JSONObject authorKey = allNewsFeeds.getJSONObject("fields");
// return data in the reference key otherwise return null
                String name = allNewsFeeds.optString("sectionName");
                String title = allNewsFeeds.optString("webTitle");
                String newsUrl = allNewsFeeds.optString("webUrl");
                String byLine = authorKey.optString("byline");
                String time = allNewsFeeds.optString("webPublicationDate");

                NewsCustomClass newsClass = new NewsCustomClass(name, title, newsUrl, byLine,time);

                newsItems.add(newsClass);
            }

        } catch (JSONException e) {
            //catch an exception if an error occur when executing the codes above
            // so the app doesn't crash
            Log.e(LOG_TAG, "problems parsing the news JSON results. ", e);

        }
        //return a list of news items
        return newsItems;
    }


}

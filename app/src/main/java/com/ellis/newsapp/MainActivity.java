
package com.ellis.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsCustomClass>> {
    private static final String TAG = "MainActivity";
    private ListView newsListView;
    private static final String BASE_URL = "https://content.guardianapis.com";
    private static final String KEY_SEARCH = "search";
    private static final String API_KEY = "api-key";
    private static final String API_KEY_TEST = "58119a8b-5752-4a3c-981a-f75108469d24";
    private static final String KEY_FIELDS = "show-fields";
    private static final String KEY_LINE = "byline";
    //create {@link NewsAdapter} object the holds the list items data
    private NewsAdapter newsAdapter;
    //TextView that is displayed when the list isEmpty
    private TextView emptyStateTextView;
    //loading indicator
    View loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the adapter content to ne held by the {@Link ListView} objcet
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsCustomClass>());
        newsListView = (ListView) findViewById(R.id.list);

        //find the textview that displayed then there are not items in the listview
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateTextView);


        //empty adapter that takes list of news items as input
        newsListView.setAdapter(newsAdapter);

        //set intent on the list item to direct
        //user to the news website
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //find the current newsitem that was clicked on
                NewsCustomClass currentNewsItem = newsAdapter.getItem(position);
                //convert the string uri into a uri object( to parse the intent constructor)
                Uri newsUri = Uri.parse(currentNewsItem.getWebUrl());
                //create intent to view the new uri
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //send intent to launch new activity
                startActivity(newsIntent);
            }
        });

        /**check if there is internet connection*/
        boolean isConnected =  false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //when the connection is available
            isConnected =true;
            getLoaderManager().initLoader(0, null, this);
        }else{
            isConnected = false;
            //text to display when there is not internt connectivity
            emptyStateTextView.setText("Plesase check your Internet Connection and try again");
        }


    }

    //create a new loader
    @Override
    public Loader<List<NewsCustomClass>> onCreateLoader(int i, Bundle bundle) {

        /**
         * create method to handle {@LINK Uri.Builder} class
         * build this url using Uri.builder
         * https://content.guardianapis.com/search?api-key=58119a8b-5752-4a3c-981a-f75108469d24&show-fields=byline
         */
        Uri newsUri = Uri.parse(BASE_URL);
        Uri.Builder builder = newsUri.buildUpon()
                .appendPath(KEY_SEARCH)
                .appendQueryParameter(API_KEY, API_KEY_TEST)
                .appendQueryParameter(KEY_FIELDS, KEY_LINE);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsCustomClass>> loader, List<NewsCustomClass> data) {
        //when view is alredy loaded, hide the loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        //display a text on the empty textview when there is no new news feed

        emptyStateTextView.setText(R.string.no_news_update);
        // clear the adapter of the previous  earthquake data
        newsAdapter.clear();

//            if there is a valid list of {@link NewsCustomClass}, then add them to the adapter's
//            data set. this will trigger the newsListView to update
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsCustomClass>> loader) {
//Loader reset, so we can clear out our existing data
        loader = null;
    }


}




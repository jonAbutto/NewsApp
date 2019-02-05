package com.ellis.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsCustomClass>> {
    private String webUrl;

    public NewsLoader(Context context,String webUrl) {
        super(context);
        this.webUrl = webUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
//background threads goes here
    @Override
    public List<NewsCustomClass> loadInBackground() {
        //return early if there there is no news updates
        if (webUrl == null ) {
            return null;
        }
        //{
        List<NewsCustomClass> newsUpdates = NewsUtils.fetchNewsFeeds(webUrl);
        return newsUpdates;
    }
}

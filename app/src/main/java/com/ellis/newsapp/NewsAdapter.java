package com.ellis.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<NewsCustomClass> {

    public NewsAdapter(@NonNull Context context, ArrayList<NewsCustomClass> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //find news at a position
        NewsCustomClass newsPosition = getItem(position);

        //check if there is an existing list item view
        //that can be reused otherwise, if the convertview
        //is null then inflate anew list item

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
        }

        //finding the textviews to display the heading of the news
        TextView tvName = (TextView) convertView.findViewById(R.id.sectionName);
        tvName.setText(newsPosition.getSectionName());
        //finding the textview to display the title of the news
        TextView tvTitle = (TextView) convertView.findViewById(R.id.webTitle);
        tvTitle.setText(newsPosition.getWebTitle());
        //find  and display the name of the author
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.author);
        tvAuthor.setText("Author: " + newsPosition.getAuthor());


//find and display date the article was posted
        TextView tvTime = (TextView) convertView.findViewById(R.id.date);
        String formatedArticleDate = formatedDate(newsPosition.getTime());
        tvTime.setText(formatedArticleDate);

        //find and display time in tis textview
        TextView time = (TextView) convertView.findViewById(R.id.time);
        //string for storing formatted time
        String formattedTime = formatTime(newsPosition.getTime());
        //display time
        time.setText(formattedTime);
        return convertView;

    }

    /**
     * helper method to help in formatting the date
     */
    private String formatedDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date outputedDate = null;
        try {
            outputedDate = dateFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat dateformatter = new SimpleDateFormat("LLL dd, yyyy", Locale.US);
        return dateformatter.format(outputedDate);
    }

    /**
     * helper method for formatting time
     */
    private String formatTime(String date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date timeOut = null;

        try {
            timeOut = timeFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        return timeFormatter.format(timeOut);
    }
}

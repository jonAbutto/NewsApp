package com.ellis.newsapp;
//custom class for JSON pbjects that will be returned when making Http request
public class NewsCustomClass {
//    sectionName
    private String  sectionName;
//    webTitle
    private String  webTitle;
//    new website URL
    private String  webUrl;
//article author
    private String author;
    //time the asticle was written
    private String time;
//    class constructor
    public NewsCustomClass(String name, String title, String url, String articleAuthor, String articleTime) {
        this.sectionName = name;
        this.webTitle = title;
        this.webUrl = url;
        this.author = articleAuthor;
        this.time = articleTime;
    }
//return the sectiona name of the news
    public String getSectionName() {
        return sectionName;
    }
//return the new title
    public String getWebTitle() {
        return webTitle;
    }
//return the news url
    public String getWebUrl() {
        return webUrl;
    }
//returns the name of the author
    public String getAuthor() {
        return author;
    }
//returns the time the article was writtten
    public String getTime() {
        return time;
    }
}

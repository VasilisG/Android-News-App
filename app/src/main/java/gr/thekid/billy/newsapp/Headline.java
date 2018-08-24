package gr.thekid.billy.newsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Headline {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public Headline(String author, String title, String description, String url, String urlToImage, String publishedAt){
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getAuthor(){
        return author;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

    public String getUrlToImage(){
        return urlToImage;
    }

    public String getPublishedAt(){
        return publishedAt;
    }

    public String getDatetime(){
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

        try {
            Date date = inFormat.parse(publishedAt);
            String outTime = outFormat.format(date);
            return outTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_STRING;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Author: " + author);
        builder.append(" Title: " + title);
        builder.append(" Description: " + description);
        builder.append(" Url: " + url);
        builder.append(" UrlToImage: " + urlToImage);
        builder.append(" PublishedAt: " + publishedAt);

        return builder.toString();
    }
}

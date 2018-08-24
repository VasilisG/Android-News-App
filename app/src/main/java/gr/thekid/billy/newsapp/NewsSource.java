package gr.thekid.billy.newsapp;


import java.util.ArrayList;

public class NewsSource{

    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
    private boolean favourite;

    public NewsSource(String id, String name, String description, String url, String category, String language, String country){
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

    public String getCategory(){
        return category;
    }

    public String getLanguage(){
        return language;
    }

    public String getCountry(){
        return country;
    }

    public boolean isFavourite(){
        return favourite;
    }

    public void setFavourite(boolean favourite){
        this.favourite = favourite;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("ID: " + id);
        builder.append(" Name: " + name);
        builder.append(" Description: " + description);
        builder.append(" Url: " + url);
        builder.append(" Category: " + category);
        builder.append(" Language: " + language);
        builder.append(" Country: " + country);

        return builder.toString();
    }

    private boolean equalsToSource(NewsSource source){

        if(source==null) return false;
        else return this.getId().equals(source.getId());
    }

    public boolean isInSourceList(ArrayList<NewsSource> sources){

        if(sources == null) return false;

        for(NewsSource source : sources){
            if(this.equalsToSource(source)){
                return true;
            }
        }
        return false;
    }
}

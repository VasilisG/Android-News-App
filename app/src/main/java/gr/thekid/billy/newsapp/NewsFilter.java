package gr.thekid.billy.newsapp;

import java.util.ArrayList;

public class NewsFilter {

    private ArrayList<NewsSource> newsSources;

    public NewsFilter(ArrayList<NewsSource> newsSources){
        this.newsSources = newsSources;
    }

    public ArrayList<NewsSource> getFilteredByCategory(String category){
        ArrayList<NewsSource> filteredSources = new ArrayList<NewsSource>();
        for(NewsSource source : newsSources){
            if(source.getCategory().equals(category)){
                filteredSources.add(source);
            }
        }
        return filteredSources;
    }

    public ArrayList<NewsSource> getFilteredByCountry(String country){
        ArrayList<NewsSource> filteredSources = new ArrayList<NewsSource>();
        for(NewsSource source : newsSources){
            if(source.getCountry().equals(country)){
                filteredSources.add(source);
            }
        }
        return filteredSources;
    }

    public ArrayList<NewsSource> getFilteredByLanguage(String language){
        ArrayList<NewsSource> filteredSources = new ArrayList<NewsSource>();
        for(NewsSource source : newsSources){
            if(source.getLanguage().equals(language)){
                filteredSources.add(source);
            }
        }
        return filteredSources;
    }

    public ArrayList<NewsSource> getFilteredByName(String name){
        String lowerCaseName = name.toLowerCase();
        ArrayList<NewsSource> filteredSources = new ArrayList<NewsSource>();
        for(NewsSource source : newsSources){
            String sourceLowerCaseName = source.getName().toLowerCase();
            if(sourceLowerCaseName.contains(lowerCaseName)){
                filteredSources.add(source);
            }
        }
        return filteredSources;
    }

    public ArrayList<NewsSource> getFavourite(){
        ArrayList<NewsSource> favouriteSources = new ArrayList<NewsSource>();
        for(NewsSource source : newsSources){
            if(source.isFavourite()){
                favouriteSources.add(source);
            }
        }
        return favouriteSources;
    }
}

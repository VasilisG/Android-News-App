package gr.thekid.billy.newsapp;

import java.util.LinkedHashMap;

public class CategoryLookup extends AttributeLookup{

    public CategoryLookup(){
        super();
        attributeLookup.put("Business", "business");
        attributeLookup.put("Entertainment", "entertainment");
        attributeLookup.put("General", "general");
        attributeLookup.put("Health", "health");
        attributeLookup.put("Science", "science");
        attributeLookup.put("Sports", "sports");
        attributeLookup.put("Technology", "technology");
    }
}

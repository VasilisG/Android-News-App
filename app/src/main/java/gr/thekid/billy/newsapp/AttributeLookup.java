package gr.thekid.billy.newsapp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AttributeLookup {

    protected LinkedHashMap<String, String> attributeLookup;

    public AttributeLookup(){
        attributeLookup = new LinkedHashMap<>();

    }

    public Set<Map.Entry<String, String>> getEntrySet(){
        return attributeLookup.entrySet();
    }

    public String getValue(String key){
        return attributeLookup.get(key);
    }
}

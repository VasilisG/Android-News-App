package gr.thekid.billy.newsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class LanguageAdapter extends BaseAdapter {

    private Context context;
    private LanguageLookup lookup;
    private ArrayList<String> languages;

    public LanguageAdapter(Context context){

        this.context = context;

        lookup = new LanguageLookup();
        languages = new ArrayList<String>();

        for(Map.Entry<String, String> entry : lookup.getEntrySet()){
            String language = entry.getKey();
            languages.add(language);
            Log.v("Language adapter", language);
        }
    }

    public String getLanguage(int i){
        return languages.get(i);
    }

    public ArrayList<String> getLanguages(){
        return languages;
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Object getItem(int i) {
        return this.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_language_row, viewGroup, false);

        TextView languageView = (TextView)row.findViewById(R.id.languageTextView);
        languageView.setText(languages.get(i));

        return row;
    }
}

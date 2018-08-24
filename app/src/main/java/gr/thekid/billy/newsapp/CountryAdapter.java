package gr.thekid.billy.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CountryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> countries;
    private ArrayList<Integer> imageResourceIds;
    private CountryLookup countryLookup;

    public CountryAdapter(Context context){

        this.context = context;
        countries = new ArrayList<String>();
        imageResourceIds = new ArrayList<Integer>();
        countryLookup = new CountryLookup();

        for(Map.Entry<String, String> entry : countryLookup.getEntrySet()){
            String value = entry.getKey();
            countries.add(value);

            String imageName = entry.getValue();
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            imageResourceIds.add(resId);

        }
    }

    public String getCountry(int i){
        return this.countries.get(i);
    }

    @Override
    public int getCount() {
        return this.countries.size();
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
        View row = inflater.inflate(R.layout.custom_country_row, viewGroup, false);

        ImageView imageView = (ImageView)row.findViewById(R.id.countryImageView);
        TextView textView = (TextView)row.findViewById(R.id.countryTextView);

        imageView.setImageResource(imageResourceIds.get(i));
        textView.setText(countries.get(i));

        return row;
    }
}

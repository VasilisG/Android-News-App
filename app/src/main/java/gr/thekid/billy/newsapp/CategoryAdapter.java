package gr.thekid.billy.newsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> categories;
    private int[] categoryImages;
    private CategoryLookup lookup;

    public CategoryAdapter(Context context){

        this.context = context;
        categories = new ArrayList<String>();
        lookup = new CategoryLookup();

        for(Map.Entry<String, String> entry : lookup.getEntrySet()){
            String value = ((String) entry.getValue());
            value = Character.toUpperCase(value.charAt(0)) + value.substring(1);
            categories.add(value);
            Log.v("Custom Adapter", value);
        }

        categoryImages = new int[]{R.drawable.businesslogo, R.drawable.entertainmentlogo, R.drawable.generallogo,
                                   R.drawable.healthlogo, R.drawable.sciencelogo, R.drawable.sportslogo, R.drawable.technologylogo};
    }

    public ArrayList<String> getCategories(){
        return categories;
    }

    public String getCategory(int i){
        return this.categories.get(i);
    }

    @Override
    public int getCount() {
        return categories.size();
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
        View row = inflater.inflate(R.layout.custom_category_row, viewGroup, false);

        ImageView imageView = (ImageView)row.findViewById(R.id.categoryImageView);
        TextView textView = (TextView)row.findViewById(R.id.categoryNameView);

        imageView.setImageResource(categoryImages[i]);
        textView.setText(categories.get(i));

        return row;
    }
}

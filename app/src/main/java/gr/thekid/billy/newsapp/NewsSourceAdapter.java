package gr.thekid.billy.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsSourceAdapter extends ArrayAdapter<NewsSource> {

    private Context context;
    private ArrayList<NewsSource> newsSources;
    private ArrayList<NewsSource> favouriteSources;
    private View customView;

    public NewsSourceAdapter(Context context, ArrayList<NewsSource> newsSources, ArrayList<NewsSource> favouriteSources) {
        super(context, R.layout.custom_news_sources, newsSources);
        this.context = context;
        this.newsSources = newsSources;
        this.favouriteSources = favouriteSources;
    }

    public int getCount(){
        return newsSources.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        customView = inflater.inflate(R.layout.custom_news_sources, parent, false);

        NewsSource source = getItem(position);

        ImageView imageView = (ImageView)customView.findViewById(R.id.newsSourceImageView);
        TextView titleView = (TextView)customView.findViewById(R.id.newsSourceTitleView);
        TextView descriptionView = (TextView)customView.findViewById(R.id.newsSourceDescriptionView);
        ImageView favouriteView = (ImageView)customView.findViewById(R.id.favouriteIconView);

        String imageName = source.getId().replace("-","_");
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        imageView.setImageResource(resId);

        titleView.setText(source.getName());
        descriptionView.setText(source.getDescription());
        updateFavouriteIcon(position, source);

        return customView;
    }

    public void updateFavouriteIcon(int position, NewsSource source){

        ImageView favouriteView = (ImageView)customView.findViewById(R.id.favouriteIconView);

        if(source.isInSourceList(favouriteSources)){
            source.setFavourite(true);
            favouriteView.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else{
            source.setFavourite(false);
            favouriteView.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}

package gr.thekid.billy.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HeadlineAdapter extends ArrayAdapter<Headline> {

    private Context context;
    private ArrayList<Headline> headlines;
    private View customView;

    public HeadlineAdapter(Context context, ArrayList<Headline> headlines) {
        super(context, R.layout.custom_headline_row, headlines);
        this.context = context;
        this.headlines = headlines;
    }

    public int getCount(){
        return headlines.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        customView = inflater.inflate(R.layout.custom_headline_row, parent, false);

        Headline headline = getItem(position);

        ImageView imageView = (ImageView)customView.findViewById(R.id.headlineImageView);
        TextView titleView = (TextView)customView.findViewById(R.id.headlineTitleView);
        TextView descriptionView = (TextView)customView.findViewById(R.id.headlineDescriptionView);
        TextView dateView = (TextView)customView.findViewById(R.id.dateTextView);
        TextView timeView = (TextView)customView.findViewById(R.id.timeTextView);

        setUrlImageToView(imageView, headline.getUrlToImage());
        setTitleToView(titleView, headline.getTitle());
        setDescriptionToView(descriptionView, headline.getDescription());
        setDateToView(dateView, headline.getDatetime());
        setTimeToView(timeView, headline.getDatetime());

        return customView;
    }

    private void setUrlImageToView(ImageView imageView, String imageUrl){
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.headline_placeholder)
                .error(R.drawable.error_icon_placeholder)
                .fit()
                .into(imageView);
    }

    private void setTitleToView(TextView textView, String title){
        if(title == Constants.NULL_STRING) textView.setText(Constants.MISSING_TITLE_MESSAGE);
        else textView.setText(title);
    }

    private void setDescriptionToView(TextView textView, String description){
        if(description == Constants.NULL_STRING) textView.setText(Constants.MISSING_DESCRIPTION_MESSAGE);
        else textView.setText(description);
    }

    private void setDateToView(TextView textView, String datetime){
        String date = datetime.split(" ")[0];
        if(date == Constants.NULL_STRING) textView.setText(Constants.MISSING_DATE_MESSAGE);
        else textView.setText(date);
    }

    private void setTimeToView(TextView textView, String datetime){
        String time = datetime.split(" ")[1];
        if(time == Constants.NULL_STRING) textView.setText(Constants.MISSING_DATE_MESSAGE);
        else textView.setText(time);
    }
}

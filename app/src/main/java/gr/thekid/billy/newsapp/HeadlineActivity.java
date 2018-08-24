package gr.thekid.billy.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HeadlineActivity extends AppCompatActivity {

    private ArrayList<Headline> headlines = new ArrayList<Headline>();
    private HeadlineAdapter headlineAdapter;
    private ListView listView;
    private TextView sourceNameView;

    private DatabaseHelper databaseHelper;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline);

        extras = getIntent().getExtras();
        String sourceHeadlinesUrl = extras.getString(Constants.HEADLINE_URL_KEY);
        String sourceName = extras.getString(Constants.NEWS_SOURCE_KEY);

        sourceNameView = (TextView)findViewById(R.id.sourceNameView);
        sourceNameView.setText(sourceName);

        listView = (ListView)findViewById(R.id.headlineListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Headline headline = headlineAdapter.getItem(position);
                databaseHelper.updateHeadlineDatabase(headline);
                Intent intent = new Intent(getHeadlineActivity(), ViewHeadlineActivity.class);
                intent.putExtra(Constants.ARTICLE_URL_KEY, headline.getUrl());
                startActivity(intent);
            }
        });

        headlineAdapter = new HeadlineAdapter(this, headlines);
        listView.setAdapter(headlineAdapter);
        registerForContextMenu(listView);

        databaseHelper = new DatabaseHelper(this);

        retrieveHeadlines(sourceHeadlinesUrl);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);

        if(view.getId() == R.id.headlineListView){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.headline_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;

        switch(item.getItemId()){

            case R.id.viewArticleItem:
                Headline headline = headlineAdapter.getItem(position);
                databaseHelper.insertHeadline(headline);
                Intent intent = new Intent(this, ViewHeadlineActivity.class);
                intent.putExtra(Constants.ARTICLE_URL_KEY, headline.getUrl());
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public HeadlineActivity getHeadlineActivity(){
        return this;
    }

    public void setHeadlines(ArrayList<Headline> headlines){
        this.headlines = headlines;
    }

    public void setHeadlineAdapter(HeadlineAdapter headlineAdapter){
        this.headlineAdapter = headlineAdapter;
    }

    public HeadlineAdapter getHeadlineAdapter(){
        return headlineAdapter;
    }

    public ListView getListView(){
        return listView;
    }

    public void updateHeadlineAdapter(HeadlineAdapter headlineAdapter){
        this.setHeadlineAdapter(headlineAdapter);
        this.getListView().setAdapter(headlineAdapter);
    }

    public void retrieveHeadlines(String sourceHeadlinesUrl){
        FetchHeadlinesTask task = new FetchHeadlinesTask(sourceHeadlinesUrl,this);
        task.execute();
    }
}

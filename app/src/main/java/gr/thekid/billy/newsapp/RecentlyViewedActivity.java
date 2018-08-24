package gr.thekid.billy.newsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentlyViewedActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayList<Headline> recentHeadlines;
    private HeadlineAdapter headlineAdapter;
    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);

        listView = (ListView)findViewById(R.id.recentlyViewedListView);
        textView = (TextView)findViewById(R.id.noRecentHeadlineToDisplayView);

        databaseHelper = new DatabaseHelper(this);
        recentHeadlines = databaseHelper.loadRecentHeadlines();

        if(recentHeadlines.isEmpty()){
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        headlineAdapter = new HeadlineAdapter(this, recentHeadlines);
        listView.setAdapter(headlineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Headline headline = headlineAdapter.getItem(position);
                viewRecentHeadline(headline);
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);

        if(view.getId() == R.id.recentlyViewedListView){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.recent_headline_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        Headline headline = recentHeadlines.get(position);

        switch (item.getItemId()) {

            case R.id.deleteHeadlineMenuItem:
                confirmDeletionOfRecord(position, headline);
                return true;

            case R.id.viewRecentHeadlineMenuItem:
                viewRecentHeadline(headline);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recent_headline_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.deleteAllHeadlinesMenuItem:
                confirmDeletionOfAllRecords();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAllRecentHeadlines(){
        databaseHelper.clearTable(Constants.RECENT_HEADLINES_TABLE);
        recentHeadlines.clear();
        updateState(headlineAdapter, recentHeadlines);
    }

    private void updateRecentHeadlines(int position, Headline headline){
        databaseHelper.deleteHeadline(headline);
        recentHeadlines = databaseHelper.loadRecentHeadlines();
        updateState(headlineAdapter,recentHeadlines);
    }

    private void updateHeadlineAdapter(ArrayList<Headline> headlines){
        headlineAdapter.clear();
        headlineAdapter.addAll(headlines);
        headlineAdapter.notifyDataSetChanged();
    }

    private void confirmDeletionOfAllRecords(){
        if(!recentHeadlines.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Constants.CONFIRM_DELETION_OF_ALL_RECORDS_MESSAGE)
                    .setPositiveButton(Constants.POSITIVE_ANSWER, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            clearAllRecentHeadlines();
                        }
                    })
                    .setNegativeButton(Constants.CANCEL, null)
                    .show();
        }
        else Toast.makeText(this, Constants.NO_RECENT_HEADLINES, Toast.LENGTH_SHORT).show();
    }

    private void confirmDeletionOfRecord(final int position, final Headline headline){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constants.CONFIRM_DELETION_OF_RECORD)
                .setPositiveButton(Constants.POSITIVE_DELETE_ANSWER, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateRecentHeadlines(position, headline);
                    }
                })
                .setNegativeButton(Constants.CANCEL, null)
                .show();
    }

    private void viewRecentHeadline(Headline headline){
        String url = headline.getUrl();
        Intent intent = new Intent(this, ViewHeadlineActivity.class);
        intent.putExtra(Constants.HEADLINE_URL_KEY, url);
        startActivity(intent);
    }

    private void updateState(HeadlineAdapter headlineAdapter, ArrayList<Headline> headlines){

        updateHeadlineAdapter(headlines);

        ListView listView = findViewById(R.id.recentlyViewedListView);
        TextView textView = findViewById(R.id.noRecentHeadlineToDisplayView);

        if(headlineAdapter.isEmpty()){
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}


package gr.thekid.billy.newsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class NewsSourcesActivity extends AppCompatActivity {

    private ArrayList<NewsSource> newsSources = new ArrayList<NewsSource>();
    private ArrayList<NewsSource> favouriteSources = new ArrayList<NewsSource>();
    private NewsSourceAdapter adapter;
    private ListView listView;

    private Bundle bundle;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_sources);

        bundle = savedInstanceState;

        listView = (ListView)findViewById(R.id.newsSourceListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                browseHeadlines(position);
            }
        });

        databaseHelper = new DatabaseHelper(this);
        favouriteSources = databaseHelper.loadFavouriteSources();

        adapter = new NewsSourceAdapter(this, newsSources, favouriteSources);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        retrieveSources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = (MenuItem)menu.findItem(R.id.searchSourceItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newsTitle) {
                updateSearchResults(adapter, newsTitle);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newsTitle) {
                updateSearchResults(adapter,newsTitle);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.allItem:
                updateScreen(adapter, newsSources);
                break;

            case R.id.categoryItem:
                createCategoryDialog();
                break;

            case R.id.countryItem:
                createCountryDialog();
                break;

            case R.id.languageItem:
                createLanguageDialog();
                break;

            case R.id.favouriteItem:
                updateScreen(adapter, favouriteSources);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu, view, info);

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)info;
        int position = menuInfo.position;
        NewsSource source = adapter.getItem(position);

        if(view.getId() == R.id.newsSourceListView){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.context_menu, menu);
        }

        updateContextMenu(menu, source);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = menuInfo.position;
        NewsSource source = adapter.getItem(position);

        switch(item.getItemId()){

            case R.id.browseHeadlinesItem:
                browseHeadlines(position);
                return true;

            case R.id.addToFavouritesItem:
                if(!source.isInSourceList(favouriteSources)){
                    insertFavouriteSource(source, position);
                    Toast.makeText(this,source.getName() + " added to favourites.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this,source.getName() + " is already in favourites.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.removeFromFavouritesItem:
                if(source.isInSourceList(favouriteSources)){
                    removeFavouriteSource(source, position);
                    Toast.makeText(this,source.getName() + " removed from favourites.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this, "Source is not in favourites' list.", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void retrieveSources(){
        FetchNewsSourcesTask task = new FetchNewsSourcesTask(this);
        task.execute(Constants.NEWS_SOURCES_URL + Constants.API_KEY);
    }

    public NewsSourcesActivity getNewsSourceActivity(){
        return this;
    }

    public NewsSourceAdapter getAdapter(){
        return adapter;
    }

    public ArrayList<NewsSource> getNewsSources(){
        return newsSources;
    }

    public void setNewsSources(ArrayList<NewsSource> newsSources){
        this.newsSources = newsSources;
    }

    public ListView getListView(){
        return listView;
    }

    public void createCategoryDialog(){
        CategoryLookup lookup = new CategoryLookup();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview,null);

        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);

        final ListView listView = (ListView)row.findViewById(R.id.customListView);
        listView.setAdapter(categoryAdapter);

        builder.setView(row);
        builder.setAdapter(categoryAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String categoryKey = categoryAdapter.getCategory(i).toLowerCase();
                NewsFilter filter = new NewsFilter(newsSources);
                ArrayList<NewsSource> sources = filter.getFilteredByCategory(categoryKey);
                updateScreen(getAdapter(), sources);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Resources r = getResources();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constants.DIP_CATEGORY_ALERT_DIALOG, r.getDisplayMetrics());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int)(displayRectangle.width() * 0.8f), (int)(pixels * (lookup.getEntrySet().size()+1)));
    }

    public void createCountryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview, null);

        final CountryLookup lookup = new CountryLookup();
        final CountryAdapter countryAdapter = new CountryAdapter(this);

        final ListView listView = (ListView)row.findViewById(R.id.customListView);
        listView.setAdapter(countryAdapter);

        builder.setView(row);
        builder.setAdapter(countryAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String countryKey = countryAdapter.getCountry(i);
                NewsFilter filter = new NewsFilter(newsSources);
                ArrayList<NewsSource> sources = filter.getFilteredByCountry(lookup.getValue(countryKey));
                updateScreen(getAdapter(), sources);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createLanguageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview, null);

        final LanguageLookup lookup = new LanguageLookup();
        final LanguageAdapter languageAdapter = new LanguageAdapter(this);

        final ListView listView = (ListView)row.findViewById(R.id.customListView);
        listView.setAdapter(languageAdapter);

        builder.setView(row);
        builder.setAdapter(languageAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String languageKey = languageAdapter.getLanguage(i);
                NewsFilter filter = new NewsFilter(newsSources);
                ArrayList<NewsSource> sources = filter.getFilteredByLanguage(lookup.getValue(languageKey));
                updateScreen(getAdapter(), sources);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void resetAdapter(NewsSourceAdapter newsSourceAdapter, ArrayList<NewsSource> sources){
        newsSourceAdapter.clear();
        if(sources != null && sources.size() > 0){
            newsSourceAdapter.addAll(sources);
        }
        newsSourceAdapter.notifyDataSetChanged();
    }

    public void updateSearchResults(NewsSourceAdapter adapter, String newsTitle){
        NewsFilter filter = new NewsFilter(newsSources);
        ArrayList<NewsSource> sources = filter.getFilteredByName(newsTitle);
        updateScreen(adapter,sources);

    }

    private void updateView(NewsSourceAdapter adapter){
        ListView listView = (ListView)findViewById(R.id.newsSourceListView);
        TextView textView = (TextView)findViewById(R.id.noNewsSourceToDisplayView);
        if(adapter.getCount()==0){
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    private void updateScreen(NewsSourceAdapter adapter, ArrayList<NewsSource> sources){
        resetAdapter(adapter,sources);
        updateView(adapter);
    }

    private void browseHeadlines(int position){
        String sourceUrlString = Constants.TOP_HEADLINES_URL + "sources=" + adapter.getItem(position).getId() + "&apiKey=" + Constants.API_KEY;
        String source = adapter.getItem(position).getName();
        Intent intent = new Intent(getNewsSourceActivity(), HeadlineActivity.class);
        intent.putExtra(Constants.HEADLINE_URL_KEY, sourceUrlString);
        intent.putExtra(Constants.NEWS_SOURCE_KEY, source);
        startActivity(intent);
    }

    private void insertFavouriteSource(NewsSource source, int position){
        databaseHelper.insertNewsSource(source);
        favouriteSources.add(source);
        adapter.updateFavouriteIcon(position,source);
        updateScreen(adapter,newsSources);
    }

    private void removeFavouriteSource(NewsSource source, int position){
        databaseHelper.deleteNewsSource(source);
        favouriteSources.remove(position);
        adapter.updateFavouriteIcon(position,source);
        updateScreen(adapter,favouriteSources);
    }

    private void updateContextMenu(ContextMenu menu, NewsSource source){
        if(source.isFavourite()){
            menu.getItem(Constants.ADD_TO_FAVOURITES_ITEM_POSITION).setEnabled(false);
            menu.getItem(Constants.REMOVE_FROM_FAVOURITES_ITEM_POSITION).setEnabled(true);
        }
        else {
            menu.getItem(Constants.ADD_TO_FAVOURITES_ITEM_POSITION).setEnabled(true);
            menu.getItem(Constants.REMOVE_FROM_FAVOURITES_ITEM_POSITION).setEnabled(false);
        }
    }
}
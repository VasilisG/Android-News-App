package gr.thekid.billy.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context,Constants.DB_NAME,null,Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, Constants.DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        updateDatabase(sqLiteDatabase, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if(oldVersion < 1){
            createNewsSourceTable(sqLiteDatabase);
            createHeadlinesTable(sqLiteDatabase);
        }
    }

    private void createNewsSourceTable(SQLiteDatabase sqLiteDatabase){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + Constants.FAVOURITE_SOURCES_TABLE + " ");
        builder.append("( _id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append("SOURCE_ID TEXT NOT NULL, ");
        builder.append("NAME TEXT, ");
        builder.append("DESCRIPTION TEXT, ");
        builder.append("URL TEXT, ");
        builder.append("CATEGORY TEXT, ");
        builder.append("LANGUAGE TEXT, ");
        builder.append("COUNTRY TEXT );");

        sqLiteDatabase.execSQL(builder.toString());
    }

    private void createHeadlinesTable(SQLiteDatabase sqLiteDatabase){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + Constants.RECENT_HEADLINES_TABLE + " ");
        builder.append("( _id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append("AUTHOR TEXT, ");
        builder.append("TITLE TEXT, ");
        builder.append("DESCRIPTION TEXT, ");
        builder.append("URL TEXT, ");
        builder.append("URL_TO_IMAGE TEXT, ");
        builder.append("PUBLISHED_AT TEXT );");

        sqLiteDatabase.execSQL(builder.toString());
    }


    public void insertNewsSource(NewsSource newsSource){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues sourceValues = new ContentValues();
        sourceValues.put("SOURCE_ID", newsSource.getId());
        sourceValues.put("NAME", newsSource.getName());
        sourceValues.put("DESCRIPTION", newsSource.getDescription());
        sourceValues.put("URL", newsSource.getUrl());
        sourceValues.put("CATEGORY", newsSource.getCategory());
        sourceValues.put("LANGUAGE", newsSource.getLanguage());
        sourceValues.put("COUNTRY", newsSource.getCountry());

        database.insert(Constants.FAVOURITE_SOURCES_TABLE, null, sourceValues);
    }

    public void insertHeadline(Headline headline){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues headlineValues = new ContentValues();
        headlineValues.put("AUTHOR", headline.getAuthor());
        headlineValues.put("TITLE", headline.getTitle());
        headlineValues.put("DESCRIPTION", headline.getDescription());
        headlineValues.put("URL", headline.getUrl());
        headlineValues.put("URL_TO_IMAGE", headline.getUrlToImage());
        headlineValues.put("PUBLISHED_AT", headline.getPublishedAt());

        database.insert(Constants.RECENT_HEADLINES_TABLE, null, headlineValues);
    }

    public void deleteNewsSource(NewsSource newsSource){
        SQLiteDatabase database = getWritableDatabase();
        String sourceId = newsSource.getId();
        database.delete(Constants.FAVOURITE_SOURCES_TABLE, "SOURCE_ID = ?", new String[] {sourceId});
    }

    public void deleteHeadline(Headline headline){
        SQLiteDatabase database = getWritableDatabase();
        String url = headline.getUrl();
        database.delete(Constants.RECENT_HEADLINES_TABLE, "URL = ?", new String[] {url});
    }

    public ArrayList<NewsSource> loadFavouriteSources(){
        SQLiteDatabase database;
        ArrayList<NewsSource> favouriteSources = new ArrayList<NewsSource>();
        int count = 0;

        String query = "SELECT * FROM FAVOURITE_SOURCES";
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        count = cursor.getCount();
        if(count == 0){
            return favouriteSources;
        }
        else {
            while (cursor.moveToNext()) {
                NewsSource source = getSourceFromDatabase(cursor);
                favouriteSources.add(source);
            }

            return favouriteSources;
        }
    }

    private NewsSource getSourceFromDatabase(Cursor cursor){
        String id = cursor.getString(1);
        String name = cursor.getString(2);
        String description = cursor.getString(3);
        String url = cursor.getString(4);
        String category = cursor.getString(5);
        String language = cursor.getString(6);
        String country = cursor.getString(7);

        NewsSource source = new NewsSource(id, name, description, url, category, language, country);
        source.setFavourite(true);
        return source;
    }

    public ArrayList<Headline> loadRecentHeadlines(){
        SQLiteDatabase database;
        ArrayList<Headline> recentHeadlines = new ArrayList<Headline>();
        int count = 0;

        String query = "SELECT * FROM RECENT_HEADLINES";
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        count = cursor.getCount();
        if(count == 0){
            return recentHeadlines;
        }
        else {
            while (cursor.moveToNext()) {
                Headline headline = getHeadlineFromDatabase(cursor);
                recentHeadlines.add(headline);
            }

            return recentHeadlines;
        }
    }

    private Headline getHeadlineFromDatabase(Cursor cursor){
        String author = cursor.getString(1);
        String title = cursor.getString(2);
        String description = cursor.getString(3);
        String url = cursor.getString(4);
        String urlToImage = cursor.getString(5);
        String publishedAt = cursor.getString(6);

        Headline headline = new Headline(author, title, description, url, urlToImage, publishedAt);
        return headline;
    }

    public void updateHeadlineDatabase(Headline headline){
        SQLiteDatabase database = getWritableDatabase();
        int rows = (int)DatabaseUtils.queryNumEntries(database,Constants.RECENT_HEADLINES_TABLE);
        ArrayList<Integer> IDs = new ArrayList<Integer>();

        insertHeadline(headline);
        IDs = getRecordIDs(database);

        if(rows > Constants.MAX_HEADLINE_RECORDS){
            Integer minId = Collections.min(IDs);
            database.delete(Constants.RECENT_HEADLINES_TABLE, "_id = ?", new String[] {minId.toString()});
        }
    }

    private ArrayList<Integer> getRecordIDs(SQLiteDatabase database){
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        String query = "SELECT * FROM RECENT_HEADLINES";
        Cursor cursor = database.rawQuery(query,null);
        int count = cursor.getCount();
        if(count == 0){
            database.close();
        }
        else {
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(0);
                IDs.add(currentId);
            }
            database.close();
        }
        return IDs;
    }

    public void clearTable(String tableName){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(tableName,null,null);
    }

}

package gr.thekid.billy.newsapp;

import android.app.ProgressDialog;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchHeadlinesTask extends AsyncTask<String, Void, ArrayList<Headline>> {

    private String sourceHeadlinesUrl;
    private HeadlineActivity headlineActivity;
    private ProgressDialog progressDialog;

    public FetchHeadlinesTask(String sourceHeadlinesUrl, HeadlineActivity headlineActivity){
        this.sourceHeadlinesUrl = sourceHeadlinesUrl;
        this.headlineActivity = headlineActivity;
        progressDialog = new ProgressDialog(this.headlineActivity);
    }


    @Override
    protected void onPreExecute(){
        progressDialog.setTitle(Constants.LOADING_HEADLINES_DIALOG_MESSAGE);
        progressDialog.show();
    }

    @Override
    protected ArrayList<Headline> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String LOG_TAG = FetchNewsSourcesTask.class.getSimpleName();

        String headlinesJsonStr;

        try{
            URL url = new URL(sourceHeadlinesUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                return null;
            }

            headlinesJsonStr = buffer.toString();
            Log.v(LOG_TAG, "News headline JSON string: " + headlinesJsonStr);
            return getHeadlines(headlinesJsonStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Headline> result){
        headlineActivity.setHeadlines(result);
        HeadlineAdapter adapter = headlineActivity.getHeadlineAdapter();
        headlineActivity.getListView().setAdapter(adapter);
        adapter.clear();
        adapter.addAll(result);
        adapter.notifyDataSetChanged();

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        return;
    }

    @Override
    protected void onProgressUpdate(Void...values){

    }

    private ArrayList<Headline> getHeadlines(String headlineJsonString){
        String author, title, description, url, urlToImage, publishedAt;
        String status;
        int totalResults;

        JSONObject jsonObject;
        JSONArray jsonArray;

        ArrayList<Headline> sourceHeadlines = new ArrayList<Headline>();
        Headline headline;

        try {
            jsonObject = new JSONObject(headlineJsonString);

            status = jsonObject.getString("status");
            if(!status.equals(Constants.JSON_STATUS_OK)){
                return null;
            }
             else{
                totalResults = jsonObject.getInt("totalResults");
                jsonArray = jsonObject.getJSONArray("articles");

                for(int i=0; i<totalResults; i++){
                    author = jsonArray.getJSONObject(i).getString("author");
                    title = jsonArray.getJSONObject(i).getString("title");
                    description = jsonArray.getJSONObject(i).getString("description");
                    url = jsonArray.getJSONObject(i).getString("url");
                    urlToImage = jsonArray.getJSONObject(i).getString("urlToImage");
                    publishedAt = jsonArray.getJSONObject(i).getString("publishedAt");

                    headline = new Headline(author, title, description, url, urlToImage, publishedAt);
                    Log.v("News headline No" + i, headline.toString());
                    sourceHeadlines.add(headline);
                }
                return sourceHeadlines;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

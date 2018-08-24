package gr.thekid.billy.newsapp;

import android.app.ProgressDialog;
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
import java.util.List;

public class FetchNewsSourcesTask extends AsyncTask<String, Void, List<NewsSource>> {

    private NewsSourcesActivity newsSourcesActivity;
    private ProgressDialog progressDialog;

    public FetchNewsSourcesTask(NewsSourcesActivity newsSourcesActivity){
        this.newsSourcesActivity = newsSourcesActivity;
        progressDialog = new ProgressDialog(this.newsSourcesActivity);
    }

    @Override
    protected void onPreExecute(){
        progressDialog.setMessage(Constants.LOADING_SOURCES_DIALOG_MESSAGE);
        progressDialog.show();
    }

    @Override
    protected List<NewsSource> doInBackground(String... params) {
        if(params.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String LOG_TAG = FetchNewsSourcesTask.class.getSimpleName();

        String sourcesJsonStr;

        try{
            URL url = new URL(Constants.NEWS_SOURCES_URL + Constants.API_KEY);

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

            sourcesJsonStr = buffer.toString();
            Log.v(LOG_TAG, "News sources JSON string: " + sourcesJsonStr);
            return getNewsSources(sourcesJsonStr);

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
    protected void onPostExecute(List<NewsSource> sources){
        newsSourcesActivity.setNewsSources((ArrayList<NewsSource>)sources);
        NewsSourceAdapter adapter = newsSourcesActivity.getAdapter();
        adapter.addAll(sources);
        newsSourcesActivity.getListView().setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    private ArrayList<NewsSource> getNewsSources(String sourcesJsonStr){

        String id, name, description, url, category, language, country;
        String status;
        JSONObject jsonObject;
        JSONArray jsonArray;

        ArrayList<NewsSource> newsSources = new ArrayList<NewsSource>();
        NewsSource source;

        try {
            jsonObject = new JSONObject(sourcesJsonStr);
            status = jsonObject.getString("status");
            Log.v("STATUS ", status);

            if(!status.equals("ok")){
                return null;
            }
            else {
                jsonArray = jsonObject.getJSONArray("sources");

                for(int i=0; i<jsonArray.length(); i++){
                    id = jsonArray.getJSONObject(i).getString("id");
                    name = jsonArray.getJSONObject(i).getString("name");
                    description = jsonArray.getJSONObject(i).getString("description");
                    url = jsonArray.getJSONObject(i).getString("url");
                    category = jsonArray.getJSONObject(i).getString("category");
                    language = jsonArray.getJSONObject(i).getString("language");
                    country = jsonArray.getJSONObject(i).getString("country");

                    source = new NewsSource(id, name, description, url, category, language, country);
                    Log.v("News source No" + i, source.toString());
                    newsSources.add(source);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsSources;
    }

}

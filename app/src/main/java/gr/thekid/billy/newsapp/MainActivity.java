package gr.thekid.billy.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private boolean online = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.shareMenuItem:
                shareApp();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSources(View view){
        online = notifyOnline();
        if(online){
            Intent intent = new Intent(this, NewsSourcesActivity.class);
            startActivity(intent);
        }
        else Toast.makeText(getApplicationContext(),Constants.NETWORK_IS_DOWN_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    public void showRecentlyViewed(View view){
        Intent intent = new Intent(this, RecentlyViewedActivity.class);
        startActivity(intent);
    }

    private void setButtonBackground(int alpha){
        Button viewSourcesButton = findViewById(R.id.viewSourcesButton);
        Button recentlyViewedButton = findViewById(R.id.recentlyViewedButton);

        viewSourcesButton.getBackground().setAlpha(alpha);
        recentlyViewedButton.getBackground().setAlpha(alpha);
    }

    public boolean notifyOnline(){

        try {
            CheckConnectionTask task = new CheckConnectionTask(online);
            return task.execute().get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.REPOSITORY_URL);
        startActivity(Intent.createChooser(shareIntent, Constants.SHARE_MESSAGE));
    }
}
